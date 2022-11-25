package com.nhnacademy.controller;

import com.nhnacademy.domain.Board;
import com.nhnacademy.domain.PageDTO;
import com.nhnacademy.domain.User;
import com.nhnacademy.service.BoardService;
import com.nhnacademy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

    private final BoardService boardService;

    public HomeController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public String home(@SessionAttribute(value = "user", required = false) User loginUser,
                       Model model) {

        if (isAdmin(loginUser)) {
            int total = boardService.boardTotalCount();
            PageDTO pageDTO = new PageDTO(total);
            model.addAttribute("boards", boardService.findAll(pageDTO.getAmount(), pageDTO.getSkip()));
            model.addAttribute("page", pageDTO);
            return "/index";
        }

        int total = boardService.nonDeleteBoardTotalCount();
        PageDTO pageDTO = new PageDTO(total);
        model.addAttribute("boards", boardService.boardGetList(pageDTO.getAmount(), pageDTO.getSkip()));
        model.addAttribute("page", pageDTO);

        return "/index";
    }

    @GetMapping(params = {"pageNum", "amount"})
    public String pagingHome(@SessionAttribute(value = "user", required = false) User loginUser,
                             @RequestParam(value = "pageNum", required = false) int pageNum,
                             @RequestParam(value = "amount", required = false) int amount,
                             Model model) {

        if (isAdmin(loginUser)) {
            int total = boardService.boardTotalCount();
            PageDTO pageDTO = new PageDTO(pageNum, amount, total);
            model.addAttribute("boards", boardService.findAll(pageDTO.getAmount(), pageDTO.getSkip()));
            model.addAttribute("page", pageDTO);
            return "/index";
        }

        int total = boardService.nonDeleteBoardTotalCount();
        PageDTO pageDTO = new PageDTO(pageNum, amount, total);
        model.addAttribute("boards", boardService.boardGetList(pageDTO.getAmount(), pageDTO.getSkip()));
        model.addAttribute("page", pageDTO);

        return "/index";
    }

    private boolean isAdmin(User loginUser) {
        return Objects.nonNull(loginUser) && loginUser.getRole().equals("Admin");
    }
}
