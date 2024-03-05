package com.application.common.config.jwt;

import com.application.common.domain.dto.jwtService.TokenDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    private String sd = "accessToken";
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Cookie a = WebUtils.getCookie( (HttpServletRequest) request,"accessToken");

        //System.out.println(a != null ? "토쿤?"+ a.getValue().toString():null);

        //        System.out.println(resolveTokenRequest((HttpServletRequest) request));
        //        String token = accessTokenResolveToken((HttpServletRequest) request);

        TokenDto tokenDto = resolveTokenRequest((HttpServletRequest) request);

        if(tokenDto.isNull()){
            log.info("토큰 발급 필요");
        } else if( tokenDto.getRefreshToken() != null && jwtTokenProvider.isExpired(tokenDto)){
            log.info("토큰 재발급 ㄱㄱ");
            Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String act = jwtTokenProvider.createAccessToken(authentication);
            tokenDto.setAccessToken(act);
            Arrays.stream(((HttpServletRequest) request).getCookies()).forEach(c->{
                if(c.getName().equals("accessToken")){
                    System.out.println("찾음");
                    System.out.println(act);
                    c.setValue(act);
                    ((HttpServletResponse)response).addCookie(c);
                }
            });

        }else if (!jwtTokenProvider.validateToken(tokenDto)){
            log.error("유효한 JWT 토큰이 없습니다, uri : {}",((HttpServletRequest) request).getRequestURI());
        }
//        else if (tokenDto.getAccessToken() == null &&  tokenDto.getRefreshToken() != null) {
//            log.info("토큰 재발급 ㄱㄱ");
//            jwtTokenProvider.reloadAccessToken(tokenDto);
//        }
        else{
            log.info("잘됨");
            Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            log.info("Security context에 인증 정보 저장 name :{} , uri : {}",authentication.getName(),((HttpServletRequest) request).getRequestURI());
//        }else {
//
//            log.error("유효한 JWT 토큰이 없습니다, uri : {}",((HttpServletRequest) request).getRequestURI());
//        }
        chain.doFilter(request,response);
    }

    private String bearerResolveToken(String accessToken){

        return accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;
//        //String token = req.getHeader(AUTHORIZATION_HEADER);
//        if(req.getCookies() == null) {return null;}
//        return Arrays.stream(req.getCookies()).filter(c->c.getName().equals(sd)).findFirst().map(t->{
//            String token = t.getValue();
//            if(){
//                return token.substring(7);
//            }else{
//                return token;
//            }
//        }).orElse(null);

    }


    public TokenDto resolveTokenRequest(HttpServletRequest req){
        Cookie at = WebUtils.getCookie(req,"accessToken");
        Cookie rt = WebUtils.getCookie(req,"refreshToken");
        return TokenDto.builder()
                .accessToken(at == null ? null : bearerResolveToken(at.getValue()))
                .refreshToken(rt == null ? null : rt.getValue())
                .build();

    }
}
