package com.example.demo.src.interest;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.interest.model.PostInterestReq;
import com.example.demo.src.interest.model.PostInterestRes;
import com.example.demo.src.itemPost.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.RESPONSE_ERROR;
import static com.example.demo.utils.FormalValidationException.isEmpty;
import static com.example.demo.utils.LogicalValidationException.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class InterestProvider {

    @Autowired
    private final InterestDao dao;

    private final JwtService jwtService;


    public InterestProvider(InterestDao dao, JwtService jwtService) {
        this.dao = dao;
        this.jwtService = jwtService;
    }



}
