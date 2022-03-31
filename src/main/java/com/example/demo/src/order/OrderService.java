package com.example.demo.src.order;

import com.example.demo.src.order.model.PatchReviewReq;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostReviewReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OrderService {
    final Logger logger= LoggerFactory.getLogger(this.getClass());

    private final OrderDao orderDao;
    private final OrderProvider orderProvider;
    private final JwtService jwtService;

    @Autowired
    public OrderService(OrderDao orderDao,OrderProvider orderProvider,JwtService jwtService) {
        this.orderDao=orderDao;
        this.orderProvider=orderProvider;
        this.jwtService=jwtService;
    }

    @Transactional
    public int createOrder(PostOrderReq postOrderReq) {
        //주문 생성은 중요한 처리 과정이므로 트랜잭션 처리
        int order_idx=orderDao.createOrder(postOrderReq);
        return order_idx;
    }
    @Transactional
    public int createReview(PostReviewReq postReviewReq) {
        int createReviewCheck=orderDao.createReview(postReviewReq);
        return createReviewCheck;
    }

    public int modifyReview(int review_idx,PatchReviewReq patchReviewReq) {
        return orderDao.modifyReview(review_idx,patchReviewReq);
    }

    public int deleteReview(int review_idx) {
        return orderDao.deleteReview(review_idx);
    }
}
