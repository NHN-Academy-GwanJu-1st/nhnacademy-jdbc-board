package com.nhnacademy.service;

import com.nhnacademy.domain.Board;
import com.nhnacademy.domain.BoardRegisterRequest;
import com.nhnacademy.domain.User;
import com.nhnacademy.domain.UserVO;
import com.nhnacademy.exception.BoardNotFoundException;
import com.nhnacademy.mapper.BoardMapper;
import com.nhnacademy.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    public BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    public Board findById(long id) {
        existCheck(id);

        return boardMapper.findById(id);
    }

    @Override
    public List<Board> findAll(int amount, int skip) {
        return boardMapper.findAll(amount, skip);
    }

    @Override
    public int boardTotalCount() {
        return boardMapper.findTotalCount();
    }

    @Override
    public int nonDeleteBoardTotalCount() {
        return boardMapper.findTotalNotDeleteBoardCount();
    }

    @Override
    public int registerBoard(BoardRegisterRequest boardRegisterRequest) {
        return boardMapper.registerBoard(boardRegisterRequest);
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
    public int recoverBoard(long id) {
        return boardMapper.recoverBoard(id);
    }

    @Override
    public boolean allowedUserCheck(long id, UserVO userSession) {

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
    public List<Board> boardGetList(int amount, int skip) {
        return boardMapper.boardGetList(amount, skip);
    }

    @Override
    public Board findByIdContainDeletedBoard(long boardId) {
        return boardMapper.findById(boardId);
    }

    private void existCheck(long id) {
        if (boardMapper.exist(id) == 0) {
            throw new BoardNotFoundException();
        }
    }
}
