package com.nhnacademy.domain;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class CommentRegisterRequest {

    @NotNull
    private final long boardId;

    @NotNull
    private final String userName;

    @NotBlank
    private final String content;

}
