package com.nhnacademy.controller;

import com.nhnacademy.domain.Board;
import com.nhnacademy.domain.BoardRegisterRequest;
import com.nhnacademy.domain.User;
import com.nhnacademy.exception.UserNotAllowedException;
import com.nhnacademy.exception.ValidationFailedException;
import com.nhnacademy.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/register")
    public String getBoardRegisterForm() {
        return "/board/register";
    }

    @PostMapping("/register")
    public String doBoardRegister(@Valid @ModelAttribute(value = "board") BoardRegisterRequest boardRequest,
                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        boardService.registerBoard(
                boardRequest.getUserName(),
                boardRequest.getTitle(),
                boardRequest.getContent()
        );

        return "redirect:/";
    }

    @GetMapping("/{boardId}")
    public String getBoardDetail(@PathVariable(value = "boardId") long boardId,
                                 Model model) {

        Board board = boardService.findById(boardId);
        model.addAttribute("board", board);

        return "/board/detail";
    }

    @GetMapping("/modify/{boardId}")
    public String getModifyForm(@PathVariable(value = "boardId") long boardId,
                                @SessionAttribute(value = "user") User userSession,
                                Model model) {

        if (!boardService.allowedUserCheck(boardId, userSession)) {
            throw new UserNotAllowedException();
        }

        Board board = boardService.findById(boardId);
        model.addAttribute("board", board);

        return "/board/modify";
    }

    @PostMapping("/modify/{boardId}")
    public String doModify(@PathVariable(value = "boardId") long boardId,
                           @Valid @ModelAttribute(value = "board") BoardRegisterRequest boardRequest,
                           @SessionAttribute(value = "user") User userSession,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        if (!boardService.allowedUserCheck(boardId, userSession)) {
            throw new UserNotAllowedException();
        }

        boardService.modifyBoard(
                boardId,
                boardRequest.getUserName(),
                boardRequest.getTitle(),
                boardRequest.getContent()
        );

        return "redirect:/";
    }

    @GetMapping("/delete/{boardId}")
    public String doDelete(@PathVariable(value = "boardId") long boardId,
                           @SessionAttribute(value = "user") User userSession) {

        if (!boardService.allowedUserCheck(boardId, userSession)) {
            throw new UserNotAllowedException();
        }

        boardService.deleteBoard(boardId);


        return "redirect:/";
    }
}