package com.cotato.nightview.naverapi;

import com.cotato.nightview.kakaoapi.KakaoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NaverApiController {
    private final NaverApiService naverApiService;
    private final KakaoApiService kakaoApiService;
    @GetMapping("/naver")
    public String naverApi() {
        naverApiService.getPlacesFromApi();
        return "";
    }
}
