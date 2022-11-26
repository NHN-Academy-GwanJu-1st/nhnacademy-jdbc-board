package com.nhnacademy.controller;

import com.nhnacademy.domain.Board;
import com.nhnacademy.domain.PageDTO;
import com.nhnacademy.domain.UserVO;
import com.nhnacademy.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class HomeControllerTest {

    private BoardService boardService;
    private MockHttpSession session;
    private MockMvc mockMvc;

    List<Board> boards;

    @BeforeEach
    void setUp() {
        boardService = mock(BoardService.class);
        session = new MockHttpSession();
        mockMvc = MockMvcBuilders.standaloneSetup(new HomeController(boardService)).build();

        boards = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            boards.add(new Board(i + 1, "testUser", "", "testTitle", "testContent", LocalDateTime.now(), LocalDateTime.now(), 'N', 0));
        }
    }

    @DisplayName("session이 관리자인 경우, 삭제 처리된 게시글까지 모두 가져오기")
    @Test
    void getHome_sessionIsAdmin() throws Exception {

        session.setAttribute("user", new UserVO("admin", "Admin"));
        boards.add(new Board(16, "testUser", "", "deleteBoard", "delete", LocalDateTime.now(), LocalDateTime.now(), 'Y', 0));
        when(boardService.boardTotalCount()).thenReturn(boards.size());

        PageDTO pageDTO = new PageDTO(boards.size());

        when(boardService.findAll(pageDTO.getAmount(), pageDTO.getSkip())).thenReturn(boards);

        mockMvc.perform(get("/")
                        .session(session))
                .andDo(print())
                .andExpect(model().attribute("boards", boards));
        session.removeAttribute("user");

    }

    @DisplayName("세션이 존재하지 않거나, 관리자가 아닌경우, 삭제된 게시글은 보여주지 않음.")
    @Test
    void getHome_noSession() throws Exception {

        session.setAttribute("user", new UserVO("user", "User"));

        List<Board> expectBoardList = new ArrayList<>();
        expectBoardList.addAll(boards);

        boards.add(new Board(16, "testUser", "", "deleteBoard", "delete", LocalDateTime.now(), LocalDateTime.now(), 'Y', 0));

        when(boardService.nonDeleteBoardTotalCount()).thenReturn(boards.size());
        PageDTO pageDTO = new PageDTO(boards.size());

        when(boardService.boardGetList(pageDTO.getAmount(), pageDTO.getSkip())).thenReturn(expectBoardList);

        mockMvc.perform(get("/")
                        .session(session))
                .andDo(print())
                .andExpect(model().attribute("boards", expectBoardList));
    }

    @Test
    void paging_sessionIsAdmin() throws Exception {

        int pageNum = 1;
        int amount = 20;

        session.setAttribute("user", new UserVO("admin", "Admin"));
        boards.add(new Board(16, "testUser", "", "deleteBoard", "delete", LocalDateTime.now(), LocalDateTime.now(), 'Y', 0));
        when(boardService.boardTotalCount()).thenReturn(boards.size());

        PageDTO pageDTO = new PageDTO(pageNum, amount, boards.size());

        when(boardService.findAll(pageDTO.getAmount(), pageDTO.getSkip())).thenReturn(boards);

        mockMvc.perform(get("/")
                        .session(session)
                        .param("pageNum", Integer.toString(pageNum))
                        .param("amount", Integer.toString(amount)))
                .andDo(print())
                .andExpect(model().attribute("boards", boards));

    }

    @Test
    void paging_noSession() throws Exception {

        int pageNum = 1;
        int amount = 20;

        when(boardService.nonDeleteBoardTotalCount()).thenReturn(boards.size());
        PageDTO pageDTO = new PageDTO(pageNum, amount, boards.size());

        when(boardService.boardGetList(pageDTO.getAmount(), pageDTO.getSkip())).thenReturn(boards);

        mockMvc.perform(get("/")
                        .param("pageNum", Integer.toString(pageNum))
                        .param("amount", Integer.toString(amount)))
                .andDo(print())
                .andExpect(model().attribute("boards", boards));
    }
}