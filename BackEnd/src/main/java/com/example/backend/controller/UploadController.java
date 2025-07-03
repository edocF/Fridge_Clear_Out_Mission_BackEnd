package com.example.backend.controller;

import com.example.backend.pojo.Result;
import com.example.backend.utils.AliyunOSSOperator;
import com.example.backend.utils.CurrentHold;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    @PostMapping()
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
           log.info("name:{}",file);
           if(!file.isEmpty()) {
               String originalFilename = file.getOriginalFilename();
               String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
               String uniqueFileName = "avatar/" + getCurrentUserId() + "/" + UUID.randomUUID() + suffix;
               String url = aliyunOSSOperator.upload(file.getBytes(), uniqueFileName);
               return Result.success(url);
           }
           return Result.error();
       }

    private String getCurrentUserId() {
        return CurrentHold.getCurrentUserId().toString();
    }
}

