package com.cotato.nightview.place;

import com.cotato.nightview.dong.Dong;
import com.cotato.nightview.dong.DongService;
import com.cotato.nightview.exception.ExceptionMessage;
import com.cotato.nightview.exception.InvalidLocationException;
import com.cotato.nightview.gu.Gu;
import com.cotato.nightview.gu.GuService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
@PropertySource("classpath:config.properties")
public class PlaceViewController {
    private final PlaceService placeService;
    private final GuService guService;
    private final PlaceUtil placeUtil;
    private final DongService dongService;

    @GetMapping(path = "/map", params = "keyword")
    public String showMapByGuName(@RequestParam("keyword") String keyword, Model model) {
        //gu id를 가져옴 -> gu id인 동을 가져옴
        Gu gu = guService.findByName(keyword);
        List<Dong> dongList = dongService.findAllByGu(gu);

        // 해당 구에 있는 장소들을 가져옴
        List<Place> placeEntityList = placeService.findAllByDongIn(dongList);
        // javascript 변수로 사용하기 위해 연관 관계가 없는 dto 객체로 변경
        List<PlaceDto> placeDtoList = placeUtil.entitiesToDtos(placeEntityList);


        // 디폴트 좌표값 설정을 위한 장소
        PlaceDto defaultPlace = placeDtoList.get(0);
        model.addAttribute("defaultPlace", defaultPlace);

        model.addAttribute("placeDtoList", placeDtoList);
        return "map/map";
    }

    @GetMapping(path = "/map", params = {"longitude", "latitude", "distance-within"})
    public String showMapByCoord(@RequestParam("longitude") double longitude, @RequestParam("latitude") double latitude,
                                 @RequestParam("distance-within") double distanceWithIn, Model model) {
        // 일정 거리 안에 있는 장소를 가져옴
        List<Place> placeEntityList = placeService.findAllWtihInDistance(longitude, latitude, distanceWithIn);
        // javascript 변수로 사용하기 위해 연관 관계가 없는 dto 객체로 변경
        List<PlaceDto> placeDtoList = placeUtil.entitiesToDtos(placeEntityList);

        model.addAttribute("placeDtoList", placeDtoList);

        PlaceDto defaultPlace = placeDtoList.get(0);
        model.addAttribute("defaultPlace", defaultPlace);
        return "map/map";
    }

    // 지원하지 않는 위치일 경우
    @ExceptionHandler({InvalidLocationException.class})
    public String invalidLocation(Model model, InvalidLocationException e) {
        model.addAttribute("data",
                new ExceptionMessage(e.getErrorMessage(), "/"));
        return "exception/message";
    }
}

