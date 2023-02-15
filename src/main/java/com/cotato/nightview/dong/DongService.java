package com.cotato.nightview.dong;

import com.cotato.nightview.gu.Gu;
import com.cotato.nightview.gu.GuService;
import com.cotato.nightview.json.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DongService {
    private final JsonUtil jsonUtil;
    private final GuService guService;
    private final DongRepository dongRepository;

    public void initDong() {
        String areaInfoJson = jsonUtil.readFileAsString("json/dong_coords.json");
        JSONArray areaInfoArray = jsonUtil.parseJsonArray(areaInfoJson, "areaInfo");

        for (Object areaObj : areaInfoArray) {

            JSONObject areaObjJson = (JSONObject) areaObj;
            String dongName = areaObjJson.get("dong").toString();
            String guName = areaObjJson.get("gu").toString();
            Gu gu = guService.findByName(guName);
            if (dongRepository.existsByNameAndGu(dongName, gu)) {
                System.out.println(dongName + "은 이미 DB에 존재하는 동입니다!");
                continue;
            }

            DongDto dto = DongDto.builder()
                    .name(dongName)
                    .build();
            dongRepository.save(dto.toEntity(gu));

        }
    }

    public Dong findByAddress(String address) {
        String[] addressSplit = address.split(" ");
        return dongRepository.findByName(addressSplit[2]);
    }

    public List<Dong> findAllByGu(Gu gu) {
        return dongRepository.findAllByGu(gu);
    }

    public List<Dong> findAll() {
        return dongRepository.findAll();
    }
}
