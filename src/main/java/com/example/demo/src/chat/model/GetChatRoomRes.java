package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetChatRoomRes {

    String bCity;
    String bDistrict;
    String bTown;
    String bProfile;
    String bNickname;
    float bTemperature;
    String bPhone;
    String sCity;
    String sDistrict;
    String sTown;
    String sProfile;
    String sNickname;
    float sTemperature;
    String sPhone;
    String path;
    String title;
    Integer price;
    String sellStatus;
    String postTable;
    Integer postId;
    Integer buyerId;
    Integer sellerId;

    List<GetChatRoomRes_Chat> chats;

    
}
