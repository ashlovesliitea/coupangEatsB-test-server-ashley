package com.example.demo.src.store.model.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PatchMenuReq {
    private String menu_name;
    private int menu_price;
    private String menu_details;
    private List<Integer> menu_cate_list;
}
