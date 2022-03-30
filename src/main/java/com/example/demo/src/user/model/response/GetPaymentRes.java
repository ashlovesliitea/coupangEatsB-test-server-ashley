package com.example.demo.src.user.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetPaymentRes {
    private int payment_idx;
    private String payment_name;
}
