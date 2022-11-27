package com.nhnacademy.controller;

import com.nhnacademy.domain.CommentRegisterRequest;
import com.nhnacademy.domain.UserVO;
import com.nhnacademy.exception.BoardNotFoundException;
import com.nhnacademy.exception.ValidationFailedException;
import com.nhnacademy.service.CommentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class CommentControllerTest {

    private CommentService commentService;
    private MockMvc mockMvc;
    MockHttpSession session;

    @BeforeEach
    void setUp() {
        commentService = mock(CommentService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new CommentController(commentService)).build();
        session = new MockHttpSession();
        session.setAttribute("user", new UserVO("testUser", "User"));
    }

    @Test
    void registerComment_notExistBoard_thenBoardNotFoundException() throws Exception {

        long boardId = 123;

        CommentRegisterRequest comment = new CommentRegisterRequest(boardId, "testUser", "testContent");

        when(commentService.registerComment(comment)).thenThrow(BoardNotFoundException.class);

        mockMvc.perform(post("/comment/register/{boardId}", boardId)
                        .param("boardId", String.valueOf(boardId))
                        .param("userName", comment.getUserName())
                        .param("content", comment.getContent()))
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BoardNotFoundException));

    }

    @Test
    void registerComment_invalidComment_thenValidationFailedException() {

        long boardId = 123;

        CommentRegisterRequest comment = new CommentRegisterRequest(boardId, "", "");

        when(commentService.registerComment(comment)).thenReturn(0);

        Throwable th = catchThrowable(() ->
                mockMvc.perform(post("/comment/register/{boardId}", boardId)
                                .param("boardId", String.valueOf(boardId))
                                .param("userName", comment.getUserName())
                                .param("content", comment.getContent()))
                        .andDo(print())
        );

        assertThat(th).isInstanceOf(NestedServletException.class)
                .hasCauseInstanceOf(ValidationFailedException.class);
    }

    @Test
    void registerComment_success() throws Exception {

        long boardId = 123;

        CommentRegisterRequest comment = new CommentRegisterRequest(boardId, "testUser", "testContent");

        when(commentService.registerComment(comment)).thenReturn(1);

        mockMvc.perform(post("/comment/register/{boardId}", boardId)
                        .param("boardId", String.valueOf(boardId))
                        .param("userName", comment.getUserName())
                        .param("content", comment.getContent()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/board/" + boardId));
    }

    @Test
    void commentModify_invalidComment_thenValidationFailedException() {

        long commentId = 5;
        long boardId = 123;

        CommentRegisterRequest comment = new CommentRegisterRequest(boardId, null, "");

        when(commentService.modifyComment(commentId, comment)).thenReturn(0);

        Throwable th = catchThrowable(() ->
                mockMvc.perform(post("/comment/modify/{commentId}", commentId)
                                .param("boardId", String.valueOf(comment.getBoardId()))
                                .param("userName", comment.getUserName())
                                .param("content", comment.getContent()))
                        .andDo(print())
        );

        assertThat(th).isInstanceOf(NestedServletException.class)
                .hasCauseInstanceOf(ValidationFailedException.class);

    }

    @Test
    void commentModify_success() throws Exception {
        long commentId = 5;
        long boardId = 123;

        CommentRegisterRequest comment = new CommentRegisterRequest(boardId, "testUser", "testComment");

        when(commentService.modifyComment(commentId, comment)).thenReturn(1);

        mockMvc.perform(post("/comment/modify/{commentId}", commentId)
                        .param("boardId", String.valueOf(comment.getBoardId()))
                        .param("userName", comment.getUserName())
                        .param("content", comment.getContent()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/board/" + comment.getBoardId()));

    }

    @Test
    void commentDelete_success() throws Exception {
        long commentId = 5;
        long boardId = 123;

        when(commentService.deleteComment(commentId)).thenReturn(1);

        mockMvc.perform(post("/comment/delete/{commentId}", commentId)
                        .param("boardId", String.valueOf(boardId)))
                        .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/board/" + boardId));
    }






}