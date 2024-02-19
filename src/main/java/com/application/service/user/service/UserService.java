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
    private final UserRepository userRepository;
    public String getTest(String tmp){
        userRepository.findAll();
        return tmp+"getTest";
    }

    
    public boolean register(UserDto userDto)
    {
        return userRepository.save(userDto.toEntity()).getUserNo() > 0;
    }
}
