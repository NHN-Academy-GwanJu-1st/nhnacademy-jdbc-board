package com.nhnacademy.service;

import com.nhnacademy.domain.FileDAO;

import java.util.List;

public interface FileService {

    List<FileDAO> findByBoardId(long boardId);

    int insertFile(long boardId, String userName, String fileName);

    int deleteFileByBoardId(long boardId);

}
