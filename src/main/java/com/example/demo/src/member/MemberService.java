package com.example.demo.src.member;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.member.model.*;
import com.example.demo.utils.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.FormalValidationException.*;
import static com.example.demo.utils.ValidationRegex.*;
import static com.example.demo.utils.LogicalValidationException.*;

@Service
public class MemberService {

    @Autowired
    private MemberDao dao;

    @Autowired
    private MemberProvider privider;

    @Transactional(rollbackFor = Exception.class)
    public PostMemberRes createUser(PostMemberReq req) throws BaseException {

        String originalPwd = req.getPwd();


        // 중복: 핸드폰번호, 닉네임
        
        // 존재한가: 동네

        if(dao.checkValidPhone(req) ==1){
            throw new BaseException(DUPLICATED_PHONE);
        }

        if(dao.checkValidNickname(req) ==1){
            throw new BaseException(DUPLICATED_NICKNAME);
        }

        if(dao.checkValidTown(req.getTown1Id()) != 1
                || dao.checkValidTown(req.getBasicTownId()) != 1){
            throw new BaseException(INVALID_TOWN);
        }

        try {
            
            // 암호화된 비밀번호로 저장
            String encryptPwd = new SHA256().encrypt(req.getPwd());
            req.setPwd(encryptPwd);

            int result = dao.createUser(req);

            if(result!=1)
                throw new BaseException(FAILED_TO_REGISTER);

            PostLoginReq loginReq = new PostLoginReq(req.getMemberId(), originalPwd);
            PostLoginRes loginRes = privider.login(loginReq);


            if(isEmpty(loginRes))
                throw new BaseException(RESPONSE_ERROR);

            PostMemberRes res = new PostMemberRes(loginRes);

            if(isEmpty(res))
                throw new BaseException(RESPONSE_ERROR);

            return res;

        }catch (Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public PatchProfileRes patchProfile(PatchProfileReq req) throws BaseException{

        try{
            int result =  dao.updateProfile(req);
            if(result != 1)
                throw new BaseException(PATCH_FAIL_PROFILE);


            GetProfileForUpdateRes updatedProfile = dao.getProfileForUpdate(req.getMemberId());


            if(updatedProfile == null)
                throw new BaseException(RESPONSE_ERROR);


            PatchProfileRes  res = new PatchProfileRes(updatedProfile);

            return res;
        }catch (Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public PatchManageInformationRes patchManageInformation(PatchManageInformationReq req) throws BaseException{

        int result = 0;


        try {

            if(req.getEmail() != null && isEmpty(req.getPhone())){
                result = dao.updateEmail(req);
            }

            if(req.getPhone() != null && isEmpty(req.getEmail())){
                result = dao.updatePhone(req);
            }

            if(result !=1){
                throw new BaseException(RESPONSE_ERROR);
            }

            System.out.println("@@@@1111");
            GetManageInformationRes getRes = dao.getManageInformation(req.getMemberId());

            System.out.println("@@@@2222");
            if(isEmpty(getRes))
                throw new BaseException(RESPONSE_ERROR);

            PatchManageInformationRes res = new PatchManageInformationRes(getRes);
            System.out.println("@@@@3333");

            return res;
        }catch (Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }


    }






}
