package com.example.demo.src.keyword.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteKeywordSetReq {

    Integer memberId;
    Integer keywordSetId;


}
