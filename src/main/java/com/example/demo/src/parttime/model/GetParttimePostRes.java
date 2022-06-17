package com.example.demo.src.parttime.model;

import com.example.demo.src.itemPost.model.GetItemPostRes_imgs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetParttimePostRes {
    Integer parttimeId;
    String title;
    String companyName;
    Integer ownerId;
    String pullCnt;
    String createdAt;
    String wageMethod;
    Integer wage;
    String day;
    String time;
    String content;
    String location;
    int viewCnt;
    int applyCnt;
    int interestCnt;
    String phone;
    boolean isInterest;
    boolean isApplied;

    List<GetParttimePostRes_imgs> imgs;



}
