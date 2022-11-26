package com.nhnacademy.controller;

import com.nhnacademy.domain.User;
import com.nhnacademy.domain.UserVO;
import com.nhnacademy.exception.UserNotFoundException;
import com.nhnacademy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin(@SessionAttribute(value = "user", required = false) UserVO loginUser) {

        if (Objects.nonNull(loginUser)) {
            return "redirect:/";
        }

        return "loginForm";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam(value = "username") String username,
                          @RequestParam(value = "password") String password,
                          HttpServletRequest request) {

        User user = userService.login(username, password);

        if (Objects.isNull(user)) {
            throw new UserNotFoundException();
        }

        UserVO userVO = new UserVO(user.getUsername(), user.getRole());
        HttpSession session = request.getSession();
        session.setAttribute("user", userVO);

        return "redirect:/";
    }
}
