package com.nhnacademy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HeartMapper {

    int findByBoardIdAndUserName(@Param("boardId") long boardId, @Param("userName") String userName);

    int insertHeart(@Param("boardId") long boardId, @Param("userName") String userName);

    int deleteHeart(@Param("boardId") long boardId, @Param("userName") String userName);

}
