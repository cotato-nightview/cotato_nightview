package com.cotato.nightview.gu;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gu")
@RequiredArgsConstructor
public class GuController {
    private final GuService guService;
    @GetMapping("/init-gu")
    public String initGu() {
        guService.initGu();
        return "gu initialize success!";
    }
}
