package com.example.demo.src.store;

import com.example.demo.src.store.model.response.GetMenuRes;
import com.example.demo.src.store.model.response.GetStoreRes;
import com.example.demo.src.store.model.entity.Review;
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

    public List<GetStoreRes> getStoresByCategory(int user_address_idx,String category){
        List<GetStoreRes> getStoreListByCategory=storeDao.getStoresByCategory(user_address_idx,category);
        return getStoreListByCategory;
    }

    public List<GetStoreRes> getEveryStoreList(int user_address_idx) {
        List<GetStoreRes> getEveryStoreList=storeDao.getEveryStoreList(user_address_idx);
        return getEveryStoreList;
    }

    public List<GetStoreRes> getNewestStoreList(int user_address_idx) {
        List<GetStoreRes> newestStoreList=storeDao.getNewestStoreList(user_address_idx);
        return newestStoreList;
    }

    public GetStoreRes getStoreInfo(int user_address_idx, int store_idx) {
        GetStoreRes getStoreRes=storeDao.getStoreInfo(user_address_idx,store_idx);
        return getStoreRes;
    }

    public List<GetStoreRes> getStoresByKeyword(int user_address_idx, String search_query) {
        List<GetStoreRes> storeListByKeyword=storeDao.getstoresByKeyword(user_address_idx,search_query);
        return storeListByKeyword;
    }

    public GetMenuRes getMenuAndOption(int menu_idx) {
        GetMenuRes getMenuRes=storeDao.getMenuAndOption(menu_idx);
        return getMenuRes;
    }

    public List<Review> getReviewFromStores(int store_idx) {
        List<Review> review =storeDao.getReviewFromStores(store_idx);
        return review;
    }
}
