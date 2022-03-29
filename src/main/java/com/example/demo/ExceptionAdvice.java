package com.example.demo;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.NestedServletException;

import java.nio.charset.MalformedInputException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value= BaseException.class)
    public BaseResponse BaseExceptionHandler(BaseException e){
        System.out.println("오류발생");
        return new BaseResponse<>(e.getStatus());
    }

    @ExceptionHandler(value= { SQLException.class})
    public BaseResponse DatabaseExceptionHandler(){
        //db관련 exception
        return new BaseResponse<>(BaseResponseStatus.DATABASE_ERROR);
    }

    @ExceptionHandler(value= {DataAccessException.class})
    public BaseResponse DataAccessExceptionHandler(){
        //db관련 exception
        return new BaseResponse<>(BaseResponseStatus.DATA_NOT_FOUND);
    }

    @ExceptionHandler(value= GeneralSecurityException.class)
    public BaseResponse SecurityExceptionHandler(){
        //비밀번호 입력시 SHA256 암호화 에러 관련 핸들러
        return new BaseResponse<>(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
    }

    @ExceptionHandler(value= JsonProcessingException.class)
    public BaseResponse InvalidSocialLoginHandler(){
        return new BaseResponse(BaseResponseStatus.INVALID_SOCIAL_LOGIN);
    }

    @ExceptionHandler(value={JsonParseException.class})
    public BaseResponse JsonParseExceptionHandler(){
        return new BaseResponse(BaseResponseStatus.UNAVAILABLE_TO_PARSE_JSON);
    }

    @ExceptionHandler(value={HttpMessageNotReadableException.class})
    public BaseResponse requestNotReadableExceptionhandler(){
        return new BaseResponse(BaseResponseStatus.HTTP_MESSAGE_UNREADABLE);
    }

    //JsonParseException,MalformedJwtException,NestedServletException
    @ExceptionHandler(value={MalformedInputException.class, NestedServletException.class})
    public BaseResponse JWTExceptionHandler(){
        return new BaseResponse(BaseResponseStatus.INVALID_JWT);
    }

    @ExceptionHandler(value=Exception.class)
    public void EtcExceptionHandler(Exception e){
        System.err.println(e);
    }

}
