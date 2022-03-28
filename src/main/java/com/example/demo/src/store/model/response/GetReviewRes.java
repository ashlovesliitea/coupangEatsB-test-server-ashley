package com.example.demo.src.store.model.response;

import com.example.demo.src.store.model.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetReviewRes {
    private Review review;
}
