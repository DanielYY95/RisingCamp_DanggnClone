package com.example.demo.src.itemPost;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.itemPost.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.FormalValidationException.*;
import static com.example.demo.utils.ValidationRegex.*;
import static com.example.demo.utils.LogicalValidationException.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ItemPostProvider {

    @Autowired
    private final ItemPostDao dao;

    private final JwtService jwtService;


    public ItemPostProvider(ItemPostDao dao, JwtService jwtService) {
        this.dao = dao;
        this.jwtService = jwtService;
    }

    // 트랜잭션
    @Transactional(rollbackFor = Exception.class)
    public GetItemPostRes getItemPost(GetItemPostReq req) throws BaseException {



        HashMap<String, String> statusMap = dao.checkMemberExists(req.getMemberId());

        int isExist = Integer.parseInt(String.valueOf(statusMap.get("cnt")));
        // count(*)는 number 형태이기에 long 타입이다. 그래서 이를 바로 int로 형변환할 수 없다.
            // 그래서 String으로 형변환하고나서 int형변환

        checkMemberExists(isExist);

        String status = statusMap.get("memberStatus");


        checkMemberStatus(status);

        statusMap = dao.checkPostExists(req.getItemPostId());


        isExist = Integer.parseInt(String.valueOf(statusMap.get("cnt")));

        checkItemPostExists(isExist);

        status = statusMap.get("postStatus");


        checkItemPostStatus(status);


        try {

            GetItemPostRes res = dao.getItemPostInformation(req);

            if (isEmpty(res))
                throw new BaseException(RESPONSE_ERROR);

            List<GetItemPostRes_imgs> imgList = dao.getItemPostImgs(req.getItemPostId());

            if (isEmpty(imgList))
                throw new BaseException(RESPONSE_ERROR);

            List<GetItemPostRes_otherItems> otherItems = dao.getWriterOtherItems(res.getWriterId(), req.getItemPostId());


            res.setImgList(imgList);
            res.setOtherItems(otherItems);

            // 조회 수 증가
            dao.insertViewCnt(req.getItemPostId());


            return res;

        }catch (Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }


    }
    
    // 둘 중 하나라도 조회가 제대로 안되면 다시
    @Transactional(rollbackFor = Exception.class)
    public GetItemPostSearchRes getItemPostSearch(String keyword)throws BaseException{

        try {

            List<ItemPostList> titleSearchList = dao.getItemPostTitleSearch(keyword);
            List<ItemPostList> contentSearchList = dao.getItemPostContentSearch(keyword);

            if(isEmpty(titleSearchList) && isEmpty(contentSearchList))
                throw new BaseException(RESPONSE_ERROR);

            GetItemPostSearchRes res = new GetItemPostSearchRes(titleSearchList, contentSearchList);



            return res;

        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public GetItemPostReadyUpdateRes getItemPostReadyUpdate(GetItemPostReadyUpdateReq req) throws BaseException{

        HashMap<String, String>statusMap = dao.checkPostExists(req.getItemPostId());


        int isExist = Integer.parseInt(String.valueOf(statusMap.get("cnt")));

        checkItemPostExists(isExist);



        try{

            GetItemPostReadyUpdateRes res = dao.getItemPostReadyUpdate(req);

            List<GetItemPostRes_imgs> imgList = dao.getItemPostImgs(req.getItemPostId());

            res.setImgList(imgList);

            return res;

        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }


    }

    public List<GetItemPostSellRes> getItemPostSell(GetItemPostSellReq req) throws BaseException{


        HashMap<String, String>statusMap = dao.checkPostExists(req.getPostId());


        int isExist = Integer.parseInt(String.valueOf(statusMap.get("cnt")));

        checkItemPostExists(isExist);


        try{

            List<GetItemPostSellRes> res = dao.getBuyerIdList(req);

            if(isEmpty(res))
                throw new BaseException(RESPONSE_ERROR);

            return res;

        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }


    }



}
