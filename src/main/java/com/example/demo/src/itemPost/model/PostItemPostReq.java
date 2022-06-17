package com.example.demo.src.itemPost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostItemPostReq {

    Integer itemPostId;

    Integer memberId;

    String title;

    int price;

    int categoryId;

    String offerStatus;

    String content;

    int townId;

    List<PostItemPostReq_imgs> imgList;


}
