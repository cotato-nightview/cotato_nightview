package com.cotato.nightview.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class JsonUtil {

    public String readFileAsString(String file) {
        ClassPathResource resource = new ClassPathResource(file);
        JSONParser parser = new JSONParser();

        InputStream inputStream;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        JSONObject parseResult;
        try {
            parseResult = (JSONObject) parser.parse(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return parseResult.toString();
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
