package com.nhnacademy.service;

import com.nhnacademy.domain.Board;
import com.nhnacademy.domain.BoardRegisterRequest;
import com.nhnacademy.domain.User;
import com.nhnacademy.domain.UserVO;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardService {

    Board findById(long id);

    List<Board> findAll(int amount, int skip);

    int boardTotalCount();

    int nonDeleteBoardTotalCount();

    int registerBoard(BoardRegisterRequest boardRegisterRequest);

    int modifyBoard(long id, String modifierName, String title, String content);

    int deleteBoard(long id);

    int recoverBoard(long id);

    boolean allowedUserCheck(long id, UserVO userSession);

    List<Board> boardGetList(int amount, int skip);
}
