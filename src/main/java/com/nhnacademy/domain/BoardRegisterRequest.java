package com.nhnacademy.domain;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class BoardRegisterRequest {

    @NotNull
    private final String userName;

    @Size(min = 2, max = 200)
    private final String title;

    @Size(max = 40000)
    private final String content;
}
