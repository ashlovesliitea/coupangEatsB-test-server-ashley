package com.example.demo.src.store;

import com.example.demo.src.store.model.request.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public int modifyStoreInfo(int store_idx, PatchStoreReq patchStoreReq) {
        return storeDao.modifyStoreInfo(store_idx,patchStoreReq);

    }

    public int modifyMenu(int menu_idx, PatchMenuReq patchMenuReq){

       return storeDao.modifyMenu(menu_idx,patchMenuReq);

    }

    public int modifyOption(int option_idx, PatchOptionReq patchOptionReq) {
        return storeDao.modifyOption(option_idx,patchOptionReq);
    }

    public int createUserLikedStore(PostLikedReq postLikedReq) {
        return storeDao.createUserLikedStore(postLikedReq);
    }

    public void deleteUserLikedStore(int liked_idx) {
        storeDao.deleteUserLikedStore(liked_idx);
    }

    public int deleteStoreInfo(int store_idx) {
        return storeDao.deleteStoreInfo(store_idx);
    }

    public int deleteMenu(int menu_idx) {
        return storeDao.deleteMenu(menu_idx);
    }

    public int deleteOption(int option_idx) {
        return storeDao.deleteOption(option_idx);
    }
}
