package com.nhnacademy.controller;

import com.nhnacademy.exception.BoardNotFoundException;
import com.nhnacademy.exception.UserNotAllowedException;
import com.nhnacademy.exception.UserNotFoundException;
import com.nhnacademy.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(Exception ex, Model model) {
        log.error("", ex);
        model.addAttribute("exception", ex);
        return "/error";
    }

    @ExceptionHandler(UserNotAllowedException.class)
    public String handleUserNotAllowedException(Exception ex, Model model) {
        log.error("", ex);
        model.addAttribute("exception", ex);
        return "/error";
    }

    @ExceptionHandler(ValidationFailedException.class)
    public String handleValidationFailedException(Exception ex, Model model) {
        log.error("", ex);
        model.addAttribute("exception", ex);
        return "/error";
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public String handleBoardNotFoundException(Exception ex, Model model) {
        log.error("", ex);
        model.addAttribute("exception", ex);
        return "/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        log.error("", ex);
        model.addAttribute("exception", ex);
        return "/error";
    }
}
