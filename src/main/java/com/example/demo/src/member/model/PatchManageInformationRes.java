package com.example.demo.src.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchManageInformationRes {

    String email;
    String phone;
    Integer memberId;

    public PatchManageInformationRes(GetManageInformationRes res) {
        this.email = res.email;
        this.phone = res.phone;
        this.memberId = res.memberId;
    }
}
