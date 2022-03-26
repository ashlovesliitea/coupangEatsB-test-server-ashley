package com.example.demo.src.order;

import com.example.demo.annotation.NoAuth;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}