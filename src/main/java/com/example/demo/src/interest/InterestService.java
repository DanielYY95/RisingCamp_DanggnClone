package com.example.demo.src.interest;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.interest.model.PostInterestReq;
import com.example.demo.src.interest.model.PostInterestRes;
import com.example.demo.src.itemPost.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.FormalValidationException.isEmpty;
import static com.example.demo.utils.LogicalValidationException.*;


@Service
public class InterestService {

    @Autowired
    private InterestDao dao;

    @Autowired
    private InterestProvider provider;

    @Transactional(rollbackFor = Exception.class)
    public PostInterestRes postInterest(PostInterestReq req) throws BaseException{

        HashMap<String, String> statusMap = dao.checkPostExists(req.getPostId());


        int isExist = Integer.parseInt(String.valueOf(statusMap.get("cnt")));


        checkItemPostExists(isExist);


        int result;

        try{

            // 데이터가 없다면 insert
            if(dao.getInterest(req) != 1){

                result =  dao.insertInterest(req);
                if(result != 1)
                    throw new BaseException(POST_FAIL_INTEREST);

            }else{
                // 데이터가 있다면 status에 따라 update

                result = (req.getHasInterest() == 1) ? dao.modifyToCancel(req):dao.modifyToInterest(req);
                if(result != 1)
                    throw new BaseException(MODIFY_FAIL_INTEREST);



            }

            PostInterestRes res = dao.getInterestStatus(dao.getInterestId(req));

            if(isEmpty(res))
                throw new BaseException(RESPONSE_ERROR);

            return res;

        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }


}
