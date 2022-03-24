package com.example.demo;

import org.junit.jupiter.api.Test;

public class distTest {

    public static int estimateDeliveryTime(double dist){
        int deliveryTime=(int)Math.ceil(dist/20*60);
        return deliveryTime;
    }

    @Test
    public void distTest(){
        int delivery=estimateDeliveryTime(0.06);
        System.out.println("delivery = " + delivery);
    }
}
