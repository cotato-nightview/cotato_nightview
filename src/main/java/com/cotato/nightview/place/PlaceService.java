package com.cotato.nightview.place;

import com.cotato.nightview.api.NaverExteranlApi;
import com.cotato.nightview.comment.CommentRepository;
import com.cotato.nightview.dong.Dong;
import com.cotato.nightview.enums.ExceptionMessageEnum;
import com.cotato.nightview.enums.PlaceEnum;
import com.cotato.nightview.enums.ValidLatitudeEnum;
import com.cotato.nightview.enums.ValidLongitudeEnum;
import com.cotato.nightview.gu.Gu;
import com.cotato.nightview.like_place.LikePlaceRepository;
import com.cotato.nightview.member.Member;
import com.cotato.nightview.validation.ValidateService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

            // API?????? ???????????? ????????? JSONArray??? ??????
            JSONArray placesFromApi = naverApi.getPlaces(dongName + " " + PlaceEnum.NIGHT_SCAPE.getKeyword());

            // JSONArray??? dto ????????? ??????
            PlaceDto placeDtos[] = placeUtil.itemsToDto(placesFromApi);

            // DB??? ??????
            for (PlaceDto placeDto : placeDtos) {
                savePlace(placeDto);
            }
        }
    }

    public String makeParamsString(String keyword) {
        double latitude;
        double longitude;
        if (keyword.endsWith("???")) {
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
        // ??????????????? ???????????? ??????
        if (!(dto.getCategory().contains(PlaceEnum.TOURIST_ATTRACTION.getKeyword()) || dto.getCategory().contains(PlaceEnum.PLACE_NAME.getKeyword())))
            return false;

        // ?????? ?????? ????????? ??????
        if (!(dto.getAddress().contains(PlaceEnum.SEOUL.getKeyword()))) return false;

        // ?????? ??????
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
