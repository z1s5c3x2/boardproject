package com.application.controller;


import com.application.model.dto.UserDto;
import com.application.model.entity.UserEntity;
import com.application.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;

    @GetMapping
    public void test(){
        userRepository.save(new UserEntity(100,"naweg"));
        System.out.println("sdfsdfg");
    }
}
