package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GetOrderRes {
    private int order_idx;
    private String user_name;
    private String user_address;
    private String store_name;
    private String order_request_store;
    private String order_request_delivery;
    private int discount;
    private int total_price;
    private String payment_name;
    private Timestamp order_date;
    private List<OrderDetail> order_detail_list;
}
