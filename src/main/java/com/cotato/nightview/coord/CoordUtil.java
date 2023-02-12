package com.cotato.nightview.coord;

import com.cotato.nightview.api.KakaoExteranlApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoordUtil {
    private final KakaoExteranlApi kakaoApi;

    public Coord ktmToWGS(double mapx, double mapy) {
        // documents를 json 객체로 변환
        JSONObject jsonObjectCoord = kakaoApi.transCoord(mapx, mapy);

        // coord 객체러 변환 후 리턴
        return jsonToCoord(jsonObjectCoord);
    }


    public Coord jsonToCoord(JSONObject coordJson) {
        return new Coord((double) coordJson.get("x"), (double) coordJson.get("y"));
    }
}
