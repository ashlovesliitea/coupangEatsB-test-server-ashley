package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.request.PatchUserReq;
import com.example.demo.src.user.model.request.PostAddressReq;
import com.example.demo.src.user.model.request.PostPaymentReq;
import com.example.demo.src.user.model.request.PostUserReq;
import com.example.demo.src.user.model.response.PostUserRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkUserId(postUserReq.getUser_id()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;

            //암호화
            pwd = new SHA256().encrypt(postUserReq.getUser_pw());
            postUserReq.setUser_pw(pwd);
            System.out.println("pwd = " + pwd);

            int userIdx = userDao.createUser(postUserReq);
            String jwt=jwtService.createJwt(userIdx);
            return new PostUserRes(userIdx,postUserReq.getUser_name(),jwt);

    }

    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {

            int result = userDao.modifyUserName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
    }

    public int createUserAddress(PostAddressReq postAddressReq) {

        return userDao.createUserAddress(postAddressReq);
    }

    public int deleteUserAddress(int user_address_idx) {
        return userDao.deleteUserAddress(user_address_idx);
    }

    public int createPayment(PostPaymentReq postPaymentReq) {
        return userDao.createPayment(postPaymentReq);
    }

    public int deleteUserPayment(int user_payment_idx) {
        return userDao.deletePayment(user_payment_idx);
    }
}
