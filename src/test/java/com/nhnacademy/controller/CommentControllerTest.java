package com.nhnacademy.controller;

import com.nhnacademy.domain.CommentRegisterRequest;
import com.nhnacademy.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

class CommentControllerTest {


    private CommentService commentService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        commentService = Mockito.mock(CommentService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new CommentController(commentService)).build();
    }

    @Test
    void registerComment() {

    }
}