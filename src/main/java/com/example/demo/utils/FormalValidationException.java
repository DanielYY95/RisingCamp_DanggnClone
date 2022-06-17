package com.example.demo.utils;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;

import static com.example.demo.utils.ValidationRegex.*;

import static com.example.demo.config.BaseResponseStatus.*;


public class FormalValidationException {

    // 1. 값이 있는가?
    // 2. String이라면 빈칸인가?
        // 빈칸이어도 되는 부분은 checkEmpty 활용
    // 3. 닉네임, 이메일, 휴대폰 등의 형식이 맞는가?

    
    
    // 빈값 여부 유효성
    
    // Integer를 사용하는 이유는 유효성 검사를 위해 null인지 여부를 확인할 때, null을 담을 수가 있으니


    public static boolean hasRequestValue(Object... obj){

        for(Object x: obj) {

            if (isEmpty(x)) {
                return !(isEmpty(x));
            }
        }

        return true;

    }

    // Common

    public static boolean isEmpty(Object obj){

        boolean result = (obj == null);

        return result;
    }

    public static boolean isEmptyString(String s){

        boolean result = (s == null || s.trim().equals(""));

        return result;
    }


    // 형식 유효성
    
    public static boolean checkIdFormal(Integer id){


        return (id>0);
    }


    public static boolean checkNicknameFormal(String nickname){

        return (isRegexNickname(nickname));

    }


    public static boolean checkPhoneFormal(String phone){


        return (isRegexPhone(phone));
    }

    public static boolean checkEmailFormal(String email){


        return (isRegexEmail(email));
    }

    // 길이 유효성

    public static boolean checkValueLength(String value){

        value = value.trim();

        return (value.length()<=30);
    }


    public static boolean checkTextLength(String text){

        text = text.trim();

        return (text.length()<=1000);
    }

    public static boolean checkUrlLength(String url){

        url = url.trim();

        return (url.length()<=2084);
    }



}
