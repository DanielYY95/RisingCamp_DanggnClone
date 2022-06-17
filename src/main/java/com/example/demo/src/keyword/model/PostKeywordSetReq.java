package com.example.demo.src.keyword.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostKeywordSetReq {


    private Integer memberId;

    private String name;

    private Integer town1Id;

    private Integer town2Id;


}
