package com.example.demo.utils;


import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import static com.example.demo.config.BaseResponseStatus.INVALID_MEMBER;
import static com.example.demo.config.BaseResponseStatus.MEMBERS_EMPTY_MEMBER_ID;
import static com.example.demo.utils.FormalValidationException.checkIdFormal;
import static com.example.demo.utils.FormalValidationException.isEmpty;

@Aspect
@Component
public class MemberValidationAspect {


//    @Before("")
//    public BaseResponse<BaseResponseStatus> checkMemberId(Integer memberId) {
//
//        BaseResponseStatus msg = null;
//
//        // 유효성 검사 : 회원고유번호가 빈값인지
//        if(isEmpty(memberId))
//            msg = (MEMBERS_EMPTY_MEMBER_ID);
//
//        // 유효성 검사: 회원고유번호가 양수인지
//        if(!(checkIdFormal(memberId)))
//            msg = (INVALID_MEMBER);
//
//
//        return new BaseResponse<>(msg);
//    }


}
