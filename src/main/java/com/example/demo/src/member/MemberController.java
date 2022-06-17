package com.example.demo.src.member;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.member.model.*;

import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpSession;

import java.util.HashMap;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.FormalValidationException.*;
import static com.example.demo.utils.ValidationRegex.*;
import static com.example.demo.utils.LogicalValidationException.*;



@RestController
@RequestMapping("/app/members")
public class MemberController {

    @Autowired
    private final MemberProvider provider;

    @Autowired
    private final MemberService service;

    @Autowired
    private final JwtService jwtService;


    KakaoAPI kakaoApi = new KakaoAPI();


    // private final은 중간에 변경되는 것을 막기위해
    
    public MemberController(MemberProvider provider, MemberService service, JwtService jwtService) {
        this.provider = provider;
        this.service = service;
        this.jwtService = jwtService;
    }
    


    @GetMapping("/cnts")
    public BaseResponse<Integer> getMemberCnt(){


        try{
            int result = provider.getMemberCnt();
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq req) {

        try {


            // 유효성 검사 1. 회원고유번호가 빈값인지
            if (isEmpty(req.getMemberId()))
                return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

            if(!checkIdFormal(req.getMemberId()))
                return new BaseResponse<>(INVALID_MEMBER);

            // 유효성 검사 2. 회원비밀번호가 빈값인지
            if (isEmptyString(req.getPwd()))
                return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_PWD);

            // 길이 수 제한

            if(!checkValueLength(req.getPwd()))
                return new BaseResponse<>(TOOLONG_VALUE);


            PostLoginRes result = provider.login(req);

            return new BaseResponse<>(result);

        }catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }

    }


    // 회원가입할 때 입력해야되는 데이터
        // 현재 위치, 전화번호, 인증번호, 닉네임, 프로필 사진

    @PostMapping("/reg")
    public BaseResponse<PostMemberRes> createUser(@RequestBody PostMemberReq postMemberReq){


        // 빈 값인지

        if(isEmpty(postMemberReq.getPhone()) || isEmptyString(postMemberReq.getNickname())
                || isEmpty(postMemberReq.getTown1Id()) || isEmpty(postMemberReq.getBasicTownId()) || isEmptyString(postMemberReq.getPwd()))
            return new BaseResponse<>(REQUEST_ERROR);

        // 길이 수 제한
        if(!checkValueLength(postMemberReq.getPhone()) || !checkValueLength(postMemberReq.getNickname()) || !checkValueLength(postMemberReq.getPwd()))
            return new BaseResponse<>(TOOLONG_VALUE);

        // 핸드폰 번호 유효성
        if(!(isRegexPhone(postMemberReq.getPhone()))){
            return new BaseResponse<>(POST_MEMBERS_INVALID_PHONE);
        }
        // 닉네임 유효성
        if(!(isRegexNickname(postMemberReq.getNickname()))){
            return new BaseResponse<>(POST_MEMBERS_INVALID_NICKNAME);
        }


        // 프로필 사진이 있을 때에만 진행
        if(!isEmptyString(postMemberReq.getProfile())) {

            if (!checkUrlLength(postMemberReq.getProfile()))
                return new BaseResponse<>(TOOLONG_URL);

            if (!isRegexUrl(postMemberReq.getProfile()))
                return new BaseResponse<>(INVALID_URL);
        }

        // Url의 경우, null값으로 그대로 두지 말자
        if(postMemberReq.getProfile() == null) postMemberReq.setProfile("");


        // 동네1과 설정 동네 유효성
        if(postMemberReq.getTown1Id() != postMemberReq.getBasicTownId()){
            return new BaseResponse<>(POST_MEMBERS_INVALID_TOWN);
        }
        
        // 동네 1과 설정 동네에 모두 올바른 값이 들어갔는지
        if(!(postMemberReq.getTown1Id()>0 && postMemberReq.getBasicTownId()>0 )){
            return new BaseResponse<>(POST_MEMBERS_INVALID_TOWN);
        }



        try{
            PostMemberRes result = service.createUser(postMemberReq);


            return new BaseResponse<>(result);
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    // 배운 점
        // @RequestBody 를 쓰면 post headers 에서 application/json을 써야한다.


    @GetMapping("")
    public BaseResponse<GetProfileRes> getProfile(@RequestParam("memberId") Integer memberId){
        
        // 회원 고유번호
        
        if(isEmpty(memberId) || !(checkIdFormal(memberId)))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        try{

            GetProfileRes result = provider.getProfile(memberId);

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    };


    @GetMapping("/profile/{memberId}")
    public BaseResponse<GetProfileForUpdateRes> getProfileForUpdate(@PathVariable Integer memberId){

        // 유효성 검사 : 회원고유번호가 빈값인지
        if(isEmpty(memberId))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        // 유효성 검사: 회원고유번호가 양수인지
        if(!(checkIdFormal(memberId)))
            return new BaseResponse<>(INVALID_MEMBER);

        try{
            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            // 유효성 검사: request로 받은 id와 추출한 id 비교
            if(memberId != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

        GetProfileForUpdateRes res = provider.getProfileForUpdate(memberId);


         return new BaseResponse<>(res);

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));

        }

    }


    @PatchMapping("/profile")
    public BaseResponse<PatchProfileRes> patchProfile(@RequestBody PatchProfileReq req){
        
        // 유효성 검사 : 회원고유번호가 빈값인지
        if(isEmpty(req.getMemberId()))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);
        
        // 유효성 검사: 회원고유번호가 양수인지
        if(!(checkIdFormal(req.getMemberId())))
            return new BaseResponse<>(INVALID_MEMBER);

        // 닉네임이 빈 값인지
        if(isEmpty(req.getNickname()))
            return new BaseResponse<>(EMPTY_NICKNAME);
        
        // 닉네임의 정규식 검사
        if(!(isRegexNickname(req.getNickname())))
            return new BaseResponse<>(POST_MEMBERS_INVALID_NICKNAME);
        
        
        // 길이에 대한 유효성
        if(checkValueLength(req.getNickname()))
            return new BaseResponse<>(TOOLONG_VALUE);

        if(checkUrlLength(req.getProfile()))
            return new BaseResponse<>(TOOLONG_URL);


        // 사진 경로가 null이 아니라면, url형태인지???
        if(isEmpty(req.getProfile()!=null)){

            if(isRegexUrl(req.getProfile()))
                return new BaseResponse<>(INVALID_URL);
        }


        try{
            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();
            
            // 유효성 검사: request로 받은 id와 추출한 id 비교
            if(req.getMemberId() != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchProfileRes res = service.patchProfile(req);

            return new BaseResponse<>(res);

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));

        }

    }


    @GetMapping("/manage/{memberId}")
    public BaseResponse<GetManageInformationRes> getManageInformation(@PathVariable Integer memberId){

        // 유효성 검사 : 회원고유번호가 빈값인지
        if(isEmpty(memberId))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        // 유효성 검사: 회원고유번호가 양수인지
        if(!(checkIdFormal(memberId)))
            return new BaseResponse<>(INVALID_MEMBER);




        try {

            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            // 유효성 검사: request로 받은 id와 추출한 id 비교
            if(memberId != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetManageInformationRes res = provider.getManageInformation(memberId);

            return new BaseResponse<>(res);

        }catch (BaseException exception){

            return new BaseResponse<>(exception.getStatus());
        }


    }


    @PatchMapping("/manage")
    public BaseResponse<PatchManageInformationRes> patchManageInformation(@RequestBody PatchManageInformationReq req){

        // 유효성 검사 : 회원고유번호가 빈값인지
        if(isEmpty(req.getMemberId()))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        // 유효성 검사: 회원고유번호가 양수인지
        if(!(checkIdFormal(req.getMemberId())))
            return new BaseResponse<>(INVALID_MEMBER);


        if((isEmpty(req.getPhone()) && isEmpty(req.getEmail()))){

            return new BaseResponse<>(REQUEST_ERROR);
        }

        if((!isEmpty(req.getPhone()) && !isEmpty(req.getEmail()))){

            return new BaseResponse<>(REQUEST_ERROR);
        }





        // 핸드폰 번호가 빈 값이 아니면, 핸드폰 번호의 정규식 검사
        if(!isEmpty(req.getPhone())) {

            if(!checkValueLength(req.getPhone()))
                return new BaseResponse<>(TOOLONG_VALUE);


            if (!(isRegexPhone(req.getPhone())))
                return new BaseResponse<>(POST_MEMBERS_INVALID_PHONE);
        }

        // 이메일이 빈 값이 아닐 때만, 이메일의 형식 검사
        if(!isEmpty(req.getEmail())) {


            if(!checkValueLength(req.getEmail()))
                return new BaseResponse<>(TOOLONG_VALUE);


            if (!checkEmailFormal(req.getEmail()))
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        try {

            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            // 유효성 검사: request로 받은 id와 추출한 id 비교
            if(req.getMemberId() != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchManageInformationRes res = service.patchManageInformation(req);

            return new BaseResponse<>(res);
        }catch (BaseException exception){

            return new BaseResponse<>(exception.getStatus());
        }

    }





    // 프론트의 a href https://kauth.kakao.com/oauth/authorize?client_id=bfec17b4484ed8e3b54f03b74f550cc2&redirect_uri=http://localhost:8000/app/members/login/kakao&response_type=code

    // Get 메서드는 supported 라서

    // access token 받는 것 까지 ok

    @PostMapping("/login/kakao")
    public KakaoLoginRes kakaoLogin(@RequestParam("code") String code, HttpSession session) {


        KakaoLoginRes res  = new KakaoLoginRes();

        // 1번 인증코드 요청 전달
        String accessToken = kakaoApi.getAccessToken(code);
        // 2번 인증코드로 토큰 전달
        HashMap<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

        System.out.println("login info : " + userInfo.toString());


        if(userInfo.get("email") != null) {

            res.setEmail(userInfo.get("email"));
            res.setAccessToken(accessToken);

        }


        return res;
    }



}
