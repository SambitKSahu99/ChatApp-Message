package com.elixr.ChatApp_Message.filter;

import com.elixr.ChatApp_Message.contants.MessageAppConstants;
import com.elixr.ChatApp_Message.contants.UrlConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final WebClient webClient;
    @Getter
    private String jwtToken;


    public JwtFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(UrlConstants.AUTH_SERVICE_URL).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractToken(request);
            jwtToken = token;
            if (StringUtils.hasText(token)) {
                String userName = webClient.post()
                        .uri(UrlConstants.VERIFY_TOKEN_ENDPOINT)
                        .header(MessageAppConstants.AUTHORIZATION_HEADER
                                , MessageAppConstants.BEARER + token)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                                clientResponse.bodyToMono(String.class)
                                        .flatMap(errorMessage -> Mono.error(new RuntimeException(MessageAppConstants.CLIENT_ERROR + errorMessage))))
                        .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                                clientResponse.bodyToMono(String.class)
                                        .flatMap(errorMessage -> Mono.error(new RuntimeException(MessageAppConstants.SERVER_ERROR + errorMessage))))
                        .bodyToMono(String.class)
                        .block();
                if (userName != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userName, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }catch(RuntimeException exception){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(MessageAppConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(MessageAppConstants.BEARER)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

