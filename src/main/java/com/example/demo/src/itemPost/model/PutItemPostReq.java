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
public class PutItemPostReq {

    Integer itemPostId;
    Integer writerId;
    String title;
    String content;
    Integer price;
    String offerStatus;
    Integer categoryId;

    List<PostItemPostReq_imgs> imgList;


}
