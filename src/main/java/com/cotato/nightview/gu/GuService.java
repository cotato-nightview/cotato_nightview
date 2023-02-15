package com.cotato.nightview.gu;

import com.cotato.nightview.json.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class GuService {
    private final JsonUtil jsonUtil;
    private final GuRepository guRepository;

    @Transactional
    public void initGu() {
        String areaInfoJson = jsonUtil.readFileAsString("json/dong_coords.json");
        JSONArray areaInfoArray = jsonUtil.parseJsonArray(areaInfoJson, "areaInfo");
        for (Object areaObj : areaInfoArray) {
            JSONObject areaObjJson = (JSONObject) areaObj;
            String guName = areaObjJson.get("gu").toString();
            System.out.println(guName);
            // 구 초기화 중복 방지
            if (guRepository.existsByName(guName)) {
                continue;
            }

            GuDto guDto = GuDto.builder()
                    .name(guName)
                    .build();
            guRepository.save(guDto.toEntity());

        }
    }

    public Gu findByName(String guName) {
        return guRepository.findByName(guName)
                .orElseThrow(()->new EntityNotFoundException("없는 지역이름입니다!"));
    }
}
