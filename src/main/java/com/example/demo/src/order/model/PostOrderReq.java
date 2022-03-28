package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostOrderReq {
    private int user_idx;
    private int user_address_idx;
    private int store_idx;
    private String order_request_store;
    private String order_request_delivery;
    private int discount;
    private int payment_idx;
    private List<Details> order_detail_list;
}
