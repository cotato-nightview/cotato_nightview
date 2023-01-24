package com.cotato.nightview.api.naverapi;

import com.cotato.nightview.api.ApiService;
import com.cotato.nightview.coord.Coord;
import com.cotato.nightview.json.JsonService;
import com.cotato.nightview.api.kakaoapi.KakaoApiService;
import com.cotato.nightview.place.PlaceDto;
import com.cotato.nightview.place.PlaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class NaverApiService implements ApiService {
    private final JsonService jsonService;

    public JSONArray getPlacesFromApi(String areaName) {

        // 초기 URI 생성
        String InitUri = "https://openapi.naver.com";

        // 지역 이름으로 uri 생성
        URI uri = buildUri(InitUri, areaName);

        // URI로 요청 엔티티 생성
        RequestEntity<Void> requestEntity = buildRequestEntity(uri);

        // API 호출 후 응답을 String 형식 Json으로 받음
        ResponseEntity<String> res = callApi(requestEntity);

        // API 응답 중 실제 장소 정보인 "items"만 파싱
        JSONArray items = jsonService.parseJsonArray(res.getBody(), "items");

        return items;


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

    @Override
    public URI buildUri(Object InitUri, Object areaName) {
        return UriComponentsBuilder.fromUriString((String) InitUri)
                .path("/v1/search/local.json")
                .queryParam("query", areaName + " 야경")
                .queryParam("display", "5")
                .queryParam("sort", "random")
                .encode()
                .build()
                .toUri();
    }

    @Override
    public RequestEntity<Void> buildRequestEntity(URI uri) {
        return RequestEntity.get(uri)
                .header("X-Naver-Client-ID", "rs3jbPtyp96zx_0CSktZ")
                .header("X-Naver-Client-Secret", "QtBYuGAEHT")
                .build();
    }

    @Override
    public ResponseEntity<String> callApi(RequestEntity<Void> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(requestEntity, String.class);
    }
}
