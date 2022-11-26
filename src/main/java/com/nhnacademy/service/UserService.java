package com.nhnacademy.service;

import com.nhnacademy.domain.User;

public interface UserService {

    int existUser(String username);
    User getUser(String username);

    User login(String username, String password);
}
