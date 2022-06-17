package com.example.demo.src.interest;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.interest.model.PostInterestReq;
import com.example.demo.src.interest.model.PostInterestRes;
import com.example.demo.src.itemPost.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.FormalValidationException.*;
import static com.example.demo.utils.ValidationRegex.isRegexUrl;


@RestController
@RequestMapping("/app/interests")
public class InterestController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final InterestProvider provider;

    @Autowired
    private final InterestService service;

    @Autowired
    private final JwtService jwtService;


    public InterestController(InterestProvider provider, InterestService service, JwtService jwtService) {
        this.provider = provider;
        this.service = service;
        this.jwtService = jwtService;
    }


    @PostMapping("")
    public BaseResponse<PostInterestRes> postInterest(@RequestBody PostInterestReq req){

        // 유효성 검사 회원고유번호
        if (isEmpty(req.getMemberId()))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        if(!checkIdFormal(req.getMemberId()))
            return new BaseResponse<>(INVALID_MEMBER);

        // 유효성 검사 게시물 고유번호
        if (isEmpty(req.getPostId()) || !(req.getHasInterest() == 1 || req.getHasInterest() == 0))
            return new BaseResponse<>(REQUEST_ERROR);

        if(!checkIdFormal(req.getPostId()))
            return new BaseResponse<>(INVALID_POST);



        try{

            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            if(req.getMemberId() != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostInterestRes result = service.postInterest(req);

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

}


