package com.example.demo;

import com.example.demo.src.itemPost.model.PostItemPostReq;
import com.example.demo.utils.SHA256;
import com.example.demo.utils.ValidationRegex;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.validation.Valid;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);

        // 메모리 사용량 출력
        long heapSize = Runtime.getRuntime().totalMemory();
        System.out.println("HEAP Size(M) : "+ heapSize / (1024*1024) + " MB");



    }


}
