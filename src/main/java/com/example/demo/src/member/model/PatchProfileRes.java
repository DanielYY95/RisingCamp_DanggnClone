package com.example.demo.src.member.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchProfileRes {


    String profile;
    String nickname;
    Integer memberId;

    public PatchProfileRes(GetProfileForUpdateRes res) {
        this.profile = res.profile;
        this.nickname = res.nickname;
        this.memberId = res.memberId;
    }
}
