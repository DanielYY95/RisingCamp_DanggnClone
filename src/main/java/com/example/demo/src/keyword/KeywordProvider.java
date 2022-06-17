package com.example.demo.src.keyword;

import com.example.demo.config.BaseException;
import com.example.demo.src.keyword.model.GetKeywordAlarmsRes;
import com.example.demo.src.keyword.model.GetKeywordSetRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.utils.LogicalValidationException.checkMemberExists;
import static com.example.demo.utils.LogicalValidationException.checkMemberStatus;

@Service
public class KeywordProvider {

    @Autowired
    private KeywordDao dao;

    public List<GetKeywordSetRes> getKeywordSet(Integer memberId) throws BaseException {

        HashMap<String, String> statusMap = dao.checkMemberExists(memberId);

        int isExist = Integer.parseInt(String.valueOf(statusMap.get("cnt")));

        checkMemberExists(isExist);

        String status = statusMap.get("memberStatus");

        checkMemberStatus(status);

        try{

            // 조회 결과가 null이 나올 수도 있다.

            return dao.getKeywordSet(memberId);

        }catch (Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }
    };

    public List<GetKeywordAlarmsRes> getKeywordAlarms(Integer memberId) throws BaseException{

        HashMap<String, String> statusMap = dao.checkMemberExists(memberId);

        int isExist = Integer.parseInt(String.valueOf(statusMap.get("cnt")));

        checkMemberExists(isExist);

        String status = statusMap.get("memberStatus");

        checkMemberStatus(status);

        try {

            return dao.getKeywordAlarms(memberId);

        }catch (Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }


    };


}
