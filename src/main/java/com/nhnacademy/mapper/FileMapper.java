package com.nhnacademy.mapper;

import com.nhnacademy.domain.FileDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper {

    List<FileDAO> findByBoardId(long boardId);

    int insertFile(@Param("boardId") long boardId, @Param("userName") String userName, @Param("fileName") String fileName);

    int deleteFileByBoardId(long boardId);
}
