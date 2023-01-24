package com.cotato.nightview.coord;

import com.cotato.nightview.api.kakaoapi.KakaoApiService;
import com.cotato.nightview.json.JsonService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class CoordService {
    private final KakaoApiService kakaoApiService;
    private final JsonService jsonService;

    public Coord transCoord(double mapx, double mapy) {
        // 좌표로 URI 생성
        URI uri = kakaoApiService.buildUri(mapx, mapy);
        System.out.println("mapx = " + mapx);
        System.out.println("mapy = " + mapy);

        // URI로 요청 엔티티 생성
        RequestEntity<Void> requestEntity = kakaoApiService.buildRequestEntity(uri);

        // API 호출 후 응답을 String 형식 Json으로 받음
        ResponseEntity<String> res = kakaoApiService.callApi(requestEntity);

        // API 응답 중 실제 좌표 정보인 "documents"만 파싱
        JSONArray jsonArray = jsonService.parseJsonArray(res.getBody(),"documents");

        // documents를 json 객체로 변환
        JSONObject coordJson = (JSONObject) jsonArray.get(0);

        // java 객체로 변환 후 return
        return jsonToCoord(coordJson);
    }

    public Coord jsonToCoord(JSONObject coordJson) {
        return new Coord((double) coordJson.get("x"), (double) coordJson.get("y"));
    }
}
