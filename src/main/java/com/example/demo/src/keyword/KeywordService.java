package com.example.demo.src.keyword;

import com.example.demo.config.BaseException;
import com.example.demo.src.keyword.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.FormalValidationException.*;
import static com.example.demo.utils.LogicalValidationException.*;

@Service
public class KeywordService {

    @Autowired
    private KeywordDao dao;

    // 트랜잭션 처리 필요
    @Transactional(rollbackFor = Exception.class)
    public DeleteKeywordSetRes deleteKeywordSet(DeleteKeywordSetReq req) throws BaseException {

        try{
            int result = dao.deleteKeywordSet(req);

            if(result == 0 ){
                throw new BaseException(DELETE_FAIL_KEYWORD);
            }

            List<GetKeywordSetRes> getRes = dao.getKeywordSet(req.getMemberId());

            if(isEmpty(getRes))
                throw new BaseException(RESPONSE_ERROR);

            DeleteKeywordSetRes res = new DeleteKeywordSetRes(getRes);

            if(isEmpty(res))
                throw new BaseException(RESPONSE_ERROR);


            return res;

        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    };

    // 트랜잭션 처리 필요
    @Transactional(rollbackFor = Exception.class)
    public PostKeywordSetRes postKeywordSet(PostKeywordSetReq req) throws BaseException {
        
        // 동네가 유효한지
            // 동네설정이 되어있을 때만 적용. null인 경우도 있기에
        if(!isEmpty(req.getTown1Id()))
            checkTownExists(dao.checkTownExists(req.getTown1Id()));

        if(!isEmpty(req.getTown2Id()))
        checkTownExists(dao.checkTownExists(req.getTown2Id()));

            try{
            // 필요한 매개 데이터
                // keywordSetId, memberId, name, keywordId, town1Id, town2Id

            // 키워드 등록을 위해 keywordId와 keywordSetId 를 불러온다.
                // 이미 기존 값이 있는 경우, 그대로 쓰되 그게 아니라면 키워드 테이블에 새로 등록해줘야한다.
                // null이 나올 수 있으니 Integer

                // 키워드 고유번호
            Integer keywordId = dao.getKeywordId(req.getName());
            if(isEmpty(keywordId)) keywordId = 0;

                // 키워드 설정 고유번호
            Integer keywordSetId = dao.getKeywordSetId(req.getMemberId(), keywordId);
            if(isEmpty(keywordSetId)) keywordSetId = 0;


            // 키워드 고유번호를 통해 키워드 설정 조회를 했는데, 이미 해당 회원이 등록한 기존 것이 있으면 status만 변경
            if(keywordSetId > 0){
                dao.restoreKeywordSet(keywordSetId);

            }else{
                // 특정 회원의 키워드설정 새로운 것을 등록하는 건데, 전체적으로 기존에 keyword 등록된 것이 있다면
                // 만약 keywordId가 없다면, 키워드도 새로, 키워드 설정도 새로 등록. 키워드 생성 후 그 키워드ID를 가져와서 진행

                if(keywordId == 0){

                    // 키워드 테이블에 새로운 키워드 등록
                    if(dao.insertKeyword(req)!=1)
                        throw new BaseException(FAILED_TO_KEYWORD);

                    // 등록한 키워드 가져오기
                    keywordId = dao.getKeywordId(req.getName());
                }   
                
                    // 특정회원의 키워드 설정 등록
                    if(dao.insertKeywordSet(keywordId, req)!=1)
                        throw new BaseException(FAILED_TO_KEYWORD);
                }

            List<GetKeywordSetRes> getRes = dao.getKeywordSet(req.getMemberId());

            if(isEmpty(getRes))
                throw new BaseException(RESPONSE_ERROR);

            PostKeywordSetRes res = new PostKeywordSetRes(getRes);

            if(isEmpty(res))
                throw new BaseException(RESPONSE_ERROR);


            return res;

        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }



    }



}
