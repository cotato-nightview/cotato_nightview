package com.cotato.nightview.api;

import com.cotato.nightview.json.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:config.properties")
public class NaverExteranlApi implements ExteranlApi {
    private final JsonUtil jsonUtil;
    @Value("${api.X-Naver-Client-ID}")
    private String X_Naver_Client_ID;
    @Value("${api.X-Naver-Client-Secret}")
    private String X_Naver_Client_Secret;

    // param을 검색어로 검색함
    public JSONArray getPlaces(String param) {
        // 지역 이름으로 uri 생성
        URI uri = buildGetPlacesUri(param);

        // URI로 요청 엔티티 생성
        RequestEntity<Void> requestEntity = buildRequestEntity(uri);

        // API 호출 후 응답을 String 형식 Json으로 받음
        ResponseEntity<String> res = callApi(requestEntity);

        // API 응답 중 실제 장소 정보인 "items"만 파싱
        JSONArray items = jsonUtil.parseJsonArray(res.getBody(), "items");

        return items;
    }

    public URI buildGetPlacesUri(String param) {
        return UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query", param)
                .queryParam("display", "5")
                .queryParam("sort", "random")
                .encode()
                .build()
                .toUri();
    }

    @Override
    public RequestEntity<Void> buildRequestEntity(URI uri) {
        return RequestEntity.get(uri)
                .header("X-Naver-Client-ID", X_Naver_Client_ID)
                .header("X-Naver-Client-Secret", X_Naver_Client_Secret)
                .build();
    }

    @Override
    public ResponseEntity<String> callApi(RequestEntity<Void> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(requestEntity, String.class);
    }
}
