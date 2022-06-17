package com.example.demo.src.keyword.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetKeywordAlarmsRes {

    Integer keywordAlarmId;
    Long postId;
    String imgPath;
    String keyword;
    String name;
    String title;
    String createdAt;



}
