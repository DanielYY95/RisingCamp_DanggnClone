package com.example.demo.src.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostMemberReq {

    // 전화번호
    private String phone;

    // 닉네임
    private String nickname;

    // 동네1고유번호
    private Integer town1Id;

    // 대표동네고유번호
    private Integer basicTownId;

    // 프로필사진
    private String profile;

    // 비밀번호
    private String pwd;


    // 저장용 회원ID
    private Integer memberId;


}
