package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException{

            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
    }

    public List<GetUserRes> getUsersByEmail(String email) throws BaseException{

            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
            return getUsersRes;

    }


    public GetUserRes getUser(int userIdx) throws BaseException {

            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;

    }

    public int checkUserId(String userId) throws BaseException{
            return userDao.checkUserId(userId);

    }

    public PostLoginRes logIn(PostLoginReq postLoginReq,String redirectURL) throws BaseException{
        User user = userDao.getPwd(postLoginReq);
        String encryptPwd;

        encryptPwd=new SHA256().encrypt(postLoginReq.getUser_pw());
        String redirect_url;


        if(user.getUser_pw().equals(encryptPwd)){
            int userIdx = user.getUser_idx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt,redirectURL);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

}
