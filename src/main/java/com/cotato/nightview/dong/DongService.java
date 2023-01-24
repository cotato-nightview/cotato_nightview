package com.cotato.nightview.dong;

import com.cotato.nightview.gu.Gu;
import com.cotato.nightview.gu.GuDto;
import com.cotato.nightview.gu.GuService;
import com.cotato.nightview.json.JsonService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DongService {
    private final JsonService jsonService;
    private final GuService guService;
    private final DongRepository dongRepository;

    public void initDong() {
        String areaInfoJson = jsonService.readFileAsString("dong_coords.json");
        JSONArray areaInfoArray = jsonService.parseJsonArray(areaInfoJson, "areaInfo");

        for (Object areaObj : areaInfoArray) {

            JSONObject areaObjJson = (JSONObject) areaObj;
            String dongName = areaObjJson.get("dong").toString();
            String guName = areaObjJson.get("gu").toString();

            if (dongRepository.findByName(dongName) == null) {
                Gu gu = guService.findByName(guName);

                DongDto dto = DongDto.builder()
                        .name(dongName)
                        .build();

                dongRepository.save(dto.toEntity(gu));
            }
        }
    }

    public Dong getDongFromAddress(String address) {
        String[] addressSplit = address.split(" ");
        return dongRepository.findByName(addressSplit[2]);
    }
}
