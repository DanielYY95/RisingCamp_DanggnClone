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


    // users
    MEMBERS_EMPTY_MEMBER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    MEMBERS_EMPTY_MEMBER_PWD(false, 2011, "유저 비밀번호 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),


    // [POST] /members
    POST_MEMBERS_INVALID_NICKNAME(false,2021,
                "닉네임은 띄어쓰기 없이 한글, 영문, 숫자만 가능해요."),

    POST_MEMBERS_INVALID_PHONE(false,2022,
            "핸드폰 번호 형식을 확인해주세요."),

    POST_MEMBERS_INVALID_TOWN(false,2023,
            "동네 설정을 다시 해주세요"),


    DUPLICATED_PHONE(false, 2025, "가입된 핸드폰 번호입니다."),
    DUPLICATED_NICKNAME(false, 2026, "중복된 닉네임입니다. "),
    INVALID_TOWN(false, 2027, "존재하지않는 동네입니다."),
    INVALID_MEMBER_REQUEST(false, 2028, "존재하지않는 회원으로 접근하려고 합니다."),
    INVALID_POST(false, 2029, "존재하지않는 게시물입니다."),
    EMPTY_CONTENT(false, 2030, "내용을 빈칸으로 둘 수 없습니다."),
    INVALID_CHATROOM(false, 2031, "존재하지않는 채팅방입니다."),
    EMPTY_NICKNAME(false, 2032, "닉네임을 입력하세요"),
    INVALID_URL(false, 2033, "URL 형식이 올바르지 않습니다."),
    TOOLONG_VALUE(false, 2034, "30자 이하로 작성해주세요"),
    TOOLONG_TEXT(false, 2035, "텍스트는 1000자 이하로 작성해주세요"),
    TOOLONG_URL(false, 2036, "url은 2084자 이하로 작성해주세요."),
    EMPTY_TITLE(false, 2037, "제목을 빈칸으로 둘 수 없습니다."),
    INVALID_OFFERSTATUS(false, 2038, "가격제안 여부를 확인해주세요"),
    INVALID_CATEGORY(false, 2039, "존재하지않는 항목입니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),


    // [Get] /members
    INVALID_MEMBER(false, 3001, "존재하지않는 회원입니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"비밀번호가 틀렸습니다."),
    FAILED_TO_CHAT(false,3015,"채팅 추가에 실패했습니다."),


    FAILED_TO_REGISTER(false,3016,
            "회원가입에 실패했습니다."),

    BLOCKED_MEMBER(false,3017,
            "정지된 유저입니다."),


    DELETED_MEMBER(false,3018,
            "탈퇴한 유저입니다."),

    DELETED_ITEMPOST(false,3019,
            "삭제된 게시글입니다."),
    HIDDEN_ITEMPOST(false,3020,
            "숨김처리된 게시글입니다."),
    FAILED_TO_ITEMPOST(false,3021,
            "게시물 추가에 실패했습니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    DELETE_FAIL_KEYWORD(false,4015,"키워드 삭제 실패"),


    PATCH_FAIL_PROFILE(false,4016,"프로필 수정 실패"),
    FAILED_TO_KEYWORD(false,4017,"키워드 추가 실패"),

    MODIFY_FAIL_ITEMPOST(false,4018,"게시물 수정 실패"),

    POST_FAIL_INTEREST(false,4019,"관심 추가 실패"),
    MODIFY_FAIL_INTEREST(false,4020,"관심 수정 실패"),
    MODIFY_FAIL_SELL(false,4021,"판매완료 실패")
    
    
    ;


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
