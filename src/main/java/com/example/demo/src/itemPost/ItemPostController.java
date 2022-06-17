package com.example.demo.src.itemPost;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.itemPost.model.*;
import com.example.demo.src.member.model.GetProfileForUpdateRes;
import com.example.demo.utils.JwtService;
import lombok.Getter;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.FormalValidationException.*;
import static com.example.demo.utils.ValidationRegex.*;


@RestController
@RequestMapping("/app/itemPosts")
public class ItemPostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ItemPostProvider provider;

    @Autowired
    private final ItemPostService service;

    @Autowired
    private final JwtService jwtService;


    public ItemPostController(ItemPostProvider provider, ItemPostService service, JwtService jwtService) {
        this.provider = provider;
        this.service = service;
        this.jwtService = jwtService;
    }

    @GetMapping("/{itemPostId}")
    public BaseResponse<GetItemPostRes> getItemPost(@PathVariable Integer itemPostId){

        try {
            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();


            GetItemPostReq req = new GetItemPostReq(memberIdByJwt, itemPostId);

            GetItemPostRes res = provider.getItemPost(req);

            return new BaseResponse<>(res);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }



    @PostMapping("")
    public BaseResponse<PostItemPostRes> PostItemPost(@RequestBody PostItemPostReq req) throws BaseException {
        
        // 회원고유번호가 빈값인지, 형식에 맞는지
        
        if(isEmpty(req.getMemberId()) || !(checkIdFormal(req.getMemberId())))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        // 게시물에 필수적으로 들어갈 정보들이 빈값인지

        if(isEmptyString(req.getTitle()) || isEmpty(req.getPrice()) || isEmpty(req.getCategoryId())
                || isEmptyString(req.getOfferStatus()) || isEmptyString(req.getContent()) || isEmpty(req.getTownId()))
            return new BaseResponse<>(REQUEST_ERROR);

        // 제목 길이 수 30자 제한
        
        if(!checkValueLength(req.getTitle()))
            return new BaseResponse<>(TOOLONG_VALUE);

        // 내용 길이 수 1000자 제한
        if(!checkTextLength(req.getContent()))
            return new BaseResponse<>(TOOLONG_TEXT);
        
        // 등록할 사진들 모두 URL 길이 수 제한 및 정규식

        if(!isEmpty(req.getImgList())){
            for(PostItemPostReq_imgs img : req.getImgList()){
                if(!checkUrlLength(img.getPath()))
                    return new BaseResponse<>(TOOLONG_URL);

                if(!isRegexUrl(img.getPath()))
                    return new BaseResponse<>(INVALID_URL);
            }

        }

        // 가격이 0보다 같거나 큰지, 항목ID와 동네ID가 모두 양수인지

        if(!(req.getPrice()>=0) || !checkIdFormal(req.getCategoryId()) || !checkIdFormal(req.getTownId()))
            return new BaseResponse<>(REQUEST_ERROR);

        // 가격 제안 여부 확인
        
        if(!(req.getOfferStatus().equals("T") || req.getOfferStatus().equals("F")))
            return new BaseResponse<>(INVALID_OFFERSTATUS);

        try{
            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            // 유효성 검사: request로 받은 id와 추출한 id 비교
            if(req.getMemberId() != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // 게시물을 등록하고나서 얻은 고유번호
            int itemPostId = service.postItemPost(req);

            // 게시물 고유번호와 회원 고유번호로 새로 등록된 중고거래 게시물 조회
            GetItemPostReq getReq =  new GetItemPostReq(req.getMemberId(), itemPostId);
            GetItemPostRes getRes =  provider.getItemPost(getReq);

            PostItemPostRes res = new PostItemPostRes(getRes);

            return new BaseResponse<>(res);

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));

        }





    }

    @GetMapping("")
    public BaseResponse<GetItemPostSearchRes> getItemPostList(@RequestParam(required = false) String keyword){

        // 길이수 제한 유효성
        if(!checkValueLength(keyword))
            return new BaseResponse<>(BaseResponseStatus.TOOLONG_VALUE);
        try {
            GetItemPostSearchRes result = provider.getItemPostSearch(keyword);
            return new BaseResponse<>(result);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @PatchMapping("/pull")
    public BaseResponse<PatchItemPostRes> patchItemPostPull(@RequestBody PatchItemPostReq req){


        // 유효성 검사 회원고유번호
        if (isEmpty(req.getMemberId()))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        if(!checkIdFormal(req.getMemberId()))
            return new BaseResponse<>(INVALID_MEMBER);

        // 유효성 검사 게시물 고유번호
        if (isEmpty(req.getItemPostId()))
            return new BaseResponse<>(REQUEST_ERROR);

        if(!checkIdFormal(req.getItemPostId()))
            return new BaseResponse<>(INVALID_POST);


        try{

            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            if(req.getMemberId() != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchItemPostRes result = service.patchItemPostPull(req);

            return new BaseResponse<>(result);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PatchMapping("/hidden")
    public BaseResponse<PatchItemPostRes> patchItemPostHidden(@RequestBody PatchItemPostReq req){


        // 유효성 검사 회원고유번호
        if (isEmpty(req.getMemberId()))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        if(!checkIdFormal(req.getMemberId()))
            return new BaseResponse<>(INVALID_MEMBER);

        // 유효성 검사 게시물 고유번호
        if (isEmpty(req.getItemPostId()))
            return new BaseResponse<>(REQUEST_ERROR);

        if(!checkIdFormal(req.getItemPostId()))
            return new BaseResponse<>(INVALID_POST);

        try{

            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            if(req.getMemberId() != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }


            PatchItemPostRes result = service.patchItemPostHidden(req);

            return new BaseResponse<>(result);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PatchMapping("/delete")
    public BaseResponse<PatchItemPostRes> patchItemPostDelete(@RequestBody PatchItemPostReq req){

        // 유효성 검사 회원고유번호
        if (isEmpty(req.getMemberId()))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        if(!checkIdFormal(req.getMemberId()))
            return new BaseResponse<>(INVALID_MEMBER);

        // 유효성 검사 게시물 고유번호
        if (isEmpty(req.getItemPostId()))
            return new BaseResponse<>(REQUEST_ERROR);

        if(!checkIdFormal(req.getItemPostId()))
            return new BaseResponse<>(INVALID_POST);

        try{

            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            if(req.getMemberId() != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchItemPostRes result = service.patchItemPostDelete(req);

            return new BaseResponse<>(result);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @GetMapping("/{itemPostId}/update")
    public BaseResponse<GetItemPostReadyUpdateRes> getItemPostReadyUpdate(@PathVariable Integer itemPostId, @RequestParam Integer memberId){

        // 유효성 검사 회원고유번호
        if (isEmpty(memberId))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        if(!checkIdFormal(memberId))
            return new BaseResponse<>(INVALID_MEMBER);

        // 유효성 검사 게시물 고유번호
        if (isEmpty(itemPostId))
            return new BaseResponse<>(REQUEST_ERROR);

        if(!checkIdFormal(itemPostId))
            return new BaseResponse<>(INVALID_POST);


        GetItemPostReadyUpdateReq req = new GetItemPostReadyUpdateReq(itemPostId, memberId);

        try{

            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            if(memberId != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetItemPostReadyUpdateRes result = provider.getItemPostReadyUpdate(req);

            return new BaseResponse<>(result);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @PutMapping("")
    public BaseResponse<PutItemPostRes> putItemPost(@RequestBody PutItemPostReq req){

        // 회원고유번호가 빈값인지, 형식에 맞는지

        if(isEmpty(req.getWriterId()) || !(checkIdFormal(req.getWriterId())))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        // 게시물에 필수적으로 들어갈 정보들이 빈값인지

        if(isEmptyString(req.getTitle()) || isEmpty(req.getPrice()) || isEmpty(req.getCategoryId())
                || isEmptyString(req.getOfferStatus()) || isEmptyString(req.getContent()))
            return new BaseResponse<>(REQUEST_ERROR);

        // 제목 길이 수 30자 제한

        if(!checkValueLength(req.getTitle()))
            return new BaseResponse<>(TOOLONG_VALUE);

        // 내용 길이 수 1000자 제한
        if(!checkTextLength(req.getContent()))
            return new BaseResponse<>(TOOLONG_TEXT);

        // 등록할 사진들 모두 URL 길이 수 제한 및 정규식

        if(!isEmpty(req.getImgList())){
            for(PostItemPostReq_imgs img : req.getImgList()){
                if(!checkUrlLength(img.getPath()))
                    return new BaseResponse<>(TOOLONG_URL);

                if(!isRegexUrl(img.getPath()))
                    return new BaseResponse<>(INVALID_URL);
            }

        }

        // 가격이 0보다 같거나 큰지, 항목ID가 모두 양수인지

        if(!(req.getPrice()>=0) || !checkIdFormal(req.getCategoryId()))
            return new BaseResponse<>(REQUEST_ERROR);

        // 가격 제안 여부 확인

        if(!(req.getOfferStatus().equals("T") || req.getOfferStatus().equals("F")))
            return new BaseResponse<>(INVALID_OFFERSTATUS);



        try{

            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            // 유효성 검사: request로 받은 id와 추출한 id 비교
            if(req.getWriterId() != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PutItemPostRes result = service.putItemPost(req);

            return new BaseResponse<>(result);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


}

@GetMapping ("/sell/{memberId}")
public BaseResponse<List<GetItemPostSellRes>> getItemPostSell(@PathVariable Integer memberId, @RequestParam Integer postId){


    // 유효성 검사 회원고유번호
    if (isEmpty(memberId))
        return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

    if(!checkIdFormal(memberId))
        return new BaseResponse<>(INVALID_MEMBER);

    // 유효성 검사 게시물 고유번호
    if (isEmpty(postId))
        return new BaseResponse<>(REQUEST_ERROR);

    if(!checkIdFormal(postId))
        return new BaseResponse<>(INVALID_POST);



    GetItemPostSellReq req = new GetItemPostSellReq(postId, memberId);

    try{

        // jwt에서 id 추출
        int memberIdByJwt = jwtService.getMemberId();

        // 유효성 검사: request로 받은 id와 추출한 id 비교
        if(memberId != memberIdByJwt){

            return new BaseResponse<>(INVALID_USER_JWT);
        }

       List<GetItemPostSellRes> result = provider.getItemPostSell(req);

        return new BaseResponse<>(result);

    }catch (BaseException exception) {
        return new BaseResponse<>((exception.getStatus()));
    }


}


@PatchMapping("/sell")
public BaseResponse<PatchItemPostSellRes> patchItemPostSell(@RequestBody PatchItemPostSellReq req){

    // 유효성 검사 회원고유번호
    if (isEmpty(req.getMemberId()))
        return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

    if(!checkIdFormal(req.getMemberId()))
        return new BaseResponse<>(INVALID_MEMBER);

    // 유효성 검사 게시물 고유번호
    if (isEmpty(req.getPostId()))
        return new BaseResponse<>(REQUEST_ERROR);

    if(!checkIdFormal(req.getPostId()))
        return new BaseResponse<>(INVALID_POST);



    try{

        // jwt에서 id 추출
        int memberIdByJwt = jwtService.getMemberId();

        // 유효성 검사: request로 받은 id와 추출한 id 비교
        if(req.getMemberId() != memberIdByJwt){

            return new BaseResponse<>(INVALID_USER_JWT);
        }

         PatchItemPostSellRes result = service.patchItemPostSell(req);

        return new BaseResponse<>(result);

    }catch (BaseException exception) {
        return new BaseResponse<>((exception.getStatus()));
    }


}




//    public BaseResponse<> checkMemberItemPost(@RequestBody PatchItemPostReq req){
//        // 유효성 검사 회원고유번호
//        if (isEmpty(req.getMemberId()))
//            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);
//
//        if(!checkIdFormal(req.getMemberId()))
//            return new BaseResponse<>(INVALID_MEMBER);
//
//        // 유효성 검사 게시물 고유번호
//        if (isEmpty(req.getItemPostId()))
//            return new BaseResponse<>(REQUEST_ERROR);
//
//        if(!checkIdFormal(req.getItemPostId()))
//            return new BaseResponse<>(INVALID_POST);
//
//        return true;
//    }


}
