package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.chat.model.GetChatRoomRes;
import com.example.demo.src.chat.model.PostChatReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.utils.LogicalValidationException.*;


@Service
public class ChatService {

    @Autowired
    private ChatDao dao;

    // 트랜잭션
    @Transactional(rollbackFor = Exception.class)
    public GetChatRoomRes postChat(PostChatReq req) throws BaseException {

        // 유효한가?

       if(dao.isValidMember(req.getTalkerId()) !=1)
           throw new BaseException(BaseResponseStatus.INVALID_MEMBER_REQUEST);

        checkMemberExists(dao.isValidMember(req.getBuyerId()));
        checkMemberExists(dao.isValidMember(req.getSellerId()));

        if(dao.isValidChatRoom(req.getRoomNo()) != 1)
            throw new BaseException(BaseResponseStatus.INVALID_CHATROOM);


        if(dao.isValidItemPost(req.getPostId()) !=1)
            throw new BaseException(BaseResponseStatus.INVALID_POST);



        // 값이 제대로 나왔는가?

        // 새롭게 추가된 데이터까지 조회
        GetChatRoomRes getChatRoomRes;

        try {
            // 채팅방이 없으면 새 채팅메시지가 추가될때,  만들어줘라
            if(req.getRoomNo() == null){

                // 이 떄, 생성되면서 새로 생성된 채팅방의 roomNo 또한 set된다.
                int id = dao.createChatroom(req);
            }

            // 채팅 메시지 추가
            int result = dao.createChat(req);

            if (result !=1) {
                throw new BaseException(BaseResponseStatus.FAILED_TO_CHAT);
            }

            // 채팅방의 마지막 메시지 정보 업데이트
            result = dao.updateChatRoom(req);

            if (result !=1) {
                throw new BaseException(BaseResponseStatus.FAILED_TO_CHAT);
            }
                getChatRoomRes =  dao.getChatRoomStatus(req.getRoomNo(), req.getTalkerId());
                getChatRoomRes.setChats(dao.getChatRoomChats(req.getRoomNo()));

        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }



        return getChatRoomRes;
    }



}
