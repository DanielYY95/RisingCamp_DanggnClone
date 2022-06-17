package com.example.demo.src.chat.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostChatReq {

    Integer buyerId;
    Integer sellerId;
    String content;
    Integer roomNo;
    Integer postId;
    String postTable;
    Integer talkerId;


}
