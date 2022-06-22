package com.example.demo.src.keyword;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.keyword.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.FormalValidationException.*;

@RestController
@RequestMapping("/app/keywords")
public class KeywordController {

    @Autowired
    private KeywordProvider provider;

    @Autowired
    private KeywordService service;

    @Autowired
    private final JwtService jwtService;

    public KeywordController(KeywordProvider provider, KeywordService service, JwtService jwtService) {
        this.provider = provider;
        this.service = service;
        this.jwtService = jwtService;
    }

// @@ 일때 return new @@@
    // @@ 일때 throw new BaseException


    @GetMapping("/set/{memberid}")
    public BaseResponse<List<GetKeywordSetRes>> getKeywordSet(@PathVariable Integer memberid){

        // 회원번호 빈 값, 유효한지
        if(isEmpty(memberid))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        if(!(checkIdFormal(memberid)))
            return new BaseResponse<>(INVALID_MEMBER);

        try {
            List<GetKeywordSetRes> res = provider.getKeywordSet(memberid);

            return new BaseResponse<>(res);
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }

    @GetMapping("/alarms/{memberid}")
    public BaseResponse<List<GetKeywordAlarmsRes>> getKeywordAlarms(@PathVariable Integer memberid) {

        // 회원번호 빈 값, 유효한지
        if(isEmpty(memberid))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        if(!(checkIdFormal(memberid)))
            return new BaseResponse<>(INVALID_MEMBER);

        try{
            List<GetKeywordAlarmsRes> res = provider.getKeywordAlarms(memberid);

            return new BaseResponse<>(res);

        }catch (BaseException exception) {
                return new BaseResponse<>((exception.getStatus()));
        }

    };



    @PatchMapping("/set")
    public BaseResponse<DeleteKeywordSetRes> deleteKeywordSet(@RequestBody DeleteKeywordSetReq req){

        // 회원 고유번호 유효성 검사
        if(isEmpty(req.getMemberId()))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);
        
        if(!(checkIdFormal(req.getMemberId())))
            return new BaseResponse<>(INVALID_MEMBER);


        // 키워드고유번호 유효성 검사
        if(isEmpty(req.getKeywordSetId()) || !(checkIdFormal(req.getKeywordSetId())))
            return new BaseResponse<>(REQUEST_ERROR);


        try{

            // 유효성 검사: request로 받은 id와 추출한 id 비교
            int memberIdByJwt = jwtService.getMemberId();
            if(req.getMemberId() != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }


            DeleteKeywordSetRes res = service.deleteKeywordSet(req);

            return new BaseResponse<>(res);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    };


    @PostMapping("/set")
    public BaseResponse<PostKeywordSetRes> postKeywordSet(@RequestBody PostKeywordSetReq req){

        // 회원 고유번호 유효성 검사
        if(isEmpty(req.getMemberId()))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        if(!(checkIdFormal(req.getMemberId())))
            return new BaseResponse<>(INVALID_MEMBER);

        // 키워드 내용

        if(!checkValueLength(req.getName()))
            return new BaseResponse<>(TOOLONG_VALUE);



        // 동네1 혹은 동네2
            // 동네Id 모두 null이거나 모두 양수가 아닌 경우
        
        if(isEmpty(req.getTown1Id()) && isEmpty(req.getTown2Id()))
            return new BaseResponse<>(POST_MEMBERS_INVALID_TOWN);

        // 동네번호가 있는데, 유효성 안 맞으면 예외 처리
        if(!isEmpty(req.getTown1Id())){
            if(!checkIdFormal(req.getTown1Id()))
                return new BaseResponse<>(POST_MEMBERS_INVALID_TOWN);
        }

        if(!isEmpty(req.getTown2Id())){
            if(!checkIdFormal(req.getTown2Id()))
                return new BaseResponse<>(POST_MEMBERS_INVALID_TOWN);
        }



        try {

            // 유효성 검사: request로 받은 id와 추출한 id 비교
            int memberIdByJwt = jwtService.getMemberId();
            if(req.getMemberId() != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }


            PostKeywordSetRes res = service.postKeywordSet(req);

            return new BaseResponse<>(res);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }


}
