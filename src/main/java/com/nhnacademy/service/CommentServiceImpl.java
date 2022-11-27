package com.nhnacademy.service;

import com.nhnacademy.domain.Board;
import com.nhnacademy.domain.Comment;
import com.nhnacademy.domain.CommentRegisterRequest;
import com.nhnacademy.domain.User;
import com.nhnacademy.exception.BoardNotFoundException;
import com.nhnacademy.mapper.BoardMapper;
import com.nhnacademy.mapper.CommentMapper;
import com.nhnacademy.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional
@Service
public class CommentServiceImpl implements CommentService{

    private final CommentMapper commentMapper;
    private final BoardMapper boardMapper;

    public CommentServiceImpl(CommentMapper commentMapper, BoardMapper boardMapper) {
        this.commentMapper = commentMapper;
        this.boardMapper = boardMapper;
    }

    @Override
    public int boardCommentExist(long boardId) {
        return commentMapper.boardCommentExist(boardId);
    }

    @Override
    public Comment findById(long commentId) {
        return commentMapper.findById(commentId);
    }

    @Override
    public List<Comment> findByBoardId(long boardId) {
        if (boardCommentExist(boardId) == 0) {
            return Collections.emptyList();
        }

        return commentMapper.findByBoardId(boardId);
    }

    @Override
    public int registerComment(CommentRegisterRequest commentRegisterRequest) {
        long boardId = commentRegisterRequest.getBoardId();
        if (boardMapper.exist(boardId) == 0) {
            throw new BoardNotFoundException();
        }

        boardMapper.increaseCommentCnt(boardId);
        return commentMapper.registerComment(commentRegisterRequest);
    }

    @Override
    public int modifyComment(long id, CommentRegisterRequest commentRequest) {


        return commentMapper.modifyComment(id, commentRequest.getContent());
    }

    @Override
    public int deleteComment(long id) {
        Comment comment = commentMapper.findById(id);
        boardMapper.decreaseComment(comment.getBoardId());

        return commentMapper.deleteComment(id);
    }

}
