package com.cotato.nightview.coord;

import com.cotato.nightview.api.kakaoapi.KakaoApiService;
import com.cotato.nightview.json.JsonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        URI uri = kakaoApiService.buildTransCoordUri(mapx, mapy);

        // URI로 요청 엔티티 생성
        RequestEntity<Void> requestEntity = kakaoApiService.buildRequestEntity(uri);

        // API 호출 후 응답을 String 형식 Json으로 받음
        ResponseEntity<String> res = kakaoApiService.callApi(requestEntity);

        // API 응답 중 실제 좌표 정보인 "documents"만 파싱
        JSONArray jsonArray = jsonService.parseJsonArray(res.getBody(), "documents");

        // documents를 json 객체로 변환
        JSONObject coordJson = (JSONObject) jsonArray.get(0);

        // java 객체로 변환 후 return
        return jsonToCoord(coordJson);
    }

    public String coordToGu(double longitude, double latitude) throws IndexOutOfBoundsException {
        URI uri = kakaoApiService.buildCoordToAddressUri(longitude, latitude);
        RequestEntity<Void> requestEntity = kakaoApiService.buildRequestEntity(uri);

        // API 호출 후 응답을 String 형식 Json으로 받음
        ResponseEntity<String> res = kakaoApiService.callApi(requestEntity);
        // API 응답 중 실제 좌표 정보인 "documents"만 파싱
        JSONArray jsonArray = jsonService.parseJsonArray(res.getBody(), "documents");

        // documents를 json 객체로 변환
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

        // 주소 정보가 담긴 address 객체를 파싱
        JSONObject address = (JSONObject) jsonObject.get("address");

        // 구 이름을 파싱
        String guName = address.get("region_2depth_name").toString();

        return guName;

    }

    public Coord jsonToCoord(JSONObject coordJson) {
        return new Coord((double) coordJson.get("x"), (double) coordJson.get("y"));
    }
}
