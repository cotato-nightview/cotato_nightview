package com.cotato.nightview.place;

import com.cotato.nightview.coord.CoordService;
import com.cotato.nightview.dong.Dong;
import com.cotato.nightview.dong.DongService;
import com.cotato.nightview.exception.ExceptionMessage;
import com.cotato.nightview.gu.Gu;
import com.cotato.nightview.gu.GuService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
@PropertySource("classpath:config.properties")
public class PlaceViewController {
    private final PlaceService placeService;
    private final ModelMapper modelMapper;
    private final CoordService coordService;
    private final GuService guService;
    private final DongService dongService;

    @GetMapping("/map")
    public String map(@RequestParam("gu-name") String guName, Model model) {
        //gu id를 가져옴 -> gu id인 동을 가져옴
        Gu gu = guService.findByName(guName);
        List<Dong> dongList = dongService.findAllByGu(gu);


        // Entity List를 Dto List로 변경
        List<Place> placeEntityList = placeService.findAllByDongIn(dongList);
        List<PlaceDto> placeDtoList = placeEntityList
                .stream()
                .map(place -> modelMapper.map(place, PlaceDto.class))
                .collect(Collectors.toList());

        model.addAttribute("placeDtoList", placeDtoList);

        // 디폴트 좌표값 설정을 위한 장소
        Place defaultPlace = placeService.findByTitle("경복궁");
        model.addAttribute("defaultPlace", defaultPlace);
        return "map/map";
    }

    @GetMapping("/find")
    public String transCoordToGu(@RequestParam double longitude, @RequestParam double latitude,
                                 RedirectAttributes redirectAttributes) {
        System.out.println("longitude = " + longitude);
        System.out.println("latitude = " + latitude);
        String guName = coordService.coordToGu(longitude, latitude);
        redirectAttributes.addAttribute("gu-name", guName);
        return "redirect:/place/map";
    }

    // 지원하지 않는 위치일 경우
    @ExceptionHandler({IndexOutOfBoundsException.class})
    public String invaildCoord(Model model) {
        model.addAttribute("data",
                new ExceptionMessage("지원하지 않는 위치입니다!", "/"));
        return "exception/message";
    }
}

