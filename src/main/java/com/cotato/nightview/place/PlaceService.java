package com.cotato.nightview.place;

import com.cotato.nightview.api.NaverExteranlApi;
import com.cotato.nightview.dong.Dong;
import com.cotato.nightview.enums.ExceptionMessageEnum;
import com.cotato.nightview.enums.PlaceEnum;
import com.cotato.nightview.enums.ValidLatitudeEnum;
import com.cotato.nightview.enums.ValidLongitudeEnum;
import com.cotato.nightview.gu.Gu;
import com.cotato.nightview.validation.ValidateService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final NaverExteranlApi naverApi;
    private final ValidateService validateService;
    private final PlaceUtil placeUtil;
    private final ModelMapper modelMapper;

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
        List<Dong> dongList = validateService.findAllDong();
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
            JSONArray placesFromApi = naverApi.getPlaces(dongName + " " + PlaceEnum.NIGHT_SCAPE.getKeyword());

            // JSONArray를 dto 배열로 변환
            PlaceDto placeDtos[] = placeUtil.itemsToDto(placesFromApi);

            for(PlaceDto placeDto : placeDtos){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (isValidPlace(placeDto)) {
                    String imageUrl = naverApi.getImageUrl(placeDto.getTitle());
                    placeDto.setImageUrl(imageUrl);
                    System.out.println("placeDto = " + placeDto);
                }
            }
            // DB에 저장
            for (PlaceDto placeDto : placeDtos) {
                savePlace(placeDto);
            }
        }
    }

    public String makeParamsString(String keyword) {
        double latitude;
        double longitude;
        if (keyword.endsWith("구")) {
            Gu gu = validateService.findGuByName(keyword);
            latitude = gu.getLatitude();
            longitude = gu.getLongitude();
        } else {
            Dong dong = validateService.findDongByName(keyword);
            latitude = dong.getLatitude();
            longitude = dong.getLongitude();
        }
        return "?latitude=" + latitude +
                "&longitude=" + longitude +
                "&distance-within=5";
    }

    private boolean isValidPlace(PlaceDto dto) {
        // 카테고리가 적절한지 검사
        if (!(dto.getCategory().contains(PlaceEnum.TOURIST_ATTRACTION.getKeyword()) || dto.getCategory().contains(PlaceEnum.PLACE_NAME.getKeyword()))) return false;
        // 서울 내에 있는지 검사
        if (!(dto.getAddress().contains(PlaceEnum.SEOUL.getKeyword()))) return false;
        // 중복 검사
        if (placeRepository.existsByTitle(dto.getTitle())) return false;
        return true;
    }

    public void savePlace(PlaceDto dto) {
        if (isValidPlace(dto)) {
            placeUtil.transCoord(dto);
            Dong dong = validateService.findDongByAddress(dto.getAddress());
            placeRepository.save(dto.toEntity(dong));
        }
    }

    public List<PlaceDto> findAllWtihInDistance(double longitude, double latitude, double distanceWithIn) {
        validateLocation(longitude, latitude);
        List<Place> placeEntityList = placeRepository.findAllWtihInDistance(longitude, latitude, distanceWithIn);
        return entitiesToDtos(placeEntityList);
    }

    public List<PlaceDto> entitiesToDtos(List<Place> placeEntityList) {
        ArrayList<PlaceDto> placeDtoList = new ArrayList<>();
        placeEntityList.forEach(place -> {
            PlaceDto placeDto = modelMapper.map(place, PlaceDto.class);

            placeUtil.setLiked(placeDto, place);
            placeUtil.setNumberOfLike(placeDto, place);
            placeUtil.setNumberOfComment(placeDto, place);

            placeDtoList.add(placeDto);
        });
        return placeDtoList;
    }

    private void validateLocation(double longitude, double latitude) {
        if (latitude < ValidLatitudeEnum.UPPER_LIMIT.getLatitude() && latitude > ValidLatitudeEnum.LOWER_LIMIT.getLatitude())
            if (longitude < ValidLongitudeEnum.UPPER_LIMIT.getLongitude() && longitude > ValidLongitudeEnum.LOWER_LIMIT.getLongitude()) {
                return;
            }
        throw new IllegalArgumentException(ExceptionMessageEnum.NOT_SUPPORTED_LOCATION.getMessage());
    }

}
