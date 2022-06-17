package com.example.demo.src.member;


import com.example.demo.config.BaseException;

import com.example.demo.config.BaseResponse;

import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.member.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.FormalValidationException.*;
import static com.example.demo.utils.ValidationRegex.*;
import static com.example.demo.utils.LogicalValidationException.*;

@Service
public class MemberProvider {

    private final MemberDao dao;


    private final JwtService jwtService;

    @Autowired
    public MemberProvider(MemberDao dao, JwtService jwtService) {
        this.dao = dao;
        this.jwtService = jwtService;
    }


    public int getMemberCnt() throws BaseException {

        try {
            return dao.getMemberCnt();
        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    public PostLoginRes login(PostLoginReq req) throws BaseException {

        
        // 유효성 검사 1. 먼저 해당 memberId로 유저가 실제 있는지 조회

        PostLoginRes result = dao.login(req.getMemberId());

            // 회원이 존재하지않는다면

        if(isEmpty(result)){
            throw new BaseException(INVALID_MEMBER);
        }

        // 유효성 검사 2. 유저 상태 화인
        checkMemberStatus(result.getMemberStatus());



        // 유저가 존재한 상황에서 비밀번호를 얻는다.
        Member member = dao.getPwd(req);

        String encryptPwd;

        try {
            // 입력 비밀번호 암호화
            encryptPwd = new SHA256().encrypt(req.getPwd());
        } catch (Exception ignored) { // exception을 무시할때 ignored로 표기한다.

            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        // 유효성 검사 3. 비밀번호 확인
        if(member.getPwd().equals(encryptPwd)){


            String jwt = jwtService.createJwt(member.getMemberId());
            result.setJwt(jwt);

            return result;
        }
        else{

            throw new BaseException(FAILED_TO_LOGIN);
        }




    };

    public GetProfileRes getProfile(Integer memberId) throws BaseException{

        checkMemberExists(memberId);


        try {

            GetProfileRes result =  dao.getProfile(memberId);

            // 회원이 존재하지않는다면
            if(result == null){
                throw new BaseException(BaseResponseStatus.INVALID_MEMBER);
            }

            return result;

        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    };


    public GetProfileForUpdateRes getProfileForUpdate(Integer memberId) throws BaseException{

        try {

            GetProfileForUpdateRes res = dao.getProfileForUpdate(memberId);

            if (isEmpty(res))
                throw new BaseException(RESPONSE_ERROR);


            return res;

        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }


    };

    public GetManageInformationRes getManageInformation(Integer memberId) throws BaseException{

        try {

            GetManageInformationRes res = dao.getManageInformation(memberId);

            if (isEmpty(res))
                throw new BaseException(RESPONSE_ERROR);


            return res;
        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }



}
