package com.example.demo.src.itemPost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemPostList {

    Integer itemPostId;
    String title;
    String town;
    int price;
    String isPull;
    String pullTime;
    String path;
    int chatCnt;
    int interestCnt;



}
