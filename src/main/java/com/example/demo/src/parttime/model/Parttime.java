package com.example.demo.src.parttime.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// 당근알바 당근 알바 게시물
public class Parttime {

    // 당근알바고유번호
    private Integer parttimeId;

    // 고용주고유번호 회원고유번호
    private Integer ownerId;

    // 고용회사명
    private String companyName;

    // 제목
    private String title;

    // 임금방식 시급/일급/주급/월급
    private String wageMethod;

    // 임금 원 단위
    private Integer wage;

    // 요일
    private String day;

    // 시간
    private String time;

    // 내용
    private String content;

    // 주소
    private String location;

    // 전화번호
    private String phone;

    // 조회수
    private Integer viewCnt;

    // 등록시간
    private Timestamp createdAt;

    // 수정시간
    private Timestamp updatedAt;

    // 게시물상태 존재(1)/삭제(2)
    private Integer postStatus;

    // 끌올 시간
    private Timestamp pullTime;

    // 끌올 횟수
    private Integer pullCnt;



}
