package com.example.demo.src.parttime;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.parttime.model.GetParttimePostReq;
import com.example.demo.src.parttime.model.GetParttimePostRes;
import com.example.demo.src.parttime.model.GetParttimePostRes_imgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ParttimeProvider {

    @Autowired
    private ParttimeDao dao;

    public GetParttimePostRes getParttimePost(GetParttimePostReq req) throws BaseException {

        if(dao.checkValidMember(req) != 1){

            throw new BaseException(INVALID_MEMBER_REQUEST);
        }

        if(dao.checkValidParttimePost(req) != 1){

            throw new BaseException(INVALID_POST);
        }

        try {
            GetParttimePostRes res = dao.getParttimePost(req);

            if (res == null) {
                throw new BaseException(RESPONSE_ERROR);
            }

            List<GetParttimePostRes_imgs> imgs = dao.getParttimeImgs(req.getParttimeId());

            if (imgs == null)
                throw new BaseException(RESPONSE_ERROR);

            res.setImgs(imgs);

            return res;
        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    };




}
