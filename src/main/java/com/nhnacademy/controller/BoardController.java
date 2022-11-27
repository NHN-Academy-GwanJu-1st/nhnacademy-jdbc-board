package com.nhnacademy.controller;

import com.nhnacademy.domain.*;
import com.nhnacademy.exception.UserNotAllowedException;
import com.nhnacademy.exception.ValidationFailedException;
import com.nhnacademy.mapper.FileMapper;
import com.nhnacademy.service.BoardService;
import com.nhnacademy.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {


    private final BoardService boardService;
    private final CommentService commentService;
    private final FileMapper fileMapper;

    public BoardController(BoardService boardService, CommentService commentService, FileMapper fileMapper) {
        this.boardService = boardService;
        this.commentService = commentService;
        this.fileMapper = fileMapper;
    }

    @GetMapping("/detail/{boardId}")
    public String getBoardDetail(@PathVariable(value = "boardId") long boardId,
                                 Model model) {

        Board board = boardService.findById(boardId);
        List<Comment> commentList = commentService.findByBoardId(boardId);
        List<FileDAO> fileList = fileMapper.findByBoardId(boardId);
        model.addAttribute("board", board);
        model.addAttribute("commentList", commentList);
        model.addAttribute("fileList", fileList);

        return "/board/detail";
    }


    @PostMapping("/delete/{boardId}")
    public String doDelete(@PathVariable(value = "boardId") long boardId,
                           @SessionAttribute(value = "user") UserVO userSession) {

        if (!boardService.allowedUserCheck(boardId, userSession)) {
            throw new UserNotAllowedException();
        }

        boardService.deleteBoard(boardId);


        return "redirect:/";
    }

    @PostMapping("/recover/{boardId}")
    public String doBoardRecover(@PathVariable(value = "boardId") long boardId,
                            @SessionAttribute(value = "user") UserVO loginUser) {

        if (!loginUser.getRole().equals("Admin")) {
            throw new UserNotAllowedException();
        }

        boardService.recoverBoard(boardId);

        return "redirect:/";
    }



}
