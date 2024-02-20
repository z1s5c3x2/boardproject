package com.application.common.config.jwtconfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends GenericFilterBean {
    private final JwtTokenProvider JwtTokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request);
        if (token != null && JwtTokenProvider.validateToken(token)) {

            Authentication authentication = JwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security context에 인증 정보 저장 name :{} , uri : {}",authentication.getName(),((HttpServletRequest) request).getRequestURI());
        }else {
            log.error("유효한 JWT 토큰이 없습니다, uri : {}",((HttpServletRequest) request).getRequestURI());
        }
        chain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest req){

        String token = req.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }
}
