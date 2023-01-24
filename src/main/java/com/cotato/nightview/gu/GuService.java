package com.cotato.nightview.gu;

import com.cotato.nightview.json.JsonService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuService {
    private final JsonService jsonService;
    private final GuRepository guRepository;

    public void initGu() {
        String areaInfoJson = jsonService.readFileAsString("dong_coords.json");
        JSONArray areaInfoArray = jsonService.parseJsonArray(areaInfoJson, "areaInfo");
        for (Object areaObj : areaInfoArray) {

            JSONObject areaObjJson = (JSONObject) areaObj;
            String guName = areaObjJson.get("gu").toString();

            if (guRepository.findByName(guName) == null) {
                GuDto guDto = new GuDto(guName);
                guRepository.save(guDto.toEntity());
            }
        }
    }
}