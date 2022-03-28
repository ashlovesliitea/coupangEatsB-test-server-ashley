package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class Details {
    private int menu_idx;
    private int menu_amount;
    private List<Integer> option_list;
}
