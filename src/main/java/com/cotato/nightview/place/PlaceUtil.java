package com.cotato.nightview.place;

import com.cotato.nightview.coord.Coord;
import com.cotato.nightview.coord.CoordUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlaceUtil {
    private final ModelMapper modelMapper;
    private final CoordUtil coordUtil;
    public List<PlaceDto> entitiesToDtos(List<Place> placeEntityList) {
        return placeEntityList.stream().map(place -> modelMapper.map(place, PlaceDto.class))
                .collect(Collectors.toList());
    }

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


}
