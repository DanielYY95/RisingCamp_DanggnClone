package com.example.demo.src.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    // 회원고유번호
    private Integer memberId;

    // 전화번호
    private String phone;
    
    // 비밀번호
    private String pwd;
    
    // 닉네임
    private String nickname;

    // 이메일
    private String email;

    // 동네1고유번호
    private Integer town1Id;

    // 동네2고유번호
    private Integer town2Id;

    // 대표동네고유번호
    private Integer basicTownId;

    // 프로필사진
    private String profile;

    // 회원상태 활동(a)/삭제(r)/정지(b)
    private String memberStatus;

    // 동네1인증시간
    private Timestamp town1AuthTime;

    // 동네2인증시간
    private Timestamp town2AuthTime;

    // 동네1인증횟수
    private Integer town1AuthCnt;

    // 동네2인증횟수
    private Integer town2AuthCnt;

    // 게시글 동네 자동 변경 T/F
    private String autoChange;

    // 검색엔진 허용 T/F
    private String searchAgree;

    // 방해금지시작시간 @@@@
    private Integer noDisturbFrom;

    // 방해금지종료시간 @@@@
    private Integer noDisturbTo;

    // 등록시간
    private Timestamp createdAt;

    // 수정시간
    private Timestamp updatedAt;

    // 매너온도 서비스 로직에 의해 제어
    private BigDecimal temperature;

    // 응답률 서비스 로직에 의해 제어
    private Integer responseRate;

}
