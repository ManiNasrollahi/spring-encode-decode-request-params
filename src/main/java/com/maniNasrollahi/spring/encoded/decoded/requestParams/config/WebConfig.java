package com.maniNasrollahi.spring.encoded.decoded.requestParams.config;

import com.maniNasrollahi.spring.encoded.decoded.requestParams.resolvers.DecodedPathVariableResolver;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.resolvers.DecodedRequestBodyResolver;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.resolvers.DecodedRequestParamResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final DecodedPathVariableResolver decodedPathVariable;
    private final DecodedRequestParamResolver decodedRequestParam;
    private final DecodedRequestBodyResolver decodedRequestBodyResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(decodedPathVariable);
        resolvers.add(decodedRequestParam);
        resolvers.add(decodedRequestBodyResolver);
    }

}
