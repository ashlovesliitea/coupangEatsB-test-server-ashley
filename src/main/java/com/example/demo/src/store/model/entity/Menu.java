package com.example.demo.src.store.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Menu {
    private int menu_idx;
    private String menu_name;
    private int menu_price;
    private String menu_detail;
    private String menu_img_url;
}
