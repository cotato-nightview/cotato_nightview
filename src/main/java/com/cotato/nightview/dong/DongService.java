package com.cotato.nightview.dong;

import com.cotato.nightview.gu.Gu;
import com.cotato.nightview.gu.GuService;
import com.cotato.nightview.json.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

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
            String dongName = null;
            try {
                dongName = areaObjJson.get("dong").toString();
            } catch (Exception e) {
                continue;
            }

            String guName = areaObjJson.get("gu").toString();
            double latitude = (double) areaObjJson.get("lat");
            double longitude = (double) areaObjJson.get("lng");

            Gu gu = guService.findByName(guName);

            if (dongRepository.existsByNameAndGu(dongName, gu)) {
                System.out.println(dongName + "은 이미 DB에 존재하는 동입니다!");
                continue;
            }

            DongDto dto = DongDto.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .name(dongName)
                    .build();
            dongRepository.save(dto.toEntity(gu));

        }
    }

    public Dong findByAddress(String address) {
        String[] addressSplit = address.split(" ");
        return findByName(addressSplit[2]);
    }

    public Dong findByName(String dongName){
        return dongRepository.findByName(dongName)
                .orElseThrow(()->new EntityNotFoundException("존재하지 않는 지역 이름입니다!"));
    }

    public List<Dong> findAll() {
        return dongRepository.findAll();
    }
}
