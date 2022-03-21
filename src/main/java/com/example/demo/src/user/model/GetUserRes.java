package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int user_idx;
    private String user_id;
    private String user_pw;
    private String user_name;
    private String user_phone;
}
