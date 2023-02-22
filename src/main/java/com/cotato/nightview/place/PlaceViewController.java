package com.cotato.nightview.place;

import com.cotato.nightview.dong.Dong;
import com.cotato.nightview.dong.DongService;
import com.cotato.nightview.gu.Gu;
import com.cotato.nightview.gu.GuService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/place")
@Validated
public class PlaceViewController {
    private final PlaceService placeService;
    private final GuService guService;
    private final PlaceUtil placeUtil;
    private final DongService dongService;

    @GetMapping(path = "/map", params = "keyword")
    public String showMapByGuName(@RequestParam("keyword") @Pattern(regexp = "^[가-힣|0-9]*[동구가]$", message = "'구','동','가'로 끝나야합니다.") String keyword) {
        return "redirect:/place/map" + placeService.makeParamsString(keyword);
    }

    @GetMapping(path = "/map", params = {"longitude", "latitude", "distance-within"})
    public String showMapByCoord(@RequestParam("longitude") double longitude, @RequestParam("latitude") double latitude,
                                 @RequestParam("distance-within") double distanceWithin, Model model) {
        // 일정 거리 안에 있는 장소를 가져옴
//        List<Place> placeEntityList = placeService.findAllWtihInDistance(longitude, latitude, distanceWithin);
        // javascript 변수로 사용하기 위해 연관 관계가 없는 dto 객체로 변경
        List<PlaceDto> placeDtoList = placeService.findAllWtihInDistance(longitude,latitude,distanceWithin);

        model.addAttribute("placeDtoList", placeDtoList);

        return "map/map";
    }

    // 지원하지 않는 위치일 경우, 구 이름이 유효성 검사를 통과했으나 없는 구인 경우
    @ExceptionHandler({IllegalArgumentException.class, EntityNotFoundException.class})
    public String invalidLocation(Exception e, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public String invalidGuName(ConstraintViolationException e, RedirectAttributes redirectAttributes,HttpServletRequest request) {
        List<String> errorMessages = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        redirectAttributes.addFlashAttribute("valid_message", errorMessages.get(0));
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}

