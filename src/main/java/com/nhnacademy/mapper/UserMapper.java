package com.nhnacademy.mapper;

import com.nhnacademy.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    int exist(String username);

    User findByName(String username);
}
