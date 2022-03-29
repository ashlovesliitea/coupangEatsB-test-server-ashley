package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Review {
    private int review_idx;
    private int user_idx;
    private String user_name;
    private int store_idx;
    private String store_name;
    private int order_idx;
    private int ratings;
    private String review_comment;
    private int reviewImgExists;
    private List<String> review_img_list;
    private List<String> menu_name;
}
