package com.nhnacademy.controller;

import com.nhnacademy.exception.InvalidHeartException;
import com.nhnacademy.service.HeartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/heart")
public class HeartController {

    private final HeartService heartService;

    public HeartController(HeartService heartService) {
        this.heartService = heartService;
    }

    @PostMapping("/addHeart")
    public String doAddHeart(@RequestParam(value = "boardId") long boardId,
                             @RequestParam(value = "userName") String userName) {


        if (heartService.findByBoardIdAndUserName(boardId, userName)) {
            throw new InvalidHeartException();
        }

        heartService.insertHeart(boardId, userName);

        return "redirect:/board/detail/" + boardId;
    }

    @PostMapping("/deleteHeart")
    public String doDeleteHeart(@RequestParam(value = "boardId") long boardId,
                                @RequestParam(value = "userName") String userName) {

        if (!heartService.findByBoardIdAndUserName(boardId, userName)) {
            throw new InvalidHeartException();
        }

        heartService.deleteHeart(boardId, userName);

        return "redirect:/board/detail/" + boardId;
    }
}
