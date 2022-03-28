package com.example.demo.src.store.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StoreLocation {
    private int store_idx;
    private double store_lng;
    private double store_lat;
}
