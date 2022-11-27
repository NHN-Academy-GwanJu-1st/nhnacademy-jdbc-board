package com.nhnacademy.service;

import com.nhnacademy.domain.FileDAO;
import com.nhnacademy.mapper.FileMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    public FileServiceImpl(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public List<FileDAO> findByBoardId(long boardId) {
        return fileMapper.findByBoardId(boardId);
    }

    @Override
    public int insertFile(long boardId, String userName, String fileName) {
        return fileMapper.insertFile(boardId, userName, fileName);
    }
}
