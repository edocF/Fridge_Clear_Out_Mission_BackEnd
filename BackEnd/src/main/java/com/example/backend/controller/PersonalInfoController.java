package com.example.backend.controller;

import com.example.backend.pojo.Result;
import com.example.backend.pojo.TaboosAndPreferenceInfo;
import com.example.backend.pojo.User;
import com.example.backend.service.PersonalInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/personalInfo")
public class PersonalInfoController {
    @Autowired
    private PersonalInfoService personalInfoService;

    @GetMapping()
    public Result getPersonalInfo() {
       User user =  personalInfoService.getPersonalInfo();
       log.info("user: {}", user);
       return Result.success(user);
    }

    @PostMapping()
    public Result updatePersonalInfo(@RequestBody TaboosAndPreferenceInfo taboosAndPreferenceInfo) {
        log.info("taboosAndPreferenceInfo: {}", taboosAndPreferenceInfo);
        personalInfoService.updatePersonalInfo(taboosAndPreferenceInfo);
        return Result.success();
    }

}
