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
// 채팅 모든 게시물의 채팅
public class Chat {

    // 채팅고유번호
    private Long chatid;

    // 채팅방번호
    private Integer roomno;

    // 구매자고유번호
    private Integer buyerid;

    // 판매자고유번호
    private Integer sellerid;

    // 대화자고유번호
    private Integer talkerid;

    // 채팅내용
    private String content;

    // 등록시간
    private Timestamp createdat;

    // 상태 존재(1)/삭제(2)
    private Boolean status;

    // 읽음여부 읽음(T)/안읽음(F)
    private String read;

    // 게시물고유번호
    private Long postid;

    // 게시물종류
    private String posttable;

}
