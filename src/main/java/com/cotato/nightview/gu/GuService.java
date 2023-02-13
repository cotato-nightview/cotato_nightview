package com.cotato.nightview.gu;

import com.cotato.nightview.json.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuService {
    private final JsonUtil jsonUtil;
    private final GuRepository guRepository;

    public void initGu() {
        String areaInfoJson = jsonUtil.readFileAsString("dong_coords.json");
        JSONArray areaInfoArray = jsonUtil.parseJsonArray(areaInfoJson, "areaInfo");
        for (Object areaObj : areaInfoArray) {
            JSONObject areaObjJson = (JSONObject) areaObj;
            String guName = areaObjJson.get("gu").toString();

            // 구 초기화 중복 방지
            if (guRepository.existsByName(guName)) {
                System.out.println(guName +"은 이미 DB에 존재하는 지역입니다!");
                throw new RuntimeException("이미 DB에 존재하는 지역입니다!");
            }

            GuDto guDto = GuDto.builder()
                    .name(guName)
                    .build();
            guRepository.save(guDto.toEntity());

        }
    }

    public Gu findByName(String guName) {
        if (!guRepository.existsByName(guName)) {
            throw new RuntimeException("없는 지역 이름입니다!");
        }
        return guRepository.findByName(guName);
    }
}
