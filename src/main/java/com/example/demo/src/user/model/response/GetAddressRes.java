package com.example.demo.src.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetAddressRes {
    private int user_address_idx;
    private String address;
}
