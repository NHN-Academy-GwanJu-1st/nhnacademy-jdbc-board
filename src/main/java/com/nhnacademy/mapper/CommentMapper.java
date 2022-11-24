package com.nhnacademy.mapper;

import com.nhnacademy.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    int boardCommentExist(long boardId);

    List<Comment> findByBoardId(long boardId);

    int registerComment(long boardId, String userName, String content);

    int modifyComment(long id, String content, String modifierName);

    int deleteComment(long id);

    int findByBoardIdTotalCount(long boardId);

}
