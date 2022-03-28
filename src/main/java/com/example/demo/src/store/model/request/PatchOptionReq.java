package com.example.demo.src.store.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PatchOptionReq {
    private String option_name;
    private int option_additional_price;
}
