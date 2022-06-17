package com.example.demo.src.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProfileForUpdateRes {

    String profile;
    String nickname;
    Integer memberId;

}
