package com.example.demo.src.store;

import com.example.demo.annotation.NoAuth;
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

    @NoAuth
    @ResponseBody
    @GetMapping("")
    public List<GetStoreRes> getStoreList(@RequestParam(required=false)String category){

            List<GetStoreRes> getStoreListByCategory=storeProvider.getStoresByCategory(category);
            return getStoreListByCategory;
        }
        /*
        else if(q!=null){

        }
        else{

        }*/

}
