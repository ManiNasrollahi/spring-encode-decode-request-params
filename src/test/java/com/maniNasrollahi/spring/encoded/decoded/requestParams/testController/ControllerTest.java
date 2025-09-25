package com.maniNasrollahi.spring.encoded.decoded.requestParams.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.controller.TestController;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.crypto.ApiEncoder;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.resolvers.DecodedPathVariableResolver;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.resolvers.DecodedRequestBodyResolver;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.resolvers.DecodedRequestParamResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TestController.class)
@Import({ApiEncoder.class,
        DecodedRequestParamResolver.class,
        DecodedPathVariableResolver.class,
        DecodedRequestBodyResolver.class})
public class ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApiEncoder apiEncoder;

    @Test
    void testDecodedRequestParam() throws Exception {
        String encodedId = apiEncoder.encode("12345");

        mockMvc.perform(get("/api/test/param")
                        .param("id", encodedId))
                .andExpect(status().isOk())
                .andExpect(content().string("Decoded param: 12345"));
    }

    @Test
    void testDecodedPathVariable() throws Exception {
        String encodedUserId = apiEncoder.encode("42");

        mockMvc.perform(get("/api/test/path/{userId}", encodedUserId))
                .andExpect(status().isOk())
                .andExpect(content().string("Decoded path variable: 42"));
    }

    @Test
    void testDecodedRequestBody() throws Exception {
        TestController.User user = new TestController.User("alice", 25);
        ObjectMapper objectMapper = new ObjectMapper();
        String encodedUser = objectMapper.writeValueAsString(user);
        String encodedJson = apiEncoder.encode(encodedUser);

        mockMvc.perform(post("/api/test/body")
                        .content(encodedJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Decoded body: alice, age: 25"));
    }
}
