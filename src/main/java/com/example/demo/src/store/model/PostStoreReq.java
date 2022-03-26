package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PostStoreReq {
    private String store_name;
    private int store_min_order;
    private String store_siNm;
    private String store_sggNm;
    private String store_emdNm;
    private String store_streetNm;
    private String store_detailNm;
    private int store_phone;
    private String store_owner;
    private String store_reg_num;
    private String store_buisness_hour;
    private String store_info;
    private String store_owner_note;
    private int store_delivery_fee;
    private double store_lng;
    private double store_lat;
    private int store_min_prep_time;
    private int store_max_prep_time;
    private List<Integer> category_list;
    private List<String>store_img_url;
}
