package com.cotato.nightview.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class JsonService {
    public String readFileAsString(String file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // jsonString = json 객체를 문자열로 받은 값
    // arrayName = 배열 이름
    public JSONArray parseJsonArray(String jsonString, String arrayName) {
        // JSON 파싱을 위한 parser 생성
        JSONParser parser = new JSONParser();

        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 응답 Body에서 장소 정보들만 JSONArray로 바꿈
        return (JSONArray) jsonObject.get(arrayName);
    }
}
