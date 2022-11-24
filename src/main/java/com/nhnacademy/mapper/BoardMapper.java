package com.nhnacademy.mapper;

import com.nhnacademy.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    int exist(long id);

    Board findById(long id);

    int registerBoard(@Param("userName") String userName, @Param("title") String title, @Param("content") String content);

    int modifyBoard(@Param("id") long id, @Param("modifierName") String modifierName, @Param("title") String title, @Param("content") String content);

    int deleteBoard(long id);

    int findTotalCount();

    List<Board> boardGetList(@Param("amount") int amount, @Param("skip") int skip);

    int increaseCommentCnt(long id);

}
