package com.cotato.nightview.naverapi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class NaverApiController {

    @GetMapping("/naver")
    public String naverApi(@RequestParam String search) {

        // URI 생성 코드
        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query", search)
                .queryParam("display", "5")
                .queryParam("sort", "random")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더에 키 값들 대입
        RequestEntity<Void> requestEntity =
                RequestEntity.get(uri)
                        .header("X-Naver-Client-ID", "rs3jbPtyp96zx_0CSktZ")
                        .header("X-Naver-Client-Secret", "QtBYuGAEHT")
                        .build();

        // API를 호출해 String 형식으로 받음
        ResponseEntity<String> res = restTemplate.exchange(requestEntity, String.class);

        // JSON 파싱을 위한 parser 생성
        JSONParser parser = new JSONParser();

        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(res.getBody());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 응답 Body에서 장소 정보들만 JSONArray로 바꿈
        JSONArray items = (JSONArray) jsonObject.get("items");

        // 순회하며 장소 이름 출력
        for(Object o : items){
            JSONObject item = (JSONObject) o;
            System.out.println("item.get(\"title\") = " + item.get("title"));
        }

        return res.getBody();
    }
}
