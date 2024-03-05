package com.application.service.user.controller;

import com.application.common.domain.dto.jwtService.LoginRequestDto;
import com.application.common.domain.dto.userService.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("유저 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class UserControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test @Order(1)
    @DisplayName("가입 성공 테스트")
    void registerSuccessTest() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .userName("가나다")
                .userEmail("z1s5c3x2@gmail.com")
                .userPassword("asdASD!@#1")
                .userNickname("zxcv")
                .userPhone("010-1234-1234")
                .userGrade("USER")
                .userBirth("990201-1")
                .build();

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        for(Field f : userDto.getClass().getDeclaredFields()){
            f.setAccessible(true);
            params.add(f.getName(),f.get(userDto).toString());
        }
        //when
        ResultActions resultActions = mvc.perform(multipart("/user/register")
                        .params(params)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print());
        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    //@Test @Order(2)
    @DisplayName("가입 실패 테스트")
    void registerFailTest() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .userName("가나다")
                .userEmail("z1s5c3x2gmail.com")
                .userPassword("asdASD!@#1")
                .userNickname("asdf")
                .userPhone("010-1234-1234")
                .userBirth("990101-1")
                .userGrade("USER")
                .build();

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        for(Field f : userDto.getClass().getDeclaredFields()){
            f.setAccessible(true);
            if(f.get(userDto)==null){continue;}
            params.add(f.getName(),f.get(userDto).toString());
        }
        //when
        ResultActions resultActions = mvc.perform(multipart("/user/register")
                        .params(params)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print());
        // then
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test @Order(3)
    @DisplayName("로그인 테스트")

    void loginTest() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("z1s5c3x2@gmail.com","asdASD!@#1");
        mvc.perform(post("/user/login").content(
                objectMapper.writeValueAsString(dto)
        ).contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        mvc.perform(get("/user"))
                .andDo(print());
    }

    @Test @Order(4)
    @DisplayName("로그인 이후 요청")
    void afterLoginRequest()throws Exception{
        mvc.perform(get("/user"))
                .andDo(print());
    }


}