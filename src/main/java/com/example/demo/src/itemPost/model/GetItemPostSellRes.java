package com.example.demo.src.itemPost.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetItemPostSellRes {

    Integer roomNo;

    Integer buyerId;
    String profile;
    String nickname;
    String lastChatTime;
    String town;



}
