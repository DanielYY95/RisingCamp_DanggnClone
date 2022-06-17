package com.example.demo.src.keyword.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.annotations.Select;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetKeywordSetRes {


    Integer keywordSetId;
    Integer keywordId;
    String name;


}
