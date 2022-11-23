package com.nhnacademy.mapper;

import com.nhnacademy.domain.Board;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BoardMapper {

    int exist(long id);

    List<Board> findAll();

    Board findById(long id);

    int registerBoard(long userId, String title, String content);

    int modifyBoard(long id, long modifierId, String title, String content, LocalDateTime updateDate);

    int deleteBoard(long id);

}
