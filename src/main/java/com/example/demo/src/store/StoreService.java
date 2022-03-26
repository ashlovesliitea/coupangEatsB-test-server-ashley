package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.store.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;

@Service
public class StoreService {

    private StoreDao storeDao;
    private StoreProvider storeProvider;
    private JwtService jwtService;

    @Autowired
    public StoreService(StoreDao storeDao, StoreProvider storeProvider, JwtService jwtService) {
        this.storeDao = storeDao;
        this.storeProvider = storeProvider;
        this.jwtService=jwtService;
    }

    @Transactional
    public int createStore(PostStoreReq postStoreReq) {
        int createStoreCheck=storeDao.createStore(postStoreReq);
        return createStoreCheck;
    }

    public int createMenuCategory(int store_idx, PostMenuCategoryReq postMenuCategoryReq){
        int createMenuCateCheck=storeDao.createMenuCategory(store_idx,postMenuCategoryReq);
      return createMenuCateCheck;
    }

    @Transactional
    public int createMenu(int store_idx, PostMenuReq postMenuReq) {
        int createMenuCheck=storeDao.createMenu(store_idx,postMenuReq);
        return createMenuCheck;
    }

    public int createOption(int menu_idx, PostOptionReq postOptionReq) {
        int createOptionCheck=storeDao.createOption(menu_idx,postOptionReq);
        return createOptionCheck;
    }
}
