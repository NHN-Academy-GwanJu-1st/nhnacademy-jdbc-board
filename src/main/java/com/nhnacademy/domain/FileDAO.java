package com.nhnacademy.domain;

import lombok.Value;

@Value
public class FileDAO {

    private final long id;
    private final long boardId;
    private final String userName;
    private final String fileName;

}
