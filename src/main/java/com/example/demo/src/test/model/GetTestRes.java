package com.example.demo.src.test.model;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class GetTestRes {
    private String testString;
    private int testInt;
    public GetTestRes(String testString,int testInt){
        this.testString=testString;
        this.testInt=testInt;
    }
}
