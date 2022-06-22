package com.example.demo.src.parttime;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.parttime.model.GetParttimePostReq;
import com.example.demo.src.parttime.model.GetParttimePostRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.FormalValidationException.checkIdFormal;
import static com.example.demo.utils.FormalValidationException.isEmpty;

@RestController
@RequestMapping("/app/parttimes")
public class ParttimeController {

    @Autowired
    private ParttimeProvider provider;



    @GetMapping("/{parttimeid}")
    public BaseResponse<GetParttimePostRes> getParttimePost(@PathVariable Integer parttimeid, @RequestParam("memberId") Integer memberId){
        
        // 빈 값과 형식 검사
        
        if(isEmpty(memberId) || !(checkIdFormal(memberId)))
            return new BaseResponse<>(MEMBERS_EMPTY_MEMBER_ID);

        if(isEmpty(parttimeid) || !(checkIdFormal(parttimeid)))
            return new BaseResponse<>(INVALID_POST);


        GetParttimePostReq req = new GetParttimePostReq(memberId, parttimeid);

        try{
            GetParttimePostRes result = provider.getParttimePost(req);


            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }



    };


}
