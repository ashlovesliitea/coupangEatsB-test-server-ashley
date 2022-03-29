package com.example.demo.src.user.model.social;

import com.example.demo.config.Constant;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;


public class SocialLoginTypeConverter implements Converter<String, Constant.SocialLoginType> {

    @Override
    public Constant.SocialLoginType convert(String s){
        return Constant.SocialLoginType.valueOf(s.toUpperCase());
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}
