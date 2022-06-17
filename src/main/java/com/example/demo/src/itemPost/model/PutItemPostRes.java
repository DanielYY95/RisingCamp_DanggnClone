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
public class PutItemPostRes {

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

    public PutItemPostRes(GetItemPostRes res) {
        this.itemPostId = res.itemPostId;
        this.writerId = res.writerId;
        this.profile = res.profile;
        this.nickname = res.nickname;
        this.temperature = res.temperature;
        this.title = res.title;
        this.content = res.content;
        this.district = res.district;
        this.town = res.town;
        this.price = res.price;
        this.offerStatus = res.offerStatus;
        this.sellStatus = res.sellStatus;
        this.openStatus = res.openStatus;
        this.postStatus = res.postStatus;
        this.isPull = res.isPull;
        this.pullTime = res.pullTime;
        this.category = res.category;
        this.chatCnt = res.chatCnt;
        this.interestCnt = res.interestCnt;
        this.viewCnt = res.viewCnt;
        this.hasInterest = res.hasInterest;
        this.imgList = res.imgList;
        this.otherItems = res.otherItems;
    }

}
