package com.example.demo.src.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostMemberRes {

    // 회원고유번호
    private Integer memberId;

    // 닉네임
    private String nickname;

    // jwt
    private String jwt;

    // 대표동네고유번호
    private Integer basicTownId;

    // 회원상태 활동(a)/삭제(r)/정지(b)
    private String memberStatus;


    public PostMemberRes(PostLoginRes res) {
        this.memberId = res.memberId;
        this.nickname = res.nickname;
        this.jwt = res.jwt;
        this.basicTownId = res.basicTownId;
        this.memberStatus = res.memberStatus;
    }
}
