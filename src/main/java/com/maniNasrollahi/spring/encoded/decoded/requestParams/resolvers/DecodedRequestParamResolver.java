package com.maniNasrollahi.spring.encoded.decoded.requestParams.resolvers;

import com.maniNasrollahi.spring.encoded.decoded.requestParams.annotation.DecodedRequestParam;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.crypto.ApiEncoder;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class DecodedRequestParamResolver implements HandlerMethodArgumentResolver {

    private final ConversionService conversionService = new DefaultConversionService();
    private final ApiEncoder apiEncoder;

    public DecodedRequestParamResolver(ApiEncoder apiEncoder) {
        this.apiEncoder = apiEncoder;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DecodedRequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        DecodedRequestParam annotation = parameter.getParameterAnnotation(DecodedRequestParam.class);
        assert annotation != null;
        String name = annotation.value();
        name = name != null && name.isEmpty() ? name : parameter.getParameterName();

        assert name != null;
        String encodedValue = webRequest.getParameter(name);
        if (encodedValue == null) {
            return null;
        }
        Class<?> targetType = parameter.getParameterType();
        return conversionService.convert(apiEncoder.decode(encodedValue), targetType);

    }
}
