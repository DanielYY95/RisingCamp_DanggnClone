package com.example.demo.src.parttime;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.parttime.model.GetParttimePostReq;
import com.example.demo.src.parttime.model.GetParttimePostRes;
import com.example.demo.utils.JwtService;
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

    @Autowired
    private final JwtService jwtService;


    public ParttimeController(ParttimeProvider provider, JwtService jwtService) {
        this.provider = provider;
        this.jwtService = jwtService;
    }

    @GetMapping("/{parttimeid}")
    public BaseResponse<GetParttimePostRes> getParttimePost(@PathVariable Integer parttimeid){
        
        // 빈 값과 형식 검사

        if(isEmpty(parttimeid) || !(checkIdFormal(parttimeid)))
            return new BaseResponse<>(INVALID_POST);




        try{

            int memberid = jwtService.getMemberId();


            GetParttimePostReq req = new GetParttimePostReq(memberid, parttimeid);

            GetParttimePostRes result = provider.getParttimePost(req);


            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }



    };


}
