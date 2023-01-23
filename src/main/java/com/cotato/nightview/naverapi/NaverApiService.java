package com.cotato.nightview.naverapi;

import com.cotato.nightview.kakaoapi.Coord;
import com.cotato.nightview.kakaoapi.KakaoApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class NaverApiService {
    private final NaverApiRepository naverApiRepository;
    private final KakaoApiService kakaoApiService;
    public void getPlacesFromApi() {

        // 초기 URI 생성
        String InitUri = InitUri().toString();

        String areaName = "마포구";
        URI uri1 = buildUriByAreaName(InitUri, areaName);

        // URI로 요청 엔티티 생성
        RequestEntity<Void> requestEntity = buildRequestEntity(uri1);

        // API 호출 후 응답을 String 형식 Json으로 받음
        ResponseEntity<String> res = callNaverApi(requestEntity);

        // API 응답 중 실제 장소 정보인 "items"만 파싱
        JSONArray items = parseItemsJson(res);

        // items를 실제 Dto로 변환
        PlaceDto placeDtos[] = itemsToDto(items);
        for (PlaceDto dto : placeDtos) {
            Coord coord = kakaoApiService.transCoord(dto.getMapx(), dto.getMapy());
            dto.setMapx(coord.getX());
            dto.setMapy(coord.getY());
            savePlace(dto);
        }
    }

    private void savePlace(PlaceDto dto) {
        if(dto.getCategory().contains("명소")) {
            naverApiRepository.save(dto.toEntity());
        }
    }

    private static URI buildUriByAreaName(String InitUri, String areaName) {
        return UriComponentsBuilder.fromUriString(InitUri)
                .replaceQueryParam("query", areaName + "야경")
                .encode()
                .build()
                .toUri();
    }

    private static PlaceDto[] itemsToDto(JSONArray items) {
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

    private static JSONArray parseItemsJson(ResponseEntity<String> res) {
        // JSON 파싱을 위한 parser 생성
        JSONParser parser = new JSONParser();

        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(res.getBody());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 응답 Body에서 장소 정보들만 JSONArray로 바꿈
        return (JSONArray) jsonObject.get("items");
    }

    private static URI InitUri() {
        return UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query", "")
                .queryParam("display", "5")
                .queryParam("sort", "random")
                .encode()
                .build()
                .toUri();

    }

    private static RequestEntity<Void> buildRequestEntity(URI uri) {
        return RequestEntity.get(uri)
                .header("X-Naver-Client-ID", "rs3jbPtyp96zx_0CSktZ")
                .header("X-Naver-Client-Secret", "QtBYuGAEHT")
                .build();
    }

    public ResponseEntity<String> callNaverApi(RequestEntity<Void> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(requestEntity, String.class);
    }
}
