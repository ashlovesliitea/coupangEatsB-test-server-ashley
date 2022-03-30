package com.example.demo.src.user.model.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class PostAddressReq {
    private int user_idx;
    private String siNm;
    private String sggNm;
    private String emdNm;
    private String streetNm;
    private String detailNm;
    private double user_address_lng;
    private double user_address_lat;
}
