package com.nhnacademy.domain;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.Date;

@Value
public class Board {
    private final long id;
    private final long userId;
    private final long modifierId;
    private final String title;
    private final String content;
    private final LocalDateTime regDate;
    private final LocalDateTime updateDate;
    private final char deleteYn;
}
