package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PostReviewReq {
    private int user_idx;
    private int store_idx;
    private int order_idx;
    private int ratings;
    private String review_comment;
    private List<String> review_img_url_list;
}
