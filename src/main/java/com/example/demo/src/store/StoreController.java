package com.example.demo.src.store;

import com.example.demo.annotation.NoAuth;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.store.model.entity.Review;
import com.example.demo.src.store.model.request.*;
import com.example.demo.src.store.model.response.GetMenuRes;
import com.example.demo.src.store.model.response.GetReviewRes;
import com.example.demo.src.store.model.response.GetStoreRes;
import com.example.demo.utils.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public BaseResponse<List<GetStoreRes>> getStoreList(@RequestParam(value="user-address-idx",required=false) int user_address_idx
                                        , @RequestParam(value="category",required=false) String category
                                        , @RequestParam(value="q",required = false)String search_query){
           if(category!=null){
               System.out.println("category = " + category);
            List<GetStoreRes> getStoreListByCategory=storeProvider.getStoresByCategory(user_address_idx,category);
            return new BaseResponse<>(getStoreListByCategory);}
           else if(search_query!=null){
               System.out.println("search_query = " + search_query);
               List<GetStoreRes> getStoreListByKeyword=storeProvider.getStoresByKeyword(user_address_idx,search_query);
               return new BaseResponse<>(getStoreListByKeyword);
            }
           //user-address-idx만 들어왔을 때
               List<GetStoreRes>  everyStoreList=storeProvider.getEveryStoreList(user_address_idx);
               return new BaseResponse<>(everyStoreList);

        }


    @ResponseBody
    @GetMapping("/newest")
    public BaseResponse<List<GetStoreRes>> getNewestStoreList(@RequestParam(value="user-address-idx",required=false) int user_address_idx){
       List<GetStoreRes> newestStoreList=storeProvider.getNewestStoreList(user_address_idx);
       return new BaseResponse<>(newestStoreList);
    }

    @ResponseBody
    @GetMapping("/{storeIdx}")
    public BaseResponse<GetStoreRes> getStoreInfo(@RequestParam(value="user-address-idx",required=false) int user_address_idx
                                    ,@PathVariable("storeIdx")int store_idx){
        GetStoreRes storeInfo=storeProvider.getStoreInfo(user_address_idx,store_idx);
        return new BaseResponse<>(storeInfo);
    }

    @ResponseBody
    @GetMapping("/{storeIdx}/menu/{menuIdx}")
    public BaseResponse<GetMenuRes> getMenu(@PathVariable("storeIdx")int store_idx, @PathVariable("menuIdx")int menu_idx){
       GetMenuRes getMenuRes=storeProvider.getMenuAndOption(menu_idx);
        return new BaseResponse<>(getMenuRes);
    }

    @ResponseBody
    @GetMapping("{storeIdx}/reviews")
    public BaseResponse<List<GetReviewRes>> getReviewFromStores(@PathVariable("storeIdx")int store_idx){
       List<Review> reviewList=storeProvider.getReviewFromStores(store_idx);
       List<GetReviewRes> getReviewResList=new ArrayList<>();
       for(Review review :reviewList){
           GetReviewRes getReviewRes=new GetReviewRes(review);
           getReviewResList.add(getReviewRes);
       }
       return new BaseResponse<>(getReviewResList);
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createStore(@RequestBody PostStoreReq postStoreReq){
       int createStoreCheck=storeService.createStore(postStoreReq);
       if(createStoreCheck!=0){
           String result="";
           return new BaseResponse<>(result);
       }
       else{
           return new BaseResponse<>(BaseResponseStatus.FAIL_TO_CREATE_STORE);
       }

    }

    @NoAuth
    @ResponseBody
    @PostMapping("/{storeIdx}/menu-category")
    public BaseResponse<String> createMenuCategory(@PathVariable("storeIdx") int store_idx
            ,@RequestBody PostMenuCategoryReq postMenuCategoryReq){
        System.out.print(store_idx);
        System.out.println("menu_category_name = " + postMenuCategoryReq.getMenu_category_name());
        int menuCateCheck=storeService.createMenuCategory(store_idx,postMenuCategoryReq);

        if(menuCateCheck!=0){
            String Result="";
            return new BaseResponse<>(Result);}
        else{
            return new BaseResponse<>(BaseResponseStatus.FAIL_TO_CREATE_MENU_CATEGORY);
        }
    }

    @ResponseBody
    @PostMapping("/{storeIdx}/menu")
    public BaseResponse<String> createMenu(@PathVariable("storeIdx")int store_idx,@RequestBody PostMenuReq postMenuReq){
        int menuCateCheck=storeService.createMenu(store_idx,postMenuReq);

        if(menuCateCheck!=0){
            String Result="";
            return new BaseResponse<>(Result);}
        else{
            return new BaseResponse<>(BaseResponseStatus.FAIL_TO_CREATE_MENU);
        }
    }

    @ResponseBody
    @PostMapping("/{storeIdx}/menu/{menuIdx}/option")
    public BaseResponse<String> createOption(@PathVariable("storeIdx")int store_idx,@PathVariable("menuIdx")int menu_idx,@RequestBody PostOptionReq postOptionReq){
        int optionCheck=storeService.createOption(menu_idx,postOptionReq);

        if(optionCheck!=0){
            String Result="";
            return new BaseResponse<>(Result);}
        else{
            return new BaseResponse<>(BaseResponseStatus.FAIL_TO_CREATE_OPTION);
        }
    }

    //상점 정보 수정 API
    @ResponseBody
    @PatchMapping("/{storeIdx}")
    public BaseResponse<String> modifyStoreInfo(@PathVariable("storeIdx")int store_idx, @RequestBody PatchStoreReq patchStoreReq){
        storeService.modifyStoreInfo(store_idx,patchStoreReq);
        String Result="";
        return new BaseResponse<>(Result);
    }

    //메뉴 수정 API
    @ResponseBody
    @PatchMapping("/{storeIdx}/menu/{menuIdx}")
    public BaseResponse<String> modifyMenu(@PathVariable("storeIdx") int store_idx, @PathVariable("menuIdx") int menu_idx, @RequestBody PatchMenuReq patchMenuReq){
        storeService.modifyMenu(menu_idx,patchMenuReq);
        String Result="";
        return new BaseResponse<>(Result);
    }

    //옵션 수정 API
    @ResponseBody
    @PatchMapping("/{storeIdx}/menu/{menuIdx}/option/{optionIdx}")
    public BaseResponse<String> modifyOption(@PathVariable("storeIdx") int store_idx, @PathVariable("menuIdx") int menu_idx
            ,@PathVariable("optionIdx") int option_idx, @RequestBody PatchOptionReq patchOptionReq){

        storeService.modifyOption(option_idx,patchOptionReq);
        String Result="";
        return new BaseResponse<>(Result);
    }
}
