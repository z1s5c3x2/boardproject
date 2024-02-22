package com.application.service.user.controller;

import com.application.common.domain.dto.jwtService.LoginRequestDto;
import com.application.common.domain.dto.jwtService.TokenDto;
import com.application.common.domain.dto.userService.UserDto;
import com.application.service.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;


@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String asd(){
        log.info("제발좀요 {} ", SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return "asd";
    }
    @PostMapping("/register")
    public boolean userRegister(@Valid UserDto userDto) {
        return userService.userRegister(userDto);
    }

    @PostMapping("/login")
    public TokenDto userLogin(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest req, HttpServletResponse res) throws IllegalAccessException {
        System.out.println("UserController.userLogin");
        TokenDto tok = userService.userLogin(loginRequestDto);
        for(Field f : tok.getClass().getDeclaredFields()){
            f.setAccessible(true);
            Cookie cookie = new Cookie(f.getName(),f.get(tok).toString());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            res.addCookie(cookie);
        }
        return tok;
    }

    @DeleteMapping("delete")
    public boolean userDelete(@RequestParam("userEmail") String userEmail){
        return userService.userDelete(userEmail);
    }

//    @GetMapping("/test")
//    public String getTest(@RequestParam("tmp") String tmp){
//        return userService.getTest(tmp);
//    }
}