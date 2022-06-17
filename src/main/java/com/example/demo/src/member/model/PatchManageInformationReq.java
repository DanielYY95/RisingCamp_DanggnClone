package com.example.demo.src.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchManageInformationReq {

    String email;
    String phone;
    Integer memberId;


}
