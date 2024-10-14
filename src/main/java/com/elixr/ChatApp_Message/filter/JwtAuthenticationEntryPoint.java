package com.elixr.ChatApp_Message.filter;

import com.elixr.ChatApp_Message.contants.MessageConstants;
import com.elixr.ChatApp_Message.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Response customErrorResponse = new Response();
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        customErrorResponse.setResponse(MessageConstants.ACCESS_DENIED+authException);
        response.getWriter().write(objectMapper.writeValueAsString(customErrorResponse));
    }
}
