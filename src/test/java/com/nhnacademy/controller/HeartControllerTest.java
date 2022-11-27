package com.nhnacademy.controller;

import com.nhnacademy.exception.InvalidHeartException;
import com.nhnacademy.service.HeartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class HeartControllerTest {

    private HeartService heartService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        heartService = Mockito.mock(HeartService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new HeartController(heartService)).build();
    }

    @Test
    void doAddHeartTest_success() throws Exception {
        long boardId = 123;
        String userName = "testUser";

        when(heartService.findByBoardIdAndUserName(boardId, userName)).thenReturn(false);
        when(heartService.insertHeart(boardId, userName)).thenReturn(1);

        mockMvc.perform(post("/heart/addHeart")
                        .param("boardId", String.valueOf(boardId))
                        .param("userName", userName))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/board/detail/" + boardId));
    }

    @Test
    void doAddHeart_alreadyInHeart_thenInvalidHeartException() throws Exception {
        long boardId = 123;
        String userName = "testUser";

        when(heartService.findByBoardIdAndUserName(boardId, userName)).thenReturn(true);
        when(heartService.insertHeart(boardId, userName)).thenReturn(0);

        mockMvc.perform(post("/heart/addHeart")
                        .param("boardId", String.valueOf(boardId))
                        .param("userName", userName))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidHeartException));
    }

    @Test
    void doDeleteHeart_success() throws Exception {
        long boardId = 123;
        String userName = "testUser";

        when(heartService.findByBoardIdAndUserName(boardId, userName)).thenReturn(true);
        when(heartService.deleteHeart(boardId, userName)).thenReturn(1);

        mockMvc.perform(post("/heart/deleteHeart")
                        .param("boardId", String.valueOf(boardId))
                        .param("userName", userName))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/board/detail/" + boardId));
    }

    @Test
    void doDeleteHeart_AlreadyNotExistHeart_thenInvalidHeartException() throws Exception {
        long boardId = 123;
        String userName = "testUser";

        when(heartService.findByBoardIdAndUserName(boardId, userName)).thenReturn(false);
        when(heartService.deleteHeart(boardId, userName)).thenReturn(0);

        mockMvc.perform(post("/heart/deleteHeart")
                        .param("boardId", String.valueOf(boardId))
                        .param("userName", userName))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidHeartException));
    }
}