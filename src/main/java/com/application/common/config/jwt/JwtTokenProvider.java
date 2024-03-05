package com.application.common.config.jwt;


import com.application.common.domain.dto.jwtService.TokenDto;
import com.application.common.domain.dto.userService.UserDto;
import com.application.common.util.redis.RefreshToken;
import com.application.service.redis.cdi.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.lettuce.core.RedisException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider{
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long accessTokenLiveSecond;
    private SecretKey key;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret,
                            @Value("${spring.jwt.access-token-validity-in-seconds}") long accessTokenLiveSecond,
                            RefreshTokenRepository refreshTokenRepository) {
        this.secret = secret;
        this.accessTokenLiveSecond = accessTokenLiveSecond *1000;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostConstruct
    public void keyInit(){
        log.info("keyset : {}",secret);
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public TokenDto createToken(Authentication authentication){
        return TokenDto.builder()
                .accessToken(createAccessToken(authentication))
                .refreshToken(createRefreshToken()).build();
    }

    public String createAccessToken(Authentication authentication){
        log.info("create access token {}",authentication);
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date validity = new Date(new Date().getTime()+this.accessTokenLiveSecond);
        return Jwts.builder().subject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key,Jwts.SIG.HS512)
                .expiration(validity)
                .compact();
    }

    private String createRefreshToken(){
        RefreshToken rft =RefreshToken.builder().userEmail(((UserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserEmail()).refreshToken(UUID.randomUUID().toString()).build();
        try{
            saveToRedisRefreshToke(rft);
        }catch(Exception e) {
            throw new RedisException(e);
        }
        return rft.getRefreshToken();
    }
    private void saveToRedisRefreshToke(RefreshToken refreshToken){

        log.info("rt  = {} ",refreshToken.toString());
        log.info("context {}", SecurityContextHolder.getContext().getAuthentication());
        refreshTokenRepository.save(refreshToken);
    }
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserDetails user = new User(claims.getSubject(), "",authorities);
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

    public boolean validateToken(TokenDto token) {

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(token.getAccessToken());
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty. {} ", e);
        } catch (JwtException e){
            log.info("what ? {} ",e);
        }
        return false;
    }

    /* 임시  위 코드에서 만료 분리 예정*/
    public boolean isExpired(TokenDto token){
        if(token.getAccessToken() == null){return false;}

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token.getAccessToken());

        }catch (ExpiredJwtException e){
            log.error("act 시간 만료 {}",e);
            return true;
        }
//        catch (ExpiredJwtException e) {
//            token.setAccessToken(null);
//            log.info("Expired JWT Token", e);
//            return true;
//        }
        return false;
    }



    private Claims parseClaims(String accessToken) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean reloadAccessToken(TokenDto tokenDto){
        UserDto userDto = (UserDto)getAuthentication(tokenDto.getAccessToken()).getPrincipal();
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserEmail(userDto.getUserEmail());
        if(optionalRefreshToken.isPresent()){
            if(optionalRefreshToken.get().getRefreshToken().equals(tokenDto.getRefreshToken())){
                log.info("{} 사용자 토큰 재발급",userDto.getUserEmail());

                return true;
            }else{
                log.error("{} 사용자 refresh 토큰 탈취 ",userDto.getUserEmail());
                refreshTokenRepository.delete(optionalRefreshToken.get());
                tokenDto.setAccessToken(null);
                tokenDto.setRefreshToken(null);
            }
        }else{
            log.info("{} 사용자 refresh 만료 재 로그인",userDto.getUserEmail());
        }
        return false;
    }
}
