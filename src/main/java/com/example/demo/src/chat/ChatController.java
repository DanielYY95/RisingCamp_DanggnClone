package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;

import com.example.demo.src.chat.model.GetChatRoomRes;
import com.example.demo.src.chat.model.GetChatRoomsRes;
import com.example.demo.src.chat.model.PostChatReq;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.FormalValidationException.*;


@RestController
@RequestMapping("/app/chats")
public class ChatController {

    @Autowired
    private ChatProvider provider;

    @Autowired
    private ChatService service;

    @Autowired
    private final JwtService jwtService;


    public ChatController(ChatProvider provider, ChatService service, JwtService jwtService) {
        this.provider = provider;
        this.service = service;
        this.jwtService = jwtService;
    }

    @GetMapping("/{memberid}/rooms")
    public BaseResponse<List<GetChatRoomsRes>> getChatRooms(@RequestParam Integer memberid){

        if(isEmpty(memberid) || !(checkIdFormal(memberid)))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);


        try{

            // jwt에서 id 추출
            int memberidByJwt = jwtService.getMemberId();

            // 유효성 검사: request로 받은 id와 추출한 id 비교
            if(memberid != memberidByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetChatRoomsRes> result = provider.getChatRooms(memberid);
            return new BaseResponse<>(result);

        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @GetMapping("/{memberid}/{roomno}")
    public BaseResponse<GetChatRoomRes> getChatRoom(@PathVariable Integer memberid, @PathVariable Integer roomno){

        if(isEmpty(roomno) || !(checkIdFormal(roomno)))
            return new BaseResponse<>(INVALID_CHATROOM);


        if(isEmpty(memberid) || !(checkIdFormal(memberid)))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);


        try{

            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            // 유효성 검사: request로 받은 id와 추출한 id 비교
            if(memberid != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetChatRoomRes result = provider.getChatRoom(roomno, memberid);
            return new BaseResponse<>(result);

        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @GetMapping("/{itempostid}/{memberid}")
    public BaseResponse<GetChatRoomRes> getChatRoomReady(@PathVariable Integer itempostid, @PathVariable Integer memberid){


        if(isEmpty(itempostid) || !(checkIdFormal(itempostid)))
            return new BaseResponse<>(INVALID_POST);

        if(isEmpty(memberid) || !(checkIdFormal(memberid)))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        try{
            GetChatRoomRes result = provider.getChatRoomReady(itempostid, memberid);
            return new BaseResponse<>(result);

        }catch (BaseException exception){
        return new BaseResponse<>(exception.getStatus());
    }



    }



    @PostMapping ("")
    public BaseResponse<GetChatRoomRes> postChat(@RequestBody PostChatReq req){

        // 빈값인지 확인

        if(isEmpty(req.getBuyerId()) || isEmpty(req.getSellerId())
                || isEmpty(req.getRoomNo()) || isEmpty(req.getPostId())
                || isEmptyString(req.getPostTable()) || isEmpty(req.getTalkerId()))
                return new BaseResponse<>(REQUEST_ERROR);

        
        if(isEmptyString(req.getContent()))
            return new BaseResponse<>(EMPTY_CONTENT);
        
        
        // 길이 수 제한 검사

        if(!checkTextLength(req.getContent()))
            return new BaseResponse<>(TOOLONG_TEXT);

        if(!checkValueLength(req.getPostTable()))
            return new BaseResponse<>(TOOLONG_VALUE);



        // 형식 검사
        if(!checkIdFormal(req.getBuyerId()) || !checkIdFormal(req.getSellerId())
                || !checkIdFormal(req.getTalkerId()) || !checkIdFormal(req.getPostId()) || !checkIdFormal(req.getRoomNo()))
            return new BaseResponse<>(REQUEST_ERROR);


        try {

            // jwt에서 id 추출
            int memberIdByJwt = jwtService.getMemberId();

            if(req.getTalkerId() != memberIdByJwt){

                return new BaseResponse<>(INVALID_USER_JWT);
            }


            GetChatRoomRes result = service.postChat(req);

            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }




}
