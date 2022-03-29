package com.example.demo.src.store.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostLikedReq {
    private int user_idx;
    private int store_idx;
}
