package com.example.demo.src.itemPost;



import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.itemPost.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;
import static com.example.demo.utils.FormalValidationException.*;
import static com.example.demo.utils.LogicalValidationException.*;


@Service
public class ItemPostService {

    @Autowired
    private ItemPostDao dao;

    @Autowired
    private ItemPostProvider provider;

    @Transactional(rollbackFor = Exception.class)
    public int postItemPost(PostItemPostReq req) throws BaseException {

        // 유효한 동네인가
        if(dao.checkValidTown(req.getTownId()) != 1){
            throw new BaseException(INVALID_TOWN);
        }


        // 유효한 항목인가
        if(dao.checkValidCategory(req.getCategoryId()) != 1){
            throw new BaseException(INVALID_CATEGORY);
        }

        try {
            // 게시물 등록 후, 고유번호를 얻는다.
            int result = dao.insertItemPost(req);

            if (result != 1)
                throw new BaseException(FAILED_TO_ITEMPOST);

            int itemPostId = req.getItemPostId();


            // 이미지들을 얻어서 등록해준다.
            List<PostItemPostReq_imgs> imgList = req.getImgList();

            // 사진 등록 반복해서
            if (!isEmpty(imgList)) {
                for (PostItemPostReq_imgs imgs : imgList) {

                    dao.insertItemPostImgs(itemPostId, req.getMemberId(), imgs);

                }
            }


            return itemPostId;
        }catch (Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }

    }


    @Transactional(rollbackFor = Exception.class)
    public PatchItemPostRes patchItemPostPull(PatchItemPostReq req) throws BaseException{


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


        try {


            int result = dao.modifyPullPost(req);
            if(result!=1)
                throw new BaseException(MODIFY_FAIL_ITEMPOST);

            GetItemPostReq getReq = new GetItemPostReq(req.getMemberId(), req.getItemPostId());

            GetItemPostRes getRes = dao.getItemPostInformation(getReq);
            List<GetItemPostRes_imgs> imgList = dao.getItemPostImgs(req.getItemPostId());
            List<GetItemPostRes_otherItems> otherItems = dao.getWriterOtherItems(getRes.getWriterId(), req.getItemPostId());
            getRes.setImgList(imgList);
            getRes.setOtherItems(otherItems);


            PatchItemPostRes res = new PatchItemPostRes(getRes);

            return res;

        }catch(Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public PatchItemPostRes patchItemPostHidden(PatchItemPostReq req) throws BaseException{

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


        try {

            // 게시물의 상태에 따라 공개 혹은 비공개 실행 후 결과 값을 받아온다.

            int result = (dao.checkItemPostStatus(req).equals("active"))? dao.modifyToHidden(req): dao.modifyToActive(req);
            if(result!=1)
                throw new BaseException(MODIFY_FAIL_ITEMPOST);


            GetItemPostReq getReq = new GetItemPostReq(req.getMemberId(), req.getItemPostId());

            GetItemPostRes getRes = dao.getItemPostInformation(getReq);
            List<GetItemPostRes_imgs> imgList = dao.getItemPostImgs(req.getItemPostId());
            List<GetItemPostRes_otherItems> otherItems = dao.getWriterOtherItems(getRes.getWriterId(), req.getItemPostId());
            getRes.setImgList(imgList);
            getRes.setOtherItems(otherItems);


            PatchItemPostRes res = new PatchItemPostRes(getRes);

            return res;

        }catch(Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public PatchItemPostRes patchItemPostDelete(PatchItemPostReq req) throws BaseException{

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

        try {

            int result = dao.modifyToDeleted(req);
            if(result!=1)
                throw new BaseException(MODIFY_FAIL_ITEMPOST);


            GetItemPostReq getReq = new GetItemPostReq(req.getMemberId(), req.getItemPostId());

            GetItemPostRes getRes = dao.getItemPostInformation(getReq);
            List<GetItemPostRes_imgs> imgList = dao.getItemPostImgs(req.getItemPostId());
            List<GetItemPostRes_otherItems> otherItems = dao.getWriterOtherItems(getRes.getWriterId(), req.getItemPostId());
            getRes.setImgList(imgList);
            getRes.setOtherItems(otherItems);


            PatchItemPostRes res = new PatchItemPostRes(getRes);

            return res;

        }catch(Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public PutItemPostRes putItemPost(PutItemPostReq req) throws BaseException{

        // 유효한 항목인가
        if(dao.checkValidCategory(req.getCategoryId()) != 1){
            throw new BaseException(INVALID_CATEGORY);
        }


        try{

            int result = dao.updateItempost(req);
            if(result !=1)
                throw new BaseException(MODIFY_FAIL_ITEMPOST);

            result = dao.deleteImgs(req);
            if(result !=1)
                throw new BaseException(MODIFY_FAIL_ITEMPOST);


            List<PostItemPostReq_imgs> imgList = req.getImgList();

            // 사진 등록 반복해서
            if (!isEmpty(imgList)) {
                for (PostItemPostReq_imgs imgs : imgList) {

                    dao.insertItemPostImgs(req.getItemPostId(), req.getWriterId(), imgs);

                }
            }

            GetItemPostReq getReq = new GetItemPostReq(req.getWriterId(), req.getItemPostId());

            GetItemPostRes getRes = dao.getItemPostInformation(getReq);
            List<GetItemPostRes_imgs> getImgList = dao.getItemPostImgs(req.getItemPostId());
            List<GetItemPostRes_otherItems> otherItems = dao.getWriterOtherItems(getRes.getWriterId(), req.getItemPostId());
            getRes.setImgList(getImgList);
            getRes.setOtherItems(otherItems);


           PutItemPostRes res = new PutItemPostRes(getRes);


            return res;

        }catch(Exception exception){

        throw new BaseException(DATABASE_ERROR);
    }


    }

    @Transactional(rollbackFor = Exception.class)
    public PatchItemPostSellRes patchItemPostSell(PatchItemPostSellReq req) throws BaseException{

        try{

            int result = dao.updateItemPostSold(req);
            if(result !=1)
                throw new BaseException(MODIFY_FAIL_SELL);

            GetItemPostReq getReq = new GetItemPostReq(req.getMemberId(), req.getPostId());

            GetItemPostRes getRes = dao.getItemPostInformation(getReq);

            if(isEmpty(getRes))
                throw new BaseException(RESPONSE_ERROR);

            List<GetItemPostRes_imgs> imgList = dao.getItemPostImgs(req.getPostId());
            List<GetItemPostRes_otherItems> otherItems = dao.getWriterOtherItems(getRes.getWriterId(), req.getPostId());
            getRes.setImgList(imgList);
            getRes.setOtherItems(otherItems);


            PatchItemPostSellRes res = new PatchItemPostSellRes(getRes);

            if(isEmpty(res))
                throw new BaseException(RESPONSE_ERROR);

            return res;

        }catch(Exception exception){

            throw new BaseException(DATABASE_ERROR);
        }


    }


}
