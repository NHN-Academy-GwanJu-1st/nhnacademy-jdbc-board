package com.nhnacademy.service;

import com.nhnacademy.domain.Board;
import com.nhnacademy.exception.BoardNotFoundException;
import com.nhnacademy.mapper.BoardMapper;
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
    public List<Board> findAll() {
        return boardMapper.findAll();
    }

    @Override
    public Board findById(long id) {
        if (boardMapper.exist(id) == 0) {
            throw new BoardNotFoundException();
        }

        return boardMapper.findById(id);
    }

    @Override
    public int registerBoard(long userId, String title, String content) {
        return boardMapper.registerBoard(userId, title, content);
    }

    @Override
    public int modifyBoard(long id, long modifierId, String title, String content, LocalDateTime updateDate) {
        return boardMapper.modifyBoard(id, modifierId, title, content, updateDate);
    }

    @Override
    public int deleteBoard(long id) {
        return boardMapper.deleteBoard(id);
    }
}
