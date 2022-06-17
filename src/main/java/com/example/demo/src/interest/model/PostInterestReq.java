package com.example.demo.src.interest.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostInterestReq {


    Integer memberId;
    Integer postId;
    int hasInterest;


    Integer interestId;

}
