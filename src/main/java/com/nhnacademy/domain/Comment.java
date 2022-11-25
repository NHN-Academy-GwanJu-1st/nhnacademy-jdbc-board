package com.nhnacademy.domain;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class Comment {

    private final long id;
    private final long boardId;
    private final String userName;
    private final String content;
    private final LocalDateTime regDate;
    private final LocalDateTime updateDate;
    private final String modifyCheck;

}
