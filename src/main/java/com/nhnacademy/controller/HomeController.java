package com.nhnacademy.controller;

import com.nhnacademy.domain.Board;
import com.nhnacademy.domain.PageDTO;
import com.nhnacademy.domain.User;
import com.nhnacademy.service.BoardService;
import com.nhnacademy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final BoardService boardService;

    public HomeController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping()
    public String home(Model model) {
        int total = boardService.boardTotalCount();
        PageDTO pageDTO = new PageDTO(total);
        model.addAttribute("boards", boardService.boardGetList(pageDTO.getAmount(), pageDTO.getSkip()));
        model.addAttribute("page", pageDTO);

        return "/index";
    }

    @GetMapping(params = {"pageNum", "amount"})
    public String pagingHome(@RequestParam(value = "pageNum", required = false) int pageNum,
                              @RequestParam(value = "amount", required = false) int amount,
                              Model model) {
        int total = boardService.boardTotalCount();
        PageDTO pageDTO = new PageDTO(pageNum, amount, total);
        model.addAttribute("boards", boardService.boardGetList(pageDTO.getAmount(), pageDTO.getSkip()));
        model.addAttribute("page", pageDTO);


        return "/index";
    }

}
