package com.cotato.nightview.place;

import com.cotato.nightview.api.naverapi.NaverApiService;
import com.cotato.nightview.coord.Coord;
import com.cotato.nightview.coord.CoordService;
import com.cotato.nightview.dong.Dong;
import com.cotato.nightview.dong.DongService;
import com.cotato.nightview.exception.InvaildLocationException;
import com.cotato.nightview.json.JsonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final NaverApiService naverApiService;
    private final CoordService coordService;
    private final JsonService jsonService;
    private final DongService dongService;
    private final ModelMapper modelMapper;

    public List<PlaceDto> entitiesToDtos(List<Place> placeEntityList) {
        return placeEntityList.stream().map(place -> modelMapper.map(place, PlaceDto.class))
                .collect(Collectors.toList());
    }

    public String insertPlace(String name) {

        JSONArray placesFromApi = naverApiService.getPlacesFromApi(name);

        PlaceDto[] placeDtos = itemsToDto(placesFromApi);

        PlaceDto dto = null;

        try {
            dto = removeHtmlTags(placeDtos[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return "no search result";
        }

        if (vaildPlace(dto)) {
            setCoord(dto);
            Dong dong = dongService.getDongFromAddress(dto.getAddress());
            savePlace(dto, dong);

            return dto.toString();
        }

        return "not proper place";
    }

    public void initPlace() {
        String areaInfoJson = jsonService.readFileAsString("dong_coords.json");

        // 지역 목록을 JSONArray로 변환
        JSONArray areaInfoArray = jsonService.parseJsonArray(areaInfoJson, "areaInfo");

        int i = 0;
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

            System.out.println(areaName + "    " + ++i + "/" + areaInfoArray.size());

            // API에서 장소정보 불러와 JSONArray에 저장
            JSONArray placesFromApi = naverApiService.getPlacesFromApi(areaName + " 야경");

            // JSONArray를 dto 배열로 변환
            PlaceDto placeDtos[] = itemsToDto(placesFromApi);

            // DB에 저장
            savePlaces(placeDtos);
        }

    }

    private void setCoord(PlaceDto dto) {
        Coord coord = coordService.transCoord(dto.getLongitude(), dto.getLatitude());
        dto.setLongitude(coord.getX());
        dto.setLatitude(coord.getY());
    }

    public PlaceDto[] itemsToDto(JSONArray items) {
        ObjectMapper objectMapper = new ObjectMapper();

        //선언한 필드만 매핑
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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

    public boolean vaildPlace(PlaceDto dto) {
        // 카테고리가 적절한지 검사
        if (!(dto.getCategory().contains("명소") || dto.getCategory().contains("지명"))) {
            return false;
        }
        // 서울 내에 있는지 검사
        if (!(dto.getAddress().contains("서울"))) {
            return false;
        }
        // 중복 검사
        return placeRepository.findByTitle(dto.getTitle()) == null;
    }

    private static PlaceDto removeHtmlTags(PlaceDto dto) {
        dto.setTitle(dto.getTitle().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", ""));
        return dto;
    }

    public void savePlaces(PlaceDto[] placeDtos) {
        for (PlaceDto dto : placeDtos) {
            if (vaildPlace(dto)) {
                // 좌표계 변환 후 DB에 저장
                setCoord(dto);
                Dong dong = dongService.getDongFromAddress(dto.getAddress());
                savePlace(dto, dong);
            }
        }
    }

    public void savePlace(PlaceDto dto, Dong dong) {
        placeRepository.save(dto.toEntity(dong));
    }

    public Place findByTitle(String title) {
        return placeRepository.findByTitle(title);
    }

    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    public List<Place> findAllByDongIn(List<Dong> dongList) {
        return placeRepository.findAllByDongIn(dongList);
    }

    public List<Place> findAllWtihInDistance(double longitude, double latitude, double distanceWithIn) {
        if (!isVaildLocation(longitude, latitude)) {
            throw new InvaildLocationException("지원하지 않는 위치입니다.");
        }
        return placeRepository.findAllWtihInDistance(longitude, latitude, distanceWithIn);
    }

    private boolean isVaildLocation(double longitude, double latitude) {
        if (latitude > 38.61 || latitude < 33.11) return false;
        if (longitude > 131.87 || longitude < 124.6) return false;
        return true;
    }
}
