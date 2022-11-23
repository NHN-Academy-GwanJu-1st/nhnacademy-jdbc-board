package com.nhnacademy.service;

import com.nhnacademy.domain.User;
import com.nhnacademy.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public int existUser(String username) {
        return userMapper.exist(username);
    }

    @Override
    public User getUser(String username) {
        return userMapper.findByName(username);
    }
}
