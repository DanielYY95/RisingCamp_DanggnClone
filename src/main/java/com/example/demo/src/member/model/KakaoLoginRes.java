package com.example.demo.src.member.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginRes {

    String nickname;
    Object email;
    String accessToken;


}
