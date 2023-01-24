package com.cotato.nightview.place;

import com.cotato.nightview.api.kakaoapi.KakaoApiService;
import com.cotato.nightview.api.naverapi.NaverApiService;
import com.cotato.nightview.coord.Coord;
import com.cotato.nightview.json.JsonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final NaverApiService naverApiService;
    private final KakaoApiService kakaoApiService;
    private final JsonService jsonService;


    public void savePlaces(PlaceDto[] placeDtos) {
        for (PlaceDto dto : placeDtos) {
            if (vaildPlace(dto)) {
                // 좌표계 변환 후 DB에 저장
                Coord coord = kakaoApiService.transCoord(dto.getMapx(), dto.getMapy());
                dto.setMapx(coord.getX());
                dto.setMapy(coord.getY());
                savePlace(dto);
            }
        }
    }

    public void savePlace(PlaceDto dto) {
        placeRepository.save(dto.toEntity());
    }

    public boolean vaildPlace(PlaceDto dto) {
        // 카테고리가 적절한지 검사
        if (!(dto.getCategory().contains("명소") || dto.getCategory().contains("지명"))) {
            return false;
        }
        // 중복 검사
        return placeRepository.findByTitle(dto.getTitle()) == null;
    }

    public PlaceDto[] itemsToDto(JSONArray items) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);   //선언한 필드만 매핑

        String jsonSting;
        try {
            jsonSting = objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            return objectMapper.readValue(jsonSting, PlaceDto[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void initPlace() {
        String areaInfoJson = jsonService.readFileAsString("dong_coords.json");

        // 지역 목록을 JSONArray로 변환
        JSONArray areaInfoArray = jsonService.parseJsonArray(areaInfoJson, "areaInfo");

        // 각 지역 목록을 순회하며 장소 탐색
        for (Object areaObj : areaInfoArray) {
            // 429 에러 방지를 위한 delay
            try {
                Thread.sleep(70);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            JSONObject areaObjJson = (JSONObject) areaObj;
            String areaName = areaObjJson.get("dong").toString();

            // API에서 장소정보 불러와 JSONArray에 저장
            JSONArray placesFromApi = naverApiService.getPlacesFromApi(areaName);

            // JSONArray를 dto 배열로 변환
            PlaceDto placeDtos[] = itemsToDto(placesFromApi);

            // DB에 저장
            savePlaces(placeDtos);
        }

    }

}
