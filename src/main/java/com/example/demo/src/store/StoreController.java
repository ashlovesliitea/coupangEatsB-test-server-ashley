package com.example.demo.src.store;

import com.example.demo.annotation.NoAuth;
import com.example.demo.src.store.model.GetStoreReq;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.utils.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/stores")
public class StoreController{

    private StoreProvider storeProvider;
    private StoreService storeService;
    private JwtService jwtService;

    public StoreController(StoreProvider storeProvider, StoreService storeService, JwtService jwtService) {
        this.storeProvider = storeProvider;
        this.storeService = storeService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("")
    public List<GetStoreRes> getStoreList(@RequestParam(value="user-address-idx",required=false) int user_address_idx
                                        ,@RequestParam(value="category",required=false) String category
                                        ,@RequestParam(value="q",required = false)String search_query){
           if(category!=null){
            List<GetStoreRes> getStoreListByCategory=storeProvider.getStoresByCategory(user_address_idx,category);
            return getStoreListByCategory;}
           else if(search_query!=null){
               System.out.println("search_query = " + search_query);
            }
           //user-address-idx만 들어왔을 때
               List<GetStoreRes>  everyStoreList=storeProvider.getEveryStoreList(user_address_idx);
               return everyStoreList;



        }


    @ResponseBody
    @GetMapping("/newest")
    public List<GetStoreRes> getNewestStoreList(@RequestParam(value="user-address-idx",required=false) int user_address_idx){
       List<GetStoreRes> newestStoreList=storeProvider.getNewestStoreList(user_address_idx);
       return newestStoreList;
    }

    @ResponseBody
    @GetMapping("/{storeIdx}")
    public GetStoreRes getStoreInfo(@RequestParam(value="user-address-idx",required=false) int user_address_idx
                                    ,@PathVariable("storeIdx")int store_idx){
        GetStoreRes storeInfo=storeProvider.getStoreInfo(user_address_idx,store_idx);
        return storeInfo;
    }
}
