package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    UNAVAILABLE_TO_PARSE_JSON(false,2004,"잘못된 형태의 JSON 요청입니다. 요청 형식을 다시 확인해주세요."),
    INVALID_SOCIAL_LOGIN(false,2005,"잘못된 소셜 로그인 요청입니다. 다시 시도해주세요."),
    HTTP_MESSAGE_UNREADABLE(false,2006,"JSON값을 파싱할 수 없습니다. 다시 시도해주세요."),
    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    DATA_NOT_FOUND(false,4002,"해당 데이터가 존재하지 않습니다."),
    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    FAIL_TO_CREATE_STORE(false,4020,"새로운 상점을 생성하는 데 실패했습니다. 재시도해 주세요"),
    FAIL_TO_CREATE_MENU_CATEGORY(false,4025,"새로운 메뉴 카테고리를 생성하는 데 실패했습니다. 재시도해 주세요"),
    FAIL_TO_CREATE_MENU(false,4021,"새로운 메뉴를 생성하는 데 실패했습니다. 재시도해 주세요"),
    FAIL_TO_CREATE_OPTION(false,4022,"새로운 옵션을 생성하는 데 실패했습니다. 재시도해 주세요"),

    FAIL_TO_CREATE_ORDER(false,4023,"새로운 주문을 생성하는 데 실패했습니다. 재시도해 주세요"),
    FAIL_TO_CREATE_REVIEW(false,4024,"새로운 리뷰를 생성하는 데 실패했습니다. 재시도해 주세요"),
    FAIL_TO_CREATE_LIKED(false,4026,"새로운 즐겨찾기를 생성하는 데 실패했습니다. 재시도해 주세요"),

    FAIL_TO_MODIFY_REVIEW(false,4027,"리뷰를 수정하는데 실패했습니다."),
    FAIL_TO_DELETE_REVIEW(false,4028,"리뷰를 삭제하는데 실패했습니다."),

    FAIL_TO_CREATE_NEW_USER_ADDDRESS(false,4029,"새로운 유저 주소를 등록하는데 실패했습니다."),
    FAIL_TO_DELETE_USER_ADDDRESS(false,4030,"유저 주소를 삭제하는데 실패했습니다."),
    FAIL_TO_CREATE_NEW_PAYMENT(false,4031,"새로운 결제수단을 등록하는데 실패했습니다."),
    FAIL_TO_DELETE_PAYMENT(false,4032,"결제수단을 삭제하는데 실패했습니다.");
    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
