package com.cotato.nightview.naverapi;

import com.cotato.nightview.coord.Coord;
import com.cotato.nightview.kakaoapi.KakaoApiService;
import com.cotato.nightview.place.PlaceDto;
import com.cotato.nightview.place.PlaceRepository;
import com.cotato.nightview.place.PlaceService;
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

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class NaverApiService {
    private final PlaceService placeService;
    private final KakaoApiService kakaoApiService;

    public void getPlacesFromApi() {

        // 초기 URI 생성
        String InitUri = InitUri().toString();

        // 지역 목록을 JSONArray로 변환
        String areaInfoJson = readFileAsString("dong_coords.json");
        JSONArray areaInfo = parseJsonArray(areaInfoJson, "areaInfo");
        int i = 0;
        for (Object areaObj : areaInfo) {
            try {
                Thread.sleep(70);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("areaInfo = " + areaInfo.size());
            JSONObject areaObjJson = (JSONObject) areaObj;
            String dong = areaObjJson.get("gu").toString();
            System.out.println("gu = " + dong + i++);

            URI uri1 = buildUriByAreaName(InitUri, dong);

            // URI로 요청 엔티티 생성
            RequestEntity<Void> requestEntity = buildRequestEntity(uri1);

            // API 호출 후 응답을 String 형식 Json으로 받음
            ResponseEntity<String> res = callNaverApi(requestEntity);

            // API 응답 중 실제 장소 정보인 "items"만 파싱
            JSONArray items = parseJsonArray(res.getBody(), "items");

            // items를 실제 Dto로 변환
            PlaceDto placeDtos[] = itemsToDto(items);
            for (PlaceDto dto : placeDtos) {
                if (placeService.vaildPlace(dto)) {
                    Coord coord = kakaoApiService.transCoord(dto.getLongitude(), dto.getLatitude());
                    dto.setLongitude(coord.getX());
                    dto.setLatitude(coord.getY());
                    placeService.savePlace(dto);
                }
            }
        }
    }

    private static URI buildUriByAreaName(String InitUri, String areaName) {
        return UriComponentsBuilder.fromUriString(InitUri)
                .replaceQueryParam("query", areaName + " 야경")
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

    private static String readFileAsString(String file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONArray parseJsonArray(String jsonString, String arrayName) {
        // JSON 파싱을 위한 parser 생성
        JSONParser parser = new JSONParser();

        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 응답 Body에서 장소 정보들만 JSONArray로 바꿈
        return (JSONArray) jsonObject.get(arrayName);
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

    private static ResponseEntity<String> callNaverApi(RequestEntity<Void> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(requestEntity, String.class);
    }
}
