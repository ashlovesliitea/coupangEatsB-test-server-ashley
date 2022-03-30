package com.example.demo.src.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserReq {
    private int user_idx;
    private String user_name;
}
