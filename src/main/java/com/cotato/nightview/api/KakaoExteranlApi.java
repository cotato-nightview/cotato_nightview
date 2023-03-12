package com.cotato.nightview.api;

import com.cotato.nightview.json.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
public class KakaoExteranlApi implements ExteranlApi {
    private final JsonUtil jsonUtil;

    @Value("${api.Kakao-Rest-Api-Key}")
    private String Kakao_Rest_Api_Key;
    public JSONObject transCoord(double mapx, double mapy) {
        // 좌표로 URI 생성
        URI uri = buildTransCoordUri(mapx, mapy);

        // URI로 요청 엔티티 생성
        RequestEntity<Void> requestEntity = buildRequestEntity(uri);

        // API 호출 후 응답을 String 형식 Json으로 받음
        ResponseEntity<String> res = callApi(requestEntity);

        // API 응답 중 실제 좌표 정보인 "documents"만 파싱
        JSONArray jsonArray = jsonUtil.parseJsonArray(res.getBody(), "documents");

        // documents를 json 객체로 변환 후 반환
        return (JSONObject) jsonArray.get(0);
    }

    public URI buildTransCoordUri(double mapx, double mapy) {
        return UriComponentsBuilder.fromUriString("http://dapi.kakao.com")
                .path("/v2/local/geo/transcoord.json")
                .queryParam("x", mapx)
                .queryParam("y", mapy)
                .queryParam("input_coord", "KTM")
                .queryParam("output_coord", "WGS84")
                .encode()
                .build()
                .toUri();
    }

    @Override
    public RequestEntity<Void> buildRequestEntity(URI uri) {
        return RequestEntity.get(uri)
                .header("Authorization", "KakaoAK " + Kakao_Rest_Api_Key)
                .build();
    }

    @Override
    public ResponseEntity<String> callApi(RequestEntity<Void> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(requestEntity, String.class);
    }
}














