package com.junior.security.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        setErrorResponse(response, StatusCode.ACCESS_DENIED);
    }

    private void setErrorResponse(
            HttpServletResponse response,
            StatusCode statusCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(statusCode.getHttpCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        CommonResponse errorResponse = CommonResponse.fail(statusCode);
        try {
            log.error("[{}] JWT fail, code: {}", Thread.currentThread().getStackTrace()[1].getMethodName(), statusCode);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
