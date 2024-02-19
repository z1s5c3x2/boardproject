package com.application.service.user.controller;

import com.application.common.domain.dto.userService.UserDto;
import com.application.service.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    @GetMapping("/test")
    public String getTest(@RequestParam("tmp") String tmp){
        return userService.getTest(tmp);
    }


    @PostMapping(value = "/register")
    public boolean userRegister(@Valid UserDto userDto) throws ValidationException {
        userService.register(userDto);
        return true;
        //return userDto.getUserBirth() != null;
    }

}