package com.cotato.nightview.place;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/init-place")
    public String initPlace() {
        placeService.initPlace();
        return "place initialize success!";
    }

    @GetMapping("/insert")
    public String insertPlace(@RequestParam String name) {
        return placeService.insertPlace(name);
    }
}