package com.application.service.user.service;

import com.application.common.domain.dto.userService.UserDto;
import com.application.service.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    public String getTest(String tmp){
        return tmp+"getTest";
    }

    
    public void register(UserDto userDto)
    {
        System.out.println("UserService.register");
        System.out.println("userDto = " + userDto);
    }
}
