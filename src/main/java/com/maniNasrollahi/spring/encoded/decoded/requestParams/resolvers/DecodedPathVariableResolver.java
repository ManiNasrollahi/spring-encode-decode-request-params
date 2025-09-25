package com.maniNasrollahi.spring.encoded.decoded.requestParams.resolvers;

import com.maniNasrollahi.spring.encoded.decoded.requestParams.annotation.DecodedPathVariable;
import com.maniNasrollahi.spring.encoded.decoded.requestParams.crypto.ApiEncoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class DecodedPathVariableResolver implements HandlerMethodArgumentResolver {

    private final ConversionService conversionService = new DefaultConversionService();
    private final ApiEncoder apiEncoder;

    public DecodedPathVariableResolver(ApiEncoder apiEncoder) {
        this.apiEncoder = apiEncoder;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DecodedPathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        DecodedPathVariable annotation = parameter.getParameterAnnotation(DecodedPathVariable.class);
        assert annotation != null;
        String name = annotation.value();
        name = name != null && name.isEmpty() ? name : parameter.getParameterName();

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        assert request != null;
        @SuppressWarnings("unchecked")
        Map<String, String> uriTemplateVars =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);


        String encodedValue = uriTemplateVars.get(name);
        if (encodedValue == null) {
            throw new IllegalArgumentException("Missing path variable: " + name);
        }
        Class<?> targetType = parameter.getParameterType();
        return conversionService.convert(apiEncoder.decode(encodedValue), targetType);

    }
}
