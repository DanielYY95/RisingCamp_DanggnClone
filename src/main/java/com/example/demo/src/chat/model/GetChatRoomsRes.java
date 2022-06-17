package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetChatRoomsRes {

    String bProfile;
    String bNickname;
    String bCity;
    String bDistrict;
    String bTown;
    String sProfile;
    String sNickname;
    String sCity;
    String sDistrict;
    String sTown;
    int readCnt;
    String lastChatMsg;
    String lastChatMsgType;
    String lastChatTime;
    String path;




}
