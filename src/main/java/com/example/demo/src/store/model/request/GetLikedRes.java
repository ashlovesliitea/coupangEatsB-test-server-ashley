package com.example.demo.src.store.model.request;

import com.example.demo.src.store.model.response.GetStoreRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetLikedRes {
    private int liked_idx;
    private GetStoreRes liked_store_info;
}
