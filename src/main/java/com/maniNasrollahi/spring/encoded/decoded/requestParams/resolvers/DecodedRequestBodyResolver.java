package com.maniNasrollahi.spring.encoded.decoded.requestParams.resolvers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.annotation.DecodedRequestBody;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.crypto.ApiEncoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class DecodedRequestBodyResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;
    private final ApiEncoder apiEncoder;

    public DecodedRequestBodyResolver(ObjectMapper objectMapper, ApiEncoder apiEncoder) {
        this.objectMapper = objectMapper;
        this.apiEncoder = apiEncoder;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DecodedRequestBody.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        assert request != null;
        String body = request.getReader().lines().collect(Collectors.joining());
        InputStream inputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        String encodedJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        try {
            return objectMapper.readValue(apiEncoder.decode(encodedJson),  parameter.getParameterType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
