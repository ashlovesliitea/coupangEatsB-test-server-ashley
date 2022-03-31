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
    public BaseResponse<List<GetStoreRes>> getStoreList(@RequestParam(value="user-address-idx",required=false) Integer user_address_idx
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
    public BaseResponse<String> modifyStoreInfo(@PathVariable("storeIdx")int store_idx,
                                                @RequestBody PatchStoreReq patchStoreReq){

            int modifyCheck=storeService.modifyStoreInfo(store_idx,patchStoreReq);
            if(modifyCheck!=0){
                String Result="";
                return new BaseResponse<>(Result);}
            else return new BaseResponse<>(BaseResponseStatus.FAIL_TO_MODIFY_STORE);

    }


    @ResponseBody
    @PostMapping("/{storeIdx}")
    public BaseResponse<String> deleteStoreInfo(@PathVariable("storeIdx")int store_idx) {

            int deleteCheck=storeService.deleteStoreInfo(store_idx);
            if(deleteCheck!=0){
                String Result="";
                return new BaseResponse<>(Result);
            }
            else return new BaseResponse<>(BaseResponseStatus.FAIL_TO_DELETE_STORE);

    }

    //메뉴 수정 API
    @ResponseBody
    @PatchMapping("/{storeIdx}/menu/{menuIdx}")
    public BaseResponse<String> modifyMenu(@PathVariable("storeIdx") int store_idx,
                                           @PathVariable("menuIdx") int menu_idx,
                                           @RequestBody PatchMenuReq patchMenuReq){

            int modifyCheck=storeService.modifyMenu(menu_idx,patchMenuReq);
            if(modifyCheck!=0){
                String Result="";
                return new BaseResponse<>(Result);}
            else return new BaseResponse<>(BaseResponseStatus.FAIL_TO_MODIFY_MENU);

    }

    @ResponseBody
    @PostMapping("/{storeIdx}/menu/{menuIdx}")
    public BaseResponse<String> deleteMenu(@PathVariable("storeIdx") int store_idx,
                                           @PathVariable("menuIdx") int menu_idx) {


            int deleteCheck = storeService.deleteMenu(menu_idx);
            if (deleteCheck != 0) {
                String Result = "";
                return new BaseResponse<>(Result);
            }
            else return new BaseResponse<>(BaseResponseStatus.FAIL_TO_DELETE_MENU);

    }

    //옵션 수정 API
    @ResponseBody
    @PatchMapping("/{storeIdx}/menu/{menuIdx}/option/{optionIdx}")
    public BaseResponse<String> modifyOption(@PathVariable("storeIdx") int store_idx, @PathVariable("menuIdx") int menu_idx
            ,@PathVariable("optionIdx") int option_idx
            , @RequestBody PatchOptionReq patchOptionReq){

            int modifyCheck=storeService.modifyOption(menu_idx,patchOptionReq);
            if(modifyCheck!=0){
                String Result="";
                return new BaseResponse<>(Result);}
            else return new BaseResponse<>(BaseResponseStatus.FAIL_TO_MODIFY_OPTION);

    }

    @ResponseBody
    @PostMapping("/{storeIdx}/menu/{menuIdx}/option/{optionIdx}")
    public BaseResponse<String> modifyOption(@PathVariable("storeIdx") int store_idx, @PathVariable("menuIdx") int menu_idx
            ,@PathVariable("optionIdx") int option_idx
            ){

            int deleteCheck=storeService.deleteOption(option_idx);
            if(deleteCheck!=0){
                String Result="";
                return new BaseResponse<>(Result);
            }
            else return new BaseResponse<>(BaseResponseStatus.FAIL_TO_DELETE_OPTION);

    }

    @ResponseBody
    @GetMapping("/liked")
    public BaseResponse<List<GetLikedRes>> getUserLikedList(@RequestParam(value = "user-idx",required = false)int userIdx
                                                             ,@RequestParam(value = "user-address-idx",required = false)int userAddressIdx){
        List<GetLikedRes> getStoreResList=storeProvider.getUserLikedList(userIdx,userAddressIdx);
        return new BaseResponse<>(getStoreResList);
    }


    @ResponseBody
    @PostMapping("/liked")
    public BaseResponse<String> createUserLikedStore(@RequestBody PostLikedReq postLikedReq){
        int createLikedCheck= storeService.createUserLikedStore(postLikedReq);

        if(createLikedCheck!=0){
            String result="";
        return new BaseResponse<>(result);
        }
        else{
            return new BaseResponse<>(BaseResponseStatus.FAIL_TO_CREATE_LIKED);
        }
    }

    @ResponseBody
    @PatchMapping("/liked")
    public BaseResponse<String> deleteUserLikedStore(@RequestParam(value = "liked-idx",required = false)int liked_idx){
         storeService.deleteUserLikedStore(liked_idx);
        String result="";
        return new BaseResponse<>(result);

    }


}
