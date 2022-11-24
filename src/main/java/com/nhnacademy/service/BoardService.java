package com.nhnacademy.service;

import com.nhnacademy.domain.Board;
import com.nhnacademy.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardService {
    List<Board> findAll();

    Board findById(long id);

    int registerBoard(String userName, String title, String content);

    int modifyBoard(long id, String modifierName, String title, String content);

    int deleteBoard(long id);

    boolean allowedUserCheck(long id, User userSession);

    int boardTotalCount();

    List<Board> boardGetList(int amount, int skip);
}
