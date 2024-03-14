package com.application.service.user.controller;

import com.application.common.domain.dto.jwtService.LoginRequestDto;
import com.application.common.domain.dto.userService.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
    private static  Cookie[] cookies = new Cookie[0];

    //@Test @Order(1)
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
    @DisplayName("로그인 후 내정보 요청")

    void loginTest() throws Exception {
        //given
        LoginRequestDto dto = new LoginRequestDto("z1s5c3x2@gmail.com","asdASD!@#1");

        //when
        MvcResult res = mvc.perform(post("/user/login").content(
                objectMapper.writeValueAsString(dto)
        ).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        // cookie set
        cookies = res.getResponse().getCookies();
        System.out.println("쿠키 출력");
        Arrays.stream(cookies).forEach(c->{
            System.out.println(c.getName()+" : "+ c.getValue());
        });
        ResultActions resultActions = mvc.perform(get("/user/mypage")
                        .cookie(cookies))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("내정보"));

    }

    @Test @Order(4)
    @DisplayName("accessToken 재발급")
    void actRefresh() throws Exception{
        //given

        Thread.sleep(10000); // act liveTime

        //when
            ResultActions resultActions = mvc.perform(get("/user/mypage")
                        .cookie(cookies))
                .andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("내정보"))
                .andReturn();

    }
    @Test @Order(5)
    @DisplayName("일반 사용자로 관리자 페이지 이동")
    void afterLoginRequest()throws Exception{
        //given
        //cookie
        //when
        ResultActions resultActions = mvc.perform(get("/user/admin")
                        .cookie(cookies))
                .andDo(print());
        //then
        resultActions.andExpect(status().isForbidden());
    }

    @Test @Order(6)
    @DisplayName("토큰 탈취,변조  시나리오")
    void actHijacking()throws Exception{
        //given
        // 엑세스 토큰 재발급 이후 리프레시 토큰은 업데이트 되어 탈취된 토큰으로 요청하는 시나리오
        Arrays.stream(cookies).forEach(c->{
            if(c.getName().equals("refreshToken")){
                c.setValue("no"); // 변조
            }
        });
        Thread.sleep(10000);
        //when
        ResultActions resultActions = mvc.perform(get("/user/mypage")
                        .cookie(cookies))
                .andDo(print());
        //then
        MvcResult mvcResult = resultActions.andExpect(status().is3xxRedirection()).andReturn();
        // 토큰 삭제 유무
        Assertions.assertEquals(0, mvcResult.getResponse().getCookies().length);
    }


    @Test @Order(7)
    @DisplayName("비 로그인 인증 페이지")
    void notLoginRequest() throws Exception{
        mvc.perform(get("/user/admin")).andDo(print());
    }

}