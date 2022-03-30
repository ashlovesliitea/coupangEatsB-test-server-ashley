package com.example.demo.src.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginReq {
    private String user_id;
    private String user_pw;
}
