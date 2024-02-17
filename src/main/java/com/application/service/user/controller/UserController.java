package com.application.service.user.controller;

import com.application.service.user.service.UserService;
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
}






//    @PostMapping("/register")
//    public void register(@Valid UserDto userDto){
//        userService.register(userDto);
//    }