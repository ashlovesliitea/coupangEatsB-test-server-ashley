package com.example.demo.src.store.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReviewRatingNum {
    private float review_avg_rating;
    private int review_num;
}
