package com.example.demo.src.itemPost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetItemPostRes {

    Integer itemPostId;
    Integer writerId;
    String profile;
    String nickname;
    float temperature;
    String title;
    String content;
    String district;
    String town;
    Integer price;
    String offerStatus;
    String sellStatus;
    String openStatus;
    String postStatus;
    String isPull;
    String pullTime;
    String category;
    Integer chatCnt;
    Integer interestCnt;
    Integer viewCnt;
    Integer hasInterest;

    List<GetItemPostRes_imgs> imgList;
    List<GetItemPostRes_otherItems> otherItems;

}
