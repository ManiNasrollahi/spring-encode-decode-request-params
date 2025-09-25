package com.maniNasrollahi.spring.encoded.decoded.requestParams.controller;

import com.maniNasrollahi.spring.encoded.decoded.requestParams.annotation.DecodedPathVariable;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.annotation.DecodedRequestBody;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.annotation.DecodedRequestParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/param")
    public String getParam(@DecodedRequestParam("id") String id) {
        return "Decoded param: " + id;
    }

    @GetMapping("/path/{userId}")
    public String getPath(@DecodedPathVariable("userId") Long userId) {
        return "Decoded path variable: " + userId;
    }

    @PostMapping("/body")
    public String postBody(@DecodedRequestBody User user) {
        return "Decoded body: " + user.getName() + ", age: " + user.getAge();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {

        private String name;
        private int age;

    }
}
