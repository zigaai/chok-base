package com.zigaai.oauth2.handler;

import com.zigaai.core.model.common.Res;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.warn("OAuth2 认证错误: ", e);
        if (e instanceof AuthenticationCredentialsNotFoundException) {
            jackson2HttpMessageConverter.write(Res.unauthorized("用户未登录或登录已过期, 请重新登录(OAuth2)"), MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
            return;
        }
        jackson2HttpMessageConverter.write(Res.unauthorized("OAuth2 认证错误"), MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
    }
}
