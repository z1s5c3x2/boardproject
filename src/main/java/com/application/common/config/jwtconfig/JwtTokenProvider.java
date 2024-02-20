package com.application.common.config.jwtconfig;


import com.application.common.domain.dto.jwtService.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider{
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenValidityInMilliseconds;

    private SecretKey key;

//    public JwtTokenProvider(
//            @Value("${spring.jwt.secret}") String secret,
//            @Value("${spring.jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
//        this.secret = secret;
//        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
//    }
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
//    }

    @ConstructorBinding
    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret,
                            @Value("${spring.jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds *1000;
    }

    @PostConstruct
    public void keyInit(){
        log.info("keyset : {}",secret);
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public TokenDto createToken(Authentication authentication){
        log.info("sec {}",key);
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date validity = new Date(new Date().getTime()+this.tokenValidityInMilliseconds);

        String token = Jwts.builder().subject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key,SignatureAlgorithm.HS512)
                .expiration(validity)
                .compact();

        return TokenDto.builder()
                .token(token).build();
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

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
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
}
