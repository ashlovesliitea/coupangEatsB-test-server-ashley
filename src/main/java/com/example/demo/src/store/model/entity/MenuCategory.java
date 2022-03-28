package com.example.demo.src.store.model.entity;

import com.example.demo.src.store.model.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MenuCategory {
   private String menuCategoryName;
   private List<Menu> menuList;
}
