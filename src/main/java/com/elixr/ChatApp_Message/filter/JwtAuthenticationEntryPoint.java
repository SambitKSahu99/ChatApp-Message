package com.elixr.ChatApp_Message.filter;

import com.elixr.ChatApp_Message.contants.MessageAppConstants;
import com.elixr.ChatApp_Message.contants.MessageConstants;
import com.elixr.ChatApp_Message.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Response customErrorResponse = new Response();
        ObjectMapper objectMapper = new ObjectMapper();
        if (authException.getMessage().contains(MessageAppConstants.EXPIRED) || authException.getMessage().contains(MessageAppConstants.INVALID)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            customErrorResponse.setResponse(MessageConstants.INVALID_TOKEN);
            response.getWriter().write(objectMapper.writeValueAsString(customErrorResponse));
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            customErrorResponse.setResponse(MessageConstants.ACCESS_DENIED);
            response.getWriter().write(objectMapper.writeValueAsString(customErrorResponse));
        }
    }
}
