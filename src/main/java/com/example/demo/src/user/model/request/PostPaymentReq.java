package com.example.demo.src.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PostPaymentReq {
    private int user_idx;
    private String payment_name;
}
