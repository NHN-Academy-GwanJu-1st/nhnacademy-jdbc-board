package com.nhnacademy.controller;

import com.nhnacademy.domain.Board;
import com.nhnacademy.domain.BoardRegisterRequest;
import com.nhnacademy.domain.UserVO;
import com.nhnacademy.exception.BoardNotFoundException;
import com.nhnacademy.exception.UserNotAllowedException;
import com.nhnacademy.exception.ValidationFailedException;
import com.nhnacademy.service.BoardService;
import com.nhnacademy.service.CommentService;
import com.nhnacademy.service.FileService;
import com.nhnacademy.service.HeartService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BoardControllerTest {

    private BoardService boardService;
    private CommentService commentService;
    private FileService fileService;
    private HeartService heartService;
    private MockHttpSession session;
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        boardService = mock(BoardService.class);
        commentService = mock(CommentService.class);
        heartService = mock(HeartService.class);
        fileService = mock(FileService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new BoardController(boardService, commentService, fileService, heartService)).build();
        session = new MockHttpSession();
    }

    @Test
    void getBoardDetail_deletedBoard_thenBoardNotFoundException() throws Exception {

        long boardId = 123;

        when(boardService.findById(boardId)).thenThrow(BoardNotFoundException.class);

        mockMvc.perform(get("/board/detail/{boardId}", boardId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BoardNotFoundException));
    }

    @Test
    void getBoardDetail_sessionIsNull_success() throws Exception {

        long boardId = 123;

        Board board = new Board(boardId, "testUser", "", "testTitle", "testContent", LocalDateTime.now(), LocalDateTime.now(), 'N', 0);

        when(boardService.findById(boardId)).thenReturn(board);
        when(commentService.findByBoardId(boardId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/board/detail/{boardId}", boardId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("board", board))
                .andExpect(model().attribute("commentList", Collections.emptyList()))
                .andExpect(model().attribute("heartStatus", false))
                .andExpect(view().name("/board/detail"));
    }

    @Test
    void getBoardDetail_sessionIsNotNull_success() throws Exception {

        long boardId = 123;
        String userName = "testUser";
        Board board = new Board(boardId, userName, "", "testTitle", "testContent", LocalDateTime.now(), LocalDateTime.now(), 'N', 0);

        session.setAttribute(userName, "User");

        when(boardService.findById(boardId)).thenReturn(board);
        when(commentService.findByBoardId(boardId)).thenReturn(Collections.emptyList());
        when(heartService.findByBoardIdAndUserName(boardId, userName)).thenReturn(false);

        mockMvc.perform(get("/board/detail/{boardId}", boardId)
                        .session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("board", board))
                .andExpect(model().attribute("commentList", Collections.emptyList()))
                .andExpect(model().attribute("heartStatus", false))
                .andExpect(view().name("/board/detail"));
    }

    @Test
    void doBoardDelete_notAllowedUser_thenUserNotAllowedException() throws Exception {
        UserVO user = new UserVO("notAllowedUser", "User");
        long boardId = 123;

        session.setAttribute("user", user);

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "testTitle", "testContent");

        when(boardService.allowedUserCheck(boardId, user)).thenReturn(false);

        mockMvc.perform(post("/board/delete/{boardId}", boardId)
                        .session(session))
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotAllowedException));
    }

    @Test
    void doBoardDelete_success() throws Exception {
        UserVO user = new UserVO("testUser", "User");
        long boardId = 123;

        session.setAttribute("user", user);

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "testTitle", "testContent");

        when(boardService.allowedUserCheck(boardId, user)).thenReturn(true);
        when(boardService.deleteBoard(boardId)).thenReturn(1);

        mockMvc.perform(post("/board/delete/{boardId}", boardId)
                        .session(session))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void doBoardRecover_sessionIsNotAdmin_thenUserNotAllowedException() throws Exception {

        long boardId = 123;

        session.setAttribute("user", new UserVO("testUser", "User"));

        mockMvc.perform(post("/board/recover/{boardId}", boardId)
                        .session(session))
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotAllowedException));
    }

    @Test
    void doBoardRecover_sessionIsAdmin_success() throws Exception {

        long boardId = 123;

        session.setAttribute("user", new UserVO("admin", "Admin"));

        when(boardService.recoverBoard(boardId)).thenReturn(1);

        mockMvc.perform(post("/board/recover/{boardId}", boardId)
                        .session(session))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }



}