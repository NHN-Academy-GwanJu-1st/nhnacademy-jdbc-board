package com.nhnacademy.service;

import com.nhnacademy.mapper.HeartMapper;
import org.springframework.stereotype.Service;

@Service
public class HeartServiceImpl implements HeartService {

    private final HeartMapper heartMapper;

    public HeartServiceImpl(HeartMapper heartMapper) {
        this.heartMapper = heartMapper;
    }

    @Override
    public boolean findByBoardIdAndUserName(long boardId, String userName) {

        int result = heartMapper.findByBoardIdAndUserName(boardId, userName);

        if (result == 0) {
            return false;
        }

        return true;
    }

    @Override
    public int insertHeart(long boardId, String userName) {
        return heartMapper.insertHeart(boardId, userName);
    }

    @Override
    public int deleteHeart(long boardId, String userName) {
        return heartMapper.deleteHeart(boardId, userName);
    }
}
