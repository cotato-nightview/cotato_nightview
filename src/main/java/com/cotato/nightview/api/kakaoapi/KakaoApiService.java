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
