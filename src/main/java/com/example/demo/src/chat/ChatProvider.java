package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.chat.model.GetChatRoomRes;
import com.example.demo.src.chat.model.GetChatRoomRes_Chat;
import com.example.demo.src.chat.model.GetChatRoomsRes;
import com.example.demo.src.chat.model.PostChatReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static com.example.demo.utils.LogicalValidationException.*;
import static com.example.demo.utils.FormalValidationException.*;

@Service
public class ChatProvider {

    @Autowired
    private ChatDao dao;

    public List<GetChatRoomsRes> getChatRooms(Integer memberId) throws BaseException{

        checkMemberExists(memberId);

        try {

            List<GetChatRoomsRes> res = dao.getChatRooms(memberId);

            if(isEmpty(res))
                throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);

            return res;

        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }


    };

    // 트랜잭션
    @Transactional(rollbackFor = Exception.class)
    public GetChatRoomRes getChatRoom(Integer roomNo, Integer memberId) throws BaseException{

        if(dao.isValidMember(memberId) != 1){
            throw new BaseException(BaseResponseStatus.INVALID_MEMBER_REQUEST);
        }

        if(dao.isValidChatRoom(roomNo) !=1){
            throw new BaseException(BaseResponseStatus.INVALID_CHATROOM);
        }

        try {

            GetChatRoomRes getChatRoomRes = dao.getChatRoomStatus(roomNo, memberId);

            if(isEmpty(getChatRoomRes))
                throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);

            List<GetChatRoomRes_Chat> chats = dao.getChatRoomChats(roomNo);

            if(isEmpty(chats))
                throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);

            getChatRoomRes.setChats(chats);


            dao.updateIsRead(roomNo, memberId);

            return getChatRoomRes;

        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }


    }

    public GetChatRoomRes getChatRoomReady(Integer itemPostId, Integer memberId) throws BaseException {

        checkMemberExists(memberId);

        if(dao.isValidItemPost(itemPostId) !=1){
            throw new BaseException(BaseResponseStatus.INVALID_POST);
        }

        try {

            GetChatRoomRes res = dao.getChatRoomReady(itemPostId, memberId);

            if(isEmpty(res))
                throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);

            return res;

        }catch (Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }


    }


}
