package com.example.demo.src.order;

import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.Review;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProvider {
    final Logger logger= LoggerFactory.getLogger(this.getClass());

    private final OrderDao orderDao;
    private final JwtService jwtService;

    @Autowired
    public OrderProvider(OrderDao orderDao,JwtService jwtService) {
        this.orderDao=orderDao;
        this.jwtService=jwtService;
    }

    public GetOrderRes getOrder(int orderIdx){
        GetOrderRes getOrderRes=orderDao.getOrder(orderIdx);
        return getOrderRes;
    }

    public Review getReviewByIdx(int review_idx) {
        return orderDao.getReviewByIdx(review_idx);
    }

    public List<Review> getWholeReview() {
        return orderDao.getWholeReviewList();
    }

    public List<GetOrderRes> getWholeOrderList() {
        return orderDao.getWholeOrderList();
    }

    public List<GetOrderRes> getUsersOrderList(int user_idx) {
        return orderDao.getUsersOrderList(user_idx);
    }
}
