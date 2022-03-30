package com.example.demo.src.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserRes {
    private int userIdx;
    private String user_name;
    private String jwt;
}
