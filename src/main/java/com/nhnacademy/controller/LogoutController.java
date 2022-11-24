package com.nhnacademy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String doLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (Objects.nonNull(session.getAttribute("user"))) {
            session.removeAttribute("user");
        }

        return "redirect:/";
    }
}
