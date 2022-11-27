package com.nhnacademy.service;

public interface HeartService {

    boolean findByBoardIdAndUserName(long boardId, String userName);

    int insertHeart(long boardId, String userName);

    int deleteHeart(long boardId, String userName);

}
