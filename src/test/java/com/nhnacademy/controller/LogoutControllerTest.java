package com.nhnacademy.controller;

import com.nhnacademy.domain.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class LogoutControllerTest {

    private MockMvc mockMvc;
    MockHttpSession session;
    UserVO sessionValue;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new LogoutController()).build();
        session = new MockHttpSession();
        sessionValue = new UserVO("username", "role");
    }

    @Test
    void doLogout_adminSession() throws Exception {

        session.setAttribute("user", sessionValue);
        mockMvc.perform(get("/logout"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(result -> assertThat(result.getRequest().getSession().getAttribute("user")).isNull())
                .andExpect(view().name("redirect:/"));
    }

}