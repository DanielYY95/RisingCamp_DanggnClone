package com.example.demo.src.itemPost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class GetItemPostSearchRes {

    List<ItemPostList> titleSearchList;
    List<ItemPostList> contentSearchList;


}
