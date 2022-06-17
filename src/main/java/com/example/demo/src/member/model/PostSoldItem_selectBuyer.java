package com.example.demo.src.member.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostSoldItem_selectBuyer {


    Integer sellerId;
    Integer buyerId;
    Integer roomNo;

}
