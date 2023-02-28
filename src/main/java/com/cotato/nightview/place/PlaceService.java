package com.cotato.nightview.place;

import com.cotato.nightview.api.NaverExteranlApi;
import com.cotato.nightview.comment.CommentRepository;
import com.cotato.nightview.dong.Dong;
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
    private final LikePlaceRepository likePlaceRepository;
    private final CommentRepository commentRepository;

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
            JSONArray placesFromApi = naverApi.getPlaces(dongName + " 야경");

            // JSONArray를 dto 배열로 변환
            PlaceDto placeDtos[] = placeUtil.itemsToDto(placesFromApi);

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
        if (!(dto.getCategory().contains("명소") || dto.getCategory().contains("지명"))) return false;

        // 서울 내에 있는지 검사
        if (!(dto.getAddress().contains("서울"))) return false;

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

            setLiked(placeDto, place);
            setNumberOfLike(placeDto, place);
            setNumberOfComment(placeDto, place);

            placeDtoList.add(placeDto);
        });
        return placeDtoList;
    }

    public void setNumberOfLike(PlaceDto placeDto, Place place) {
        Long numberOfLike = likePlaceRepository.countByPlace(place);
        placeDto.setNumberOfLike(numberOfLike);
    }

    public void setLiked(PlaceDto placeDto, Place place) {
        Optional<String> authNameWrapper = validateService.getAuthUsername();
        if (authNameWrapper.isPresent()) {
            Member member = validateService.findMemberByUsername(authNameWrapper.get());
            boolean isLiked = likePlaceRepository.existsByMemberAndPlace(member, place);
            placeDto.setLiked(isLiked);
        }
    }

    public void setNumberOfComment(PlaceDto placeDto, Place place) {
        Long numberOfComment = commentRepository.countByPlace(place);
        placeDto.setNumberOfComment(numberOfComment);
    }

    private void validateLocation(double longitude, double latitude) {
        if (latitude < 38.61 && latitude > 33.11)
            if (longitude < 131.87 && longitude > 124.6) {
                return;
            }
        throw new IllegalArgumentException("지원하지 않는 위치입니다.");
    }

}
