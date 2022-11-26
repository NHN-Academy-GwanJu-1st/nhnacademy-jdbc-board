package com.nhnacademy.controller;

import com.nhnacademy.domain.User;
import com.nhnacademy.domain.UserVO;
import com.nhnacademy.exception.UserNotFoundException;
import com.nhnacademy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class LoginControllerTest {

    private UserService userService;
    MockHttpSession session;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        session = new MockHttpSession();
        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(userService)).build();
    }

    @Test
    void getLoginFormTest_sessionLoginUser() throws Exception {

        session.setAttribute("user", new UserVO("user", "User"));

        mockMvc.perform(get("/login")
                        .session(session))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void getLoginFormTest_nonSession() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("loginForm"));
    }

    @Test
    void doLogin_loginFail_thenUserNotFoundException() throws Exception {

        String userName = "user";
        String testPwd = "failPassword";

        when(userService.login(userName, testPwd)).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("username", userName)
                        .param("password", testPwd))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
    }

    @Test
    void doLogin_loginSuccess() throws Exception {


        User user = new User(1, "user", "password", "User");

        UserVO sessionValue = new UserVO(user.getUsername(), user.getRole());

        session.setAttribute("user", sessionValue);

        when(userService.login(user.getUsername(), user.getPassword())).thenReturn(user);

        mockMvc.perform(post("/login")
                        .param("username", user.getUsername())
                        .param("password", user.getPassword()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(result -> result.getRequest().getSession().getAttribute("user").equals(sessionValue))
                .andExpect(view().name("redirect:/"));
    }

}