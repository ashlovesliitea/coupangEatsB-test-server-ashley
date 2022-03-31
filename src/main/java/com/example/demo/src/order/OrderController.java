package com.example.demo.src.order;

import com.example.demo.annotation.NoAuth;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.order.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/order")
public class OrderController {
     final Logger logger= LoggerFactory.getLogger(this.getClass());
     private final OrderProvider orderProvider;
    private final OrderService orderService;
    private final JwtService jwtService;

    @Autowired
    public OrderController(OrderProvider orderProvider, OrderService orderService, JwtService jwtService) {
        this.orderProvider = orderProvider;
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    /**
     * 유저의 주문 조회 API
     * [GET] /order?user-idx={userIdx}, /order
     * @return BaseResponse<GetOrderRes>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetOrderRes>> getUsersOrderList(@RequestParam(value = "user-idx",required = false,defaultValue = "0") Integer user_idx){

        if(user_idx!=0){
        List<GetOrderRes> usersOrderList=orderProvider.getUsersOrderList(user_idx);
        return new BaseResponse<>(usersOrderList);}
        else{
            List<GetOrderRes> wholeOrderList=orderProvider.getWholeOrderList();
            return new BaseResponse<>(wholeOrderList);
        }
    }

    /**
     * 주문 번호로 조회 API
     * [GET] /order/:orderIdx
     * @return BaseResponse<GetOrderRes>
     */
    @ResponseBody
    @GetMapping("/{orderIdx}")
    public BaseResponse<GetOrderRes> getOrder(@PathVariable("orderIdx")int orderIdx) throws BaseException{
        System.out.println("orderIdx = " + orderIdx);
        GetOrderRes getOrderRes=orderProvider.getOrder(orderIdx);
        return new BaseResponse<>(getOrderRes);

    }

    /**
     * 주문 전송 API
     * [POST] /order
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostOrderRes> createOrder(@RequestBody PostOrderReq postOrderReq){
        int createOrderCheck=orderService.createOrder(postOrderReq);
        if(createOrderCheck!=0){
        PostOrderRes postOrderRes=new PostOrderRes(createOrderCheck);
        return new BaseResponse<>(postOrderRes);}
        else{
          return new BaseResponse<>(BaseResponseStatus.FAIL_TO_CREATE_ORDER);
        }
    }

    /**
     * 리뷰 전송 API
     * [POST] /order/{orderIdx}/review
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{orderIdx}/review")
    public BaseResponse<String> createReview(@PathVariable("orderIdx") int order_idx,
                                             @RequestBody PostReviewReq postReviewReq){

        int createReviewCheck=orderService.createReview(postReviewReq);
        if(createReviewCheck!=0){
            String result="";
            return new BaseResponse<>(result);
        }
        else{
            return new BaseResponse<>(BaseResponseStatus.FAIL_TO_CREATE_REVIEW);
        }

    }

    /**
     * 리뷰 수정 API
     * [PATCH] /order/{orderIdx}/review/{reviewIdx}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{orderIdx}/review/{reviewIdx}")
    public BaseResponse<String> modifyReview(@PathVariable("orderIdx") int order_idx,
                                             @PathVariable("reviewIdx") int review_idx,
                                             @RequestBody PatchReviewReq patchReviewReq){


        int modifyReviewCheck=orderService.modifyReview(review_idx,patchReviewReq);
        if(modifyReviewCheck!=0){
            String result="";
            return new BaseResponse<>(result);
        }
        else{
            return new BaseResponse<>(BaseResponseStatus.FAIL_TO_MODIFY_REVIEW);
        }
    }

    /**
     * 리뷰 삭제 API
     * [PATCH] /order/{orderIdx}/review/{reviewIdx}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{orderIdx}/review/{reviewIdx}")
    public BaseResponse<String> deleteReview(@PathVariable("orderIdx") int order_idx,
                                             @PathVariable("reviewIdx") int review_idx) {

        int deleteReviewCheck = orderService.deleteReview(review_idx);
        if (deleteReviewCheck != 0) {
            String result = "";
            return new BaseResponse<>(result);
        } else {
            return new BaseResponse<>(BaseResponseStatus.FAIL_TO_DELETE_REVIEW);
        }
    }

    /**
     * 리뷰 인덱스로 조회 API
     * [GET] /review/{reviewIdx}
     * @return BaseResponse<Review>
     */
    @ResponseBody
    @GetMapping("/review/{reviewIdx}")
    public BaseResponse<Review> getReviewByIdx(@PathVariable("reviewIdx") int review_idx){
        Review review=orderProvider.getReviewByIdx(review_idx);
        return new BaseResponse<>(review);
    }

    /**
     * 리뷰 전체 조회 API
     * [GET] /review
     * @return BaseResponse<List<Review>>
     */
    @ResponseBody
    @GetMapping("/review")
    public BaseResponse<List<Review>> getReviewByIdx(){
        List<Review> reviewList=orderProvider.getWholeReview();
        return new BaseResponse<>(reviewList);
    }



}
