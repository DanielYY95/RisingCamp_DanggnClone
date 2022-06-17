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
public class GetMemberRes {
    // 회원고유번호
    private Integer memberId;

    // 닉네임
    private String nickname;

    // 대표동네고유번호
    private Integer basicTownId;

    // 회원상태 활동(a)/삭제(r)/정지(b)
    private String memberStatus;

}
