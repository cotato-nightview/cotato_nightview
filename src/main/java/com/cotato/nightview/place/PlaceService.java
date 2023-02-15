package com.cotato.nightview.place;

import com.cotato.nightview.api.NaverExteranlApi;
import com.cotato.nightview.dong.Dong;
import com.cotato.nightview.dong.DongService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final NaverExteranlApi naverApi;
    private final DongService dongService;
    private final PlaceUtil placeUtil;

    public String insertPlace(String name) {

        JSONArray placesFromApi = naverApi.getPlaces(name);

        PlaceDto[] placeDtos = placeUtil.itemsToDto(placesFromApi);

        PlaceDto dto = null;

        try {
            dto = placeUtil.removeHtmlTags(placeDtos[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return "no search result";
        }

        savePlace(dto);
        return dto.toString();
    }

    public void initPlace() {

        List<Dong> dongList = dongService.findAll();
        int i = 0;

        for (Dong dong : dongList) {
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            String dongName = dong.getName();

            System.out.println(dongName + "    " + ++i + "/" + dongList.size());

            // API에서 장소정보 불러와 JSONArray에 저장
            JSONArray placesFromApi = naverApi.getPlaces(dongName + " 야경");

            // JSONArray를 dto 배열로 변환
            PlaceDto placeDtos[] = placeUtil.itemsToDto(placesFromApi);

            // DB에 저장
            for (PlaceDto placeDto : placeDtos) {
                savePlace(placeDto);
            }
        }
    }


    private boolean isValidPlace(PlaceDto dto) {
        // 카테고리가 적절한지 검사
        if (!(dto.getCategory().contains("명소") || dto.getCategory().contains("지명"))) return false;

        // 서울 내에 있는지 검사
        if (!(dto.getAddress().contains("서울"))) return false;

        // 중복 검사
        if (placeRepository.existsByTitle(dto.getTitle())) return false;

        return true;
    }

    @Transactional
    public void savePlace(PlaceDto dto) {
        if (isValidPlace(dto)) {
            placeUtil.transCoord(dto);
            Dong dong = dongService.findByAddress(dto.getAddress());
            placeRepository.save(dto.toEntity(dong));
        }
    }

    public List<Place> findAllByDongIn(List<Dong> dongList) {
        return placeRepository.findAllByDongIn(dongList);
    }

    public List<Place> findAllWtihInDistance(double longitude, double latitude, double distanceWithIn) {
        validateLocation(longitude, latitude);
        return placeRepository.findAllWtihInDistance(longitude, latitude, distanceWithIn);
    }

    private void validateLocation(double longitude, double latitude) {
        if (latitude < 38.61 && latitude > 33.11)
            if (longitude < 131.87 && longitude > 124.6) {
                return;
            }

        throw new IllegalArgumentException("지원하지 않는 위치입니다.");
    }

}
