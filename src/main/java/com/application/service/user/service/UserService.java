package com.application.service.user.service;

import com.application.common.domain.dto.userService.UserDto;
import com.application.service.user.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserEntityRepository userEntityRepository;
    public boolean register(UserDto userDto)
    {
        return userEntityRepository.save(userDto.toEntity()).getUserNo() > 0;
    }

    public String getTest(String tmp){
        userEntityRepository.findAll();
        return tmp+"getTest";
    }

}
