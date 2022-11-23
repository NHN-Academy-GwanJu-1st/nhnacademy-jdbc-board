package com.nhnacademy.domain;

import lombok.Value;

@Value
public class User {
    private final long id;
    private final String username;
    private final String password;

}
