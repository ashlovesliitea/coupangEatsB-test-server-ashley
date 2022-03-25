package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Option {
    private int option_idx;
    private String option_name;
    private int option_additional_price;
}
