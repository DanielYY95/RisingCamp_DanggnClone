package com.example.demo.src.chat.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// 채팅방
public class ChatRoom {

    // 채팅방번호
    private Integer roomNo;

    // 구매자고유번호
    private Integer buyerId;

    // 판매자고유번호
    private Integer sellerId;

    // 마지막 메시지
    private String lastChatMsg;

    // 마지막 메시지 종류
    private String lastChatMsgType;

    // 마지막 메시지 시간 current_timestamp
    private Timestamp lastChatTime;

    // 마지막메시지고유번호
    private Long lastMsgId;

    // 게시물고유번호
    private Long postId;

    // 게시물 테이블종류
    private String postTable;

}
