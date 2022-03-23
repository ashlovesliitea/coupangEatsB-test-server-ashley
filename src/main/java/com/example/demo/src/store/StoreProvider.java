package com.example.demo.src.store;

import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreProvider {

    private StoreDao storeDao;
    private JwtService jwtService;

    @Autowired
    public StoreProvider(StoreDao storeDao, JwtService jwtService) {
        this.storeDao = storeDao;
        this.jwtService = jwtService;
    }

    public List<GetStoreRes> getStoresByCategory(String category){
        List<GetStoreRes> getStoreListByCategory=storeDao.getStoresByCategory(category);
        return getStoreListByCategory;
    }
}
