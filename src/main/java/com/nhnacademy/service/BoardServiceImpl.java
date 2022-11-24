package com.nhnacademy.service;

import com.nhnacademy.domain.Board;
import com.nhnacademy.domain.User;
import com.nhnacademy.exception.BoardNotFoundException;
import com.nhnacademy.mapper.BoardMapper;
import com.nhnacademy.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    public BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    public List<Board> findAll() {
        return boardMapper.findAll();
    }

    @Override
    public Board findById(long id) {
        existCheck(id);

        return boardMapper.findById(id);
    }

    @Override
    public int registerBoard(String userName, String title, String content) {
        return boardMapper.registerBoard(userName, title, content);
    }

    @Override
    public int modifyBoard(long id, String modifierName, String title, String content) {
        existCheck(id);

        return boardMapper.modifyBoard(id, modifierName, title, content);
    }

    @Override
    public int deleteBoard(long id) {
        existCheck(id);

        return boardMapper.deleteBoard(id);
    }

    @Override
    public boolean allowedUserCheck(long id, User userSession) {

        if (userSession.getRole().equals("Admin")) {
            return true;
        }

        Board board = boardMapper.findById(id);
        if (board.getUserName().equals(userSession.getUsername())) {
            return true;
        }

        return false;
    }

    @Override
    public int boardTotalCount() {
        return boardMapper.findTotalCount();
    }

    @Override
    public List<Board> boardGetList(int amount, int skip) {
        return boardMapper.boardGetList(amount, skip);
    }

    private void existCheck(long id) {
        if (boardMapper.exist(id) == 0) {
            throw new BoardNotFoundException();
        }
    }
}
