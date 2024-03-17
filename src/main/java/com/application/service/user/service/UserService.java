package com.application.service.user.service;

import com.application.common.config.jwt.JwtTokenProvider;
import com.application.common.domain.dto.jwtService.LoginRequestDto;
import com.application.common.domain.dto.jwtService.TokenDto;
import com.application.common.domain.dto.userService.UserDto;
import com.application.common.domain.entity.userService.UserEntity;
import com.application.service.user.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 회원가입
    public boolean userRegister(UserDto userDto) {
        userDto.setUserPassword(passwordEncoder.encode(userDto.getPassword()));
        return userEntityRepository.save(userDto.toEntity()).getUserNo() > 0;
    }

    // 로그인
    public TokenDto userLogin(LoginRequestDto loginRequestDto){
        log.info("userLogin hi");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUserEmail(),loginRequestDto.getUserPassword());
        log.info("UsernamePasswordAuthenticationToken = {}",authenticationToken);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("authentication create : {}",authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.createToken(authentication);

    }

    public boolean userDelete(String userEmail){
        return userEntityRepository.findByUserEmail(userEmail)
                        .map(userEntity -> {
                            userEntityRepository.delete(userEntity);
                            return true;
                        }).orElseGet(()->false);
    }
}
