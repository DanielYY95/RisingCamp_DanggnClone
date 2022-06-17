package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexPhone(String target){
        String regex = "^01(?:0|1[6-9])-\\d{3,4}-\\d{4}$"; // 01 다음에 오는 값으로, 0,1,6~9 만 가능
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexNickname(String target){
        String regex = "^[a-zA-Z0-9가-힣]*$"; // 한글, 영어, 숫자만
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }


    public static boolean isRegexUrl(String target){

        String regex =
                "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                        "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                        "([).!';/?:,][[:blank:]])?$";

        Pattern URL_PATTERN = Pattern.compile(regex);
        Matcher matcher = URL_PATTERN.matcher(target);
        return matcher.find();
    }


}

