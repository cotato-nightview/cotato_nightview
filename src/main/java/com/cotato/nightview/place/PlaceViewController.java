package com.cotato.nightview.place;

import com.cotato.nightview.coord.Coord;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.lang.model.SourceVersion;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        // 디폴트 좌표값 설정을 위한 장소
        Place defaultPlace = placeService.findByTitle("경복궁");
        model.addAttribute("defaultPlace", defaultPlace);

        // Entity List를 Dto List로 변경
        List<Place> placeEntityList = placeService.findAll();
        List<PlaceDto> placeDtoList = placeEntityList
                .stream()
                .map(place -> modelMapper.map(place, PlaceDto.class))
                .collect(Collectors.toList());

        model.addAttribute("placeDtoList", placeDtoList);
        return "map/map";
    }

    @PostMapping("/find")
    public String findPlaceByCoord(@ModelAttribute Coor coor) {
        System.out.println("coord = " + coor.getX().getClass().getName());
        return "redirect:/place/map";
    }
}

