package com.example.demo.utils;


import com.example.demo.config.BaseException;
import com.example.demo.src.member.model.Member;

import static com.example.demo.utils.ValidationRegex.*;

import static com.example.demo.config.BaseResponseStatus.*;


public class LogicalValidationException {

    // 유효하지않음
    
        // 유효하지않은 jwt
        public static void checkJwtVaildated(String jwt) throws BaseException{

            throw new BaseException(INVALID_JWT);
        }


        // 권한이 없는 유저
        public static  void checkUserAuth(Member member) throws BaseException{

            throw new BaseException(INVALID_USER_JWT);
        }


        // 동네설정 다시


        // 존재하지않는 동네
        public static  void checkTownExists(int result) throws BaseException{

            if(result != 1){
                throw new BaseException(INVALID_TOWN);
            }

        }



         // 유효하지않은 회원
        public static  void checkMemberExists(int result) throws BaseException{

            if(result != 1){
                throw new BaseException(INVALID_MEMBER);
            }

        }

        // 회원상태 정보 확인
        public static  void checkMemberStatus(String status) throws BaseException{

            if(status.equals("b")){
                throw new BaseException(BLOCKED_MEMBER);
            }

            if(status.equals("d")){
                throw new BaseException(DELETED_MEMBER);
            }

        }

        // 유효하지않은 중고거래 게시물
        public static  void checkItemPostExists(int result) throws BaseException{
    
            if(result != 1){
                throw new BaseException(INVALID_POST);
            }
    
        }
    
        // 중고거래 게시물 상태 정보 확인
        public static  void checkItemPostStatus(String status) throws BaseException{
    
            if(status.equals("deleted")){
                throw new BaseException(DELETED_ITEMPOST);
            }
    
            if(status.equals("hidden")){
                throw new BaseException(HIDDEN_ITEMPOST);
            }
    
        }
        
        



    // 유효하지앟은 게사물
        public static  void checkPostExists(int result) throws BaseException{

            if(result != 1){
                throw new BaseException(INVALID_POST);
            }

        }


        // 유효하지않는 채팅방
        public static  void checkChatRoomExists(int result) throws BaseException{

            if(result != 1){
                throw new BaseException(INVALID_CHATROOM);
            }

        }

    
    // 중복

        // 중복된 이메일
        public static  void isEmailDuplicated(String email) throws BaseException{

            if(!isRegexEmail(email))
                throw new BaseException(DUPLICATED_EMAIL);

        }

        // 가입된 핸드폰 번호
        public static  void isPhoneDuplicated(String phone) throws BaseException{

            if(!isRegexPhone(phone))
                throw new BaseException(DUPLICATED_PHONE);

        }



        // 중복된 닉네임
        public static  void isNicknameDuplicated(String nickname) throws BaseException{

            if(!isRegexNickname(nickname))
                throw new BaseException(DUPLICATED_NICKNAME);

        }


}
