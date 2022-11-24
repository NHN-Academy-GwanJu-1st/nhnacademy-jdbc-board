package com.nhnacademy.service;

import com.nhnacademy.domain.Comment;
import com.nhnacademy.mapper.BoardMapper;
import com.nhnacademy.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public List<Comment> findByBoardId(long boardId) {
        if (boardCommentExist(boardId) == 0) {
            return Collections.emptyList();
        }

        return commentMapper.findByBoardId(boardId);
    }

    @Override
    public int registerComment(long boardId, String userName, String content) {
        boardMapper.increaseCommentCnt(boardId);


        return commentMapper.registerComment(boardId, userName, content);
    }

    @Override
    public int modifyComment(long id, String content, String modifierName){
        return commentMapper.modifyComment(id, content, modifierName);
    }

    @Override
    public int deleteComment(long id) {
        return commentMapper.deleteComment(id);
    }

    @Override
    public int findByBoardIdTotalCount(long boardId) {
        return commentMapper.findByBoardIdTotalCount(boardId);
    }
}
