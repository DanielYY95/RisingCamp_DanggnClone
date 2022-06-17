package com.example.demo.src.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostLoginRes {

    // 회원고유번호
    Integer memberId;

    String jwt;

    Integer basicTownId;

     String memberStatus;

    String nickname;


    public PostLoginRes(PostLoginRes res) {
        this.memberId = res.memberId;
        this.jwt = res.jwt;
        this.basicTownId = res.basicTownId;
        this.memberStatus = res.memberStatus;
        this.nickname = res.nickname;
    }
}
