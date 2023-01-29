package com.cotato.nightview.place;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.lang.model.SourceVersion;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
@PropertySource("classpath:config.properties")
public class PlaceViewController {
    private final PlaceService placeService;
    private final ModelMapper modelMapper;

    //    default map
    @GetMapping("/map")
    public String mapDefault(Model model) {
        Place defaultPlace = placeService.findByTitle("경복궁");
        model.addAttribute("defaultPlace", defaultPlace);

        List<Place> placeList = placeService.findAll();
        model.addAttribute("placeList", placeList);

        TypeMap<Place, PlaceDto> typeMap = modelMapper.createTypeMap(Place.class, PlaceDto.class);
        typeMap.addMappings(mapper -> {
            mapper.map(Place::getLatitude, PlaceDto::setMapy);
            mapper.map(Place::getLongitude, PlaceDto::setMapx);
        });
//        modelMapper.createTypeMap(Place.class,PlaceDto.class)
//                .addMapping(Place::getLongitude,PlaceDto::setMapy)
//                .addMapping(Place::getLatitude,PlaceDto::setMapy);
        PlaceDto map = modelMapper.map(defaultPlace, PlaceDto.class);
        System.out.println(map.toString());
        return "map/map";
    }
}
