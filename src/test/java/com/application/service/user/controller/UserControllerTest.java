package com.application.service.user.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Get 테스트")
    void getTest() throws Exception {
        mvc.perform(get("/user/test").param("tmp","getApi"))
                .andExpect(status().isOk())
                .andExpect(content().string("getApigetTest"));
    }
}