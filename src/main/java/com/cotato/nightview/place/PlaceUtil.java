package com.cotato.nightview.place;

import com.cotato.nightview.comment.CommentRepository;
import com.cotato.nightview.coord.Coord;
import com.cotato.nightview.coord.CoordUtil;
import com.cotato.nightview.like_place.LikePlaceRepository;
import com.cotato.nightview.member.Member;
import com.cotato.nightview.validation.ValidateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlaceUtil {
    private final CoordUtil coordUtil;
    private final LikePlaceRepository likePlaceRepository;
    private final CommentRepository commentRepository;
    private final ValidateService validateService;


    public PlaceDto removeHtmlTags(PlaceDto dto) {
        dto.setTitle(dto.getTitle().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", ""));
        return dto;
    }

    public PlaceDto[] itemsToDto(JSONArray items) {
        ObjectMapper objectMapper = new ObjectMapper();

        //선언한 필드만 매핑
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String jsonSting;
        try {
            jsonSting = objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            return objectMapper.readValue(jsonSting, PlaceDto[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void transCoord(PlaceDto dto) {
        Coord coord = coordUtil.ktmToWGS(dto.getLongitude(), dto.getLatitude());
        dto.setLongitude(coord.getX());
        dto.setLatitude(coord.getY());
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




}
