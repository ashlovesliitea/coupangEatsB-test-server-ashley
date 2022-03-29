package com.example.demo.src.user;

import com.example.demo.annotation.NoAuth;
import com.example.demo.config.Constant;
import com.example.demo.src.user.model.response.GetSocialOAuthRes;
import com.example.demo.src.user.model.social.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final UserProvider userProvider;
    private final UserService userService;
    private final JwtService jwtService;
    private final OAuthService oAuthService;


    @Autowired
    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService, OAuthService oAuthService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
        this.oAuthService = oAuthService;
    }
    //TODO- UserController try-catch문 전부 삭제할것

    /**
     * 회원 조회 API
     * [GET] /users
     * 전체 회원/이메일로 조회 api
     * [GET] /users?email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @NoAuth
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users?email=
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String email) {
        try{
            if(email == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(email);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * 브랜치 테스트용
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @NoAuth
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 유저 소셜 로그인으로 리다이렉트 해주는 url
     * [GET] /accounts/auth
     * @return void
     */
    @NoAuth
    @ResponseBody
    @GetMapping("/sign-in/{socialLoginType}") //google이 들어올 것이다.
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
        Constant.SocialLoginType socialLoginType= Constant.SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.request(socialLoginType);
    }


    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginPath (GOOGLE, FACEBOOK, NAVER, KAKAO)
     * @param code API Server 로부터 넘어오는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
     */

    @NoAuth
    @GetMapping(value = "/auth/{socialLoginType}/callback")
    public BaseResponse<GetSocialOAuthRes> callback(
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code) throws IOException, BaseException {
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
        Constant.SocialLoginType socialLoginType= Constant.SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        GetSocialOAuthRes getSocialOAuthRes=oAuthService.oAuthLogin(socialLoginType,code);
        return new BaseResponse<>(getSocialOAuthRes);
    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @NoAuth
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {

        if(postUserReq.getUser_id()== null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getUser_id())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/sign-in
     * @return BaseResponse<PostLoginRes>
     */
    @NoAuth
    @ResponseBody
    @PostMapping("/sign-in")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq,@RequestParam(required = false)String redirectURL) throws BaseException {

        if (postLoginReq.getUser_id() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if (!isRegexEmail(postLoginReq.getUser_id())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        String redirect_url;
        if(redirectURL==null){
            redirect_url="/app/stores";
        }
        else{
            redirect_url=redirectURL;
        }

        PostLoginRes postLoginRes = userProvider.logIn(postLoginReq,redirect_url);
        return new BaseResponse<>(postLoginRes);
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUser_name());
            userService.modifyUserName(patchUserReq);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
