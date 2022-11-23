package com.nhnacademy.service;

import com.nhnacademy.domain.Board;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardService {
    List<Board> findAll();

    Board findById(long id);

    int registerBoard(long userId, String title, String content);

    int modifyBoard(long id, long modifierId, String title, String content, LocalDateTime updateDate);

    int deleteBoard(long id);
}
