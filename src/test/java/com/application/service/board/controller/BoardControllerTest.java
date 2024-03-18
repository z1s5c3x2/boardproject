package com.application.service.board.controller;

import com.application.common.domain.dto.boardService.BoardDto;
import com.application.common.domain.dto.jwtService.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("게시판 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class BoardControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    private static  Cookie[] cookies1 = new Cookie[0];

    @Test @Order(1)
    @DisplayName("테스트 계정 로그인")
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
        cookies1= res.getResponse().getCookies();
        ResultActions resultActions = mvc.perform(get("/user/mypage")
                        .cookie(cookies1))
                .andDo(print());
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("내정보"));

    }

    @Test @Order(2)
    @DisplayName("게시글 목록 호출")
    void asd() throws Exception{
        mvc.perform(get("/board/list")
                        .param("page","1")
                        .param("size","2")
                        .cookie(cookies1))
                .andDo(print()).andExpect(status().isOk());

        mvc.perform(get("/board/list")
                        .param("page","2")
                        .param("size","2")
                        .cookie(cookies1))
                .andDo(print()).andExpect(status().isOk());
    }

    //@Test @Order(3)
    @DisplayName("게시글 작성")
    void postTest() throws Exception{
        //given
        BoardDto boardDto = BoardDto.builder()
                .boardNo(0)
                .boardContent("asd")
                .boardTitle("werw")
                .boardWriter("sdfsdf").build();
        //when
        mvc.perform(post("/board/post")
                        .content(objectMapper.writeValueAsString(boardDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookies1))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }
    //@Test @Order(6)
    @DisplayName("내가 쓴 글 목록")
    void myListTest() throws Exception{
        mvc.perform(get("/board/mylist").cookie(cookies1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //@Test @Order(5)
    @DisplayName("게시글 수정")
    void modifyBoard() throws Exception{
        //given
        BoardDto boardDto = BoardDto.builder()
                .boardTitle("modifyTest")
                .boardContent("modifyContent").build();
        //when
        mvc.perform(put("/board/modify")
                        .param("boardNo","2")
                .cookie(cookies1)
                        .content(objectMapper.writeValueAsString(boardDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //@Test @Odder(6)
    @DisplayName("게시글 삭제")
    void deleteBoard() throws Exception{

    }
}