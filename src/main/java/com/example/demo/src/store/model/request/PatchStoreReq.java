package com.example.demo.src.store.model.request;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class PatchStoreReq {
    private String store_name;
    private Integer store_min_order;
    private String store_siNm;
    private String store_sggNm;
    private String store_emdNm;
    private String store_streetNm;
    private String store_detailNm;
    private String store_phone;
    private String store_owner;
    private String store_reg_num;
    private String store_buisness_hour;
    private String store_info;
    private String store_owner_note;
    private Integer store_delivery_fee;
    private double store_lng;
    private double store_lat;
    private Integer store_min_prep_time;
    private Integer store_max_prep_time;
    private List<Integer> category_list;
    private List<String>store_img_url;

}
