package com.example.demo.src.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetProfileRes {
    String nickname;
    String memberId;;
    String t1name;
    int town1AuthCnt;
    String t2name;
    int town2AuthCnt;
    String createdAt;
    int bcount;
    int icount;
    float temperature;
    float responseRate;
    int ecount;
    int eccount;



}
