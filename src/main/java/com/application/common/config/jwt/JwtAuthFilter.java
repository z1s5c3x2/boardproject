package com.application.common.config.jwt;

import com.application.common.domain.dto.jwtService.TokenDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Cookie a = WebUtils.getCookie( (HttpServletRequest) request,"accessToken");

        //System.out.println(a != null ? "토쿤?"+ a.getValue().toString():null);

        //        System.out.println(resolveTokenRequest((HttpServletRequest) request));
        //        String token = accessTokenResolveToken((HttpServletRequest) request);

        TokenDto tokenDto = resolveTokenRequest((HttpServletRequest) request);

        if(tokenDto.isNull()){
            log.info("토큰 발급 필요");
        } else if( tokenDto.getRefreshToken() != null){
            log.info("토큰 확인");
            int res = jwtTokenProvider.validateToken(tokenDto);

            if(res==1){
                log.info("토큰 유효");
                Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());
                System.out.println("authentication = " + authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (res==101) {
                log.info("rct 확인 후 갱신");
                String actResult = jwtTokenProvider.reloadAccessToken(tokenDto);
                if(actResult.equals("block")){
                    log.info("토큰 변조,탈취 감지");
                } else if (actResult.equals("reLogin")) {
                    log.info("로그인 만료(리프레시 토큰 만료)");
                }else{
                    tokenDto.setAccessToken(actResult);
                    Arrays.stream(((HttpServletRequest) request).getCookies()).forEach(c->{
                        if(c.getName().equals("accessToken")){
                            System.out.println("찾음");
                            System.out.println(actResult);
                            c.setValue(actResult);
                            ((HttpServletResponse)response).addCookie(c);
                        }
                    });
                }
            }else{
                log.info("토큰 에러");
            }

        }
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
