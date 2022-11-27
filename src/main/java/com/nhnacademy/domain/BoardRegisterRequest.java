package com.nhnacademy.domain;

import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BoardRegisterRequest {

    private int id;

    @NotNull
    private String userName;

    @Size(min = 2, max = 200)
    private String title;

    @Size(max = 40000)
    private String content;

    public BoardRegisterRequest(String userName, String title, String content) {
        this.id = 0;
        this.userName = userName;
        this.title = title;
        this.content = content;
    }
}
