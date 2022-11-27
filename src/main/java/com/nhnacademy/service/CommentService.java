package com.nhnacademy.service;

import com.nhnacademy.domain.Comment;
import com.nhnacademy.domain.CommentRegisterRequest;

import java.util.List;

public interface CommentService {

    int boardCommentExist(long boardId);

    Comment findById(long commentId);

    List<Comment> findByBoardId(long boardId);

    int registerComment(CommentRegisterRequest commentRegisterRequest);

    int modifyComment(long id, CommentRegisterRequest commentRegisterRequest);

    int deleteComment(long id);

}
