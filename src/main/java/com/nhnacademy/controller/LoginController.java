package com.nhnacademy.controller;

import com.nhnacademy.domain.User;
import com.nhnacademy.exception.UserNotFoundException;
import com.nhnacademy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "/login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam(value = "username") String username,
                          @RequestParam(value = "password") String password,
                          HttpServletRequest request) {

        boolean loginResult = userService.login(username, password);

        if (!loginResult) {
            throw new UserNotFoundException();
        }

        User user = userService.getUser(username);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        return "redirect:/";
    }
}
