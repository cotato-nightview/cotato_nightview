package com.cotato.nightview.api.kakaoapi;

import com.cotato.nightview.api.ApiService;
import com.cotato.nightview.coord.Coord;
import com.cotato.nightview.json.JsonService;
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
public class KakaoApiService implements ApiService {
    private final JsonService jsonService;

    public Coord transCoord(double mapx, double mapy) {
        // 좌표로 URI 생성
        URI uri = buildUri(mapx, mapy);
        System.out.println("mapx = " + mapx);
        System.out.println("mapy = " + mapy);
        // URI로 요청 엔티티 생성
        RequestEntity<Void> requestEntity = buildRequestEntity(uri);

        // API 호출 후 응답을 String 형식 Json으로 받음
        ResponseEntity<String> res = callApi(requestEntity);

        // API 응답 중 실제 좌표 정보인 "documents"만 파싱
//        JSONArray jsonArray = parseDocumentsJson(res);
        JSONArray jsonArray = jsonService.parseJsonArray(res.getBody(),"documents");

        // documents를 json 객체로 변환
        JSONObject coordJson = (JSONObject) jsonArray.get(0);

        // java 객체로 변환 후 return
        return jsonToCoord(coordJson);
    }

    private static Coord jsonToCoord(JSONObject coordJson) {
        return new Coord((double) coordJson.get("x"), (double) coordJson.get("y"));
    }
    @Override
    public URI buildUri(Object mapx, Object mapy) {
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
                .header("Authorization", "KakaoAK " + "029fe9d698f7b84b9bb093770125665f")
                .build();
    }

    @Override
    public ResponseEntity<String> callApi(RequestEntity<Void> requestEntity) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(requestEntity, String.class);
    }
}
