package com.example.demo.src.store.model.response;

import com.example.demo.src.store.model.entity.Menu;
import com.example.demo.src.store.model.entity.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GetMenuRes {
    private Menu menu;
    private List<Option> optionList;
}
