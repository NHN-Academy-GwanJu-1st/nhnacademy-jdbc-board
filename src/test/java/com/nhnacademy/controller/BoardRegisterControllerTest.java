package com.nhnacademy.controller;

import com.nhnacademy.domain.BoardRegisterRequest;
import com.nhnacademy.domain.UserVO;
import com.nhnacademy.exception.NotAcceptableFileTypeException;
import com.nhnacademy.exception.UserNotAllowedException;
import com.nhnacademy.service.BoardService;
import com.nhnacademy.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class BoardRegisterControllerTest {

    private BoardService boardService;
    private FileService fileService;
    private MockMvc mockMvc;
    private MockHttpSession session;
    private MockMultipartFile emptyFile;

    @BeforeEach
    void setUp() {
        boardService = mock(BoardService.class);
        fileService = mock(FileService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new BoardRegisterController(boardService, fileService)).build();
        session = new MockHttpSession();
        emptyFile = new MockMultipartFile(
                "uploadFiles",
                "",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "".getBytes()
        );
    }

    @Test
    void getBoardRegisterFormTest() throws Exception {
        mockMvc.perform(get("/board/register"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/board/registerForm"));
    }

    @Test
    void doRegister_emptyFile_invalidBoard_thenValidationFailedException() throws Exception {

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "", "");

        mockMvc.perform(multipart("/board/register")
                        .file(emptyFile)
                        .param("userName", board.getUserName())
                        .param("title", board.getTitle())
                        .param("content", board.getContent()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));

    }

    @Test
    void doRegister_emptyFile_success() throws Exception {

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "testTitle", "testContent");

        when(boardService.registerBoard(board)).thenReturn(1);

        mockMvc.perform(multipart("/board/register")
                        .file(emptyFile)
                        .param("userName", board.getUserName())
                        .param("title", board.getTitle())
                        .param("content", board.getContent()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void doRegister_existFile_invalidFileType_thenNotAcceptableFileTypeException() throws Exception {
        String UPLOAD_DIR = "/Users/gwanii/Downloads/";

        String filePath = UPLOAD_DIR + "페페_img.png";

        FileInputStream inputStream = new FileInputStream(filePath);

        MockMultipartFile imgFile = new MockMultipartFile(
                "uploadFiles",
                "페페_img.xml",
                MediaType.TEXT_XML_VALUE,
                inputStream
        );

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "testTitle", "testContent");

        when(boardService.registerBoard(board)).thenReturn(1);

        mockMvc.perform(multipart("/board/register")
                        .file(imgFile)
                        .param("userName", board.getUserName())
                        .param("title", board.getTitle())
                        .param("content", board.getContent()))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotAcceptableFileTypeException));

        inputStream.close();
    }

    @Test
    void doRegisterBoard_existSingleFile_success() throws Exception {
        String UPLOAD_DIR = "/Users/gwanii/Downloads/";

        String filePath = UPLOAD_DIR + "페페_img.png";

        FileInputStream inputStream = new FileInputStream(filePath);

        MockMultipartFile imgFile = new MockMultipartFile(
                "uploadFiles",
                "페페_img.png",
                MediaType.IMAGE_PNG_VALUE,
                inputStream
        );

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "testTitle", "testContent");

        when(fileService.insertFile(board.getId(), board.getUserName(), imgFile.getName())).thenReturn(1);
        when(boardService.registerBoard(board)).thenReturn(1);

        mockMvc.perform(multipart("/board/register")
                        .file(imgFile)
                        .param("userName", board.getUserName())
                        .param("title", board.getTitle())
                        .param("content", board.getContent()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        inputStream.close();

    }

    @Test
    void doRegisterBoard_existMultiFile_success() throws Exception {
        String UPLOAD_DIR = "/Users/gwanii/Downloads/";

        String filePath = UPLOAD_DIR + "페페_img.png";

        FileInputStream inputStream1 = new FileInputStream(filePath);

        MockMultipartFile imgFile1 = new MockMultipartFile(
                "uploadFiles",
                "페페_img.png",
                MediaType.IMAGE_PNG_VALUE,
                inputStream1
        );

        FileInputStream inputStream2 = new FileInputStream(filePath);

        MockMultipartFile imgFile2 = new MockMultipartFile(
                "uploadFiles",
                "페페_img2.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                inputStream2
        );

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "testTitle", "testContent");

        when(fileService.insertFile(board.getId(), board.getUserName(), imgFile1.getName())).thenReturn(1);
        when(fileService.insertFile(board.getId(), board.getUserName(), imgFile2.getName())).thenReturn(1);
        when(boardService.registerBoard(board)).thenReturn(1);

        mockMvc.perform(multipart("/board/register")
                        .file(imgFile1)
                        .file(imgFile2)
                        .param("userName", board.getUserName())
                        .param("title", board.getTitle())
                        .param("content", board.getContent()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        inputStream1.close();
        inputStream2.close();

    }

    @Test
    void getBoardModifyForm_notAllowedUser_thenUserNotAllowedException() throws Exception {
        UserVO user = new UserVO("notAllowedUser", "User");
        long boardId = 123;

        session.setAttribute("user", user);

        when(boardService.allowedUserCheck(boardId, user)).thenReturn(false);

        mockMvc.perform(get("/board/modify/{boardId}", boardId)
                        .session(session))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotAllowedException));
            }

    @Test
    void getBoardModifyForm_success() throws Exception {

        UserVO user = new UserVO("testUser", "User");
        long boardId = 123;

        session.setAttribute("user", user);

        when(boardService.allowedUserCheck(boardId, user)).thenReturn(true);

        mockMvc.perform(get("/board/modify/{boardId}", boardId)
                        .session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/board/modify"));
    }

    @Test
    void doBoardModify_invalidBoard_thenValidationFailedException() throws Exception {

        UserVO user = new UserVO("testUser", "User");
        long boardId = 123;

        session.setAttribute("user", user);

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "", "");

        when(boardService.allowedUserCheck(boardId, user)).thenReturn(true);

        mockMvc.perform(multipart("/board/modify/{boardId}", boardId)
                        .file(emptyFile)
                        .param("userName", board.getUserName())
                        .param("title", board.getTitle())
                        .param("content", board.getContent())
                        .session(session))
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));
    }

    @Test
    void doBoardModify_notAllowedUser_thenUserNotAllowedException() throws Exception {

        UserVO user = new UserVO("notAllowedUser", "User");
        long boardId = 123;

        session.setAttribute("user", user);

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "testTitle", "testContent");

        when(boardService.allowedUserCheck(boardId, user)).thenReturn(false);

        mockMvc.perform(multipart("/board/modify/{boardId}", boardId)
                        .file(emptyFile)
                        .param("userName", board.getUserName())
                        .param("title", board.getTitle())
                        .param("content", board.getContent())
                        .session(session))
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotAllowedException));
    }

    @Test
    void doBoardModify_emptyFile_success() throws Exception {

        UserVO user = new UserVO("testUser", "User");
        long boardId = 123;

        session.setAttribute("user", user);

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "testTitle", "testContent");

        when(boardService.allowedUserCheck(boardId, user)).thenReturn(true);
        when(boardService.modifyBoard(
                boardId,
                board.getUserName(),
                board.getTitle(),
                board.getContent())
        ).thenReturn(1);

        mockMvc.perform(multipart("/board/modify/{boardId}", boardId)
                        .file(emptyFile)
                        .param("userName", board.getUserName())
                        .param("title", board.getTitle())
                        .param("content", board.getContent())
                        .session(session))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void doModifyBoard_existFile_invalidFileType_thenNotAcceptableFileTypeException() throws Exception {
        String UPLOAD_DIR = "/Users/gwanii/Downloads/";

        String filePath = UPLOAD_DIR + "페페_img.png";

        FileInputStream inputStream = new FileInputStream(filePath);

        MockMultipartFile imgFile = new MockMultipartFile(
                "uploadFiles",
                "페페_img.xml",
                MediaType.TEXT_XML_VALUE,
                inputStream
        );

        long boardId = 123;
        String userName = "testUser";
        UserVO user = new UserVO(userName, "User");

        session.setAttribute("user", user);

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "testTitle", "testContent");

        when(boardService.allowedUserCheck(boardId, user)).thenReturn(true);
        when(boardService.registerBoard(board)).thenReturn(0);

        mockMvc.perform(multipart("/board/modify/{boardId}", boardId)
                        .file(imgFile)
                        .session(session)
                        .param("userName", board.getUserName())
                        .param("title", board.getTitle())
                        .param("content", board.getContent()))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotAcceptableFileTypeException));

        inputStream.close();
    }

    @Test
    void doModifyBoard_existSigleFile_success() throws Exception {
        String UPLOAD_DIR = "/Users/gwanii/Downloads/";

        String filePath = UPLOAD_DIR + "페페_img.png";

        FileInputStream inputStream = new FileInputStream(filePath);

        MockMultipartFile imgFile = new MockMultipartFile(
                "uploadFiles",
                "페페_img.png",
                MediaType.IMAGE_PNG_VALUE,
                inputStream
        );

        long boardId = 123;
        String userName = "testUser";
        UserVO user = new UserVO(userName, "User");

        session.setAttribute("user", user);

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "testTitle", "testContent");

        when(boardService.allowedUserCheck(boardId, user)).thenReturn(true);
        when(boardService.registerBoard(board)).thenReturn(1);
        when(fileService.deleteFileByBoardId(boardId)).thenReturn(1);
        when(fileService.insertFile(boardId, userName, imgFile.getName())).thenReturn(1);

        mockMvc.perform(multipart("/board/modify/{boardId}", boardId)
                        .file(imgFile)
                        .session(session)
                        .param("userName", board.getUserName())
                        .param("title", board.getTitle())
                        .param("content", board.getContent()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        inputStream.close();
    }

    @Test
    void doModifyBoard_existMultiFile_success() throws Exception {
        String UPLOAD_DIR = "/Users/gwanii/Downloads/";

        String filePath = UPLOAD_DIR + "페페_img.png";

        FileInputStream inputStream1 = new FileInputStream(filePath);

        MockMultipartFile imgFile1 = new MockMultipartFile(
                "uploadFiles",
                "페페_img.png",
                MediaType.IMAGE_PNG_VALUE,
                inputStream1
        );

        FileInputStream inputStream2 = new FileInputStream(filePath);

        MockMultipartFile imgFile2 = new MockMultipartFile(
                "uploadFiles",
                "페페_img2.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                inputStream2
        );

        long boardId = 123;
        String userName = "testUser";
        UserVO user = new UserVO(userName, "User");

        session.setAttribute("user", user);

        BoardRegisterRequest board = new BoardRegisterRequest("testUser", "testTitle", "testContent");

        when(boardService.allowedUserCheck(boardId, user)).thenReturn(true);
        when(boardService.registerBoard(board)).thenReturn(1);
        when(fileService.deleteFileByBoardId(boardId)).thenReturn(1);
        when(fileService.insertFile(boardId, userName, imgFile1.getName())).thenReturn(1);
        when(fileService.insertFile(boardId, userName, imgFile2.getName())).thenReturn(1);

        mockMvc.perform(multipart("/board/modify/{boardId}", boardId)
                        .file(imgFile1)
                        .file(imgFile2)
                        .session(session)
                        .param("userName", board.getUserName())
                        .param("title", board.getTitle())
                        .param("content", board.getContent()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        inputStream1.close();
        inputStream2.close();
    }

}