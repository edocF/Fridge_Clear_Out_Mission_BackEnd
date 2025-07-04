package com.example.backend.controller;

import com.example.backend.pojo.Result;
import com.example.backend.pojo.TaboosAndPreferenceInfo;
import com.example.backend.pojo.User;
import com.example.backend.service.PersonalInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/personalInfo")
public class PersonalInfoController {
    @Autowired
    private PersonalInfoService personalInfoService;

    @GetMapping()
    public Result getPersonalInfo() {
       User user =  personalInfoService.getPersonalInfo();
       return Result.success(user);
    }

    @PostMapping()
    public Result updatePersonalInfo(@RequestBody TaboosAndPreferenceInfo taboosAndPreferenceInfo) {
        try {
            log.info("收到更新个人信息请求: {}", taboosAndPreferenceInfo);
            personalInfoService.updatePersonalInfo(taboosAndPreferenceInfo);
            log.info("个人信息更新成功");
            return Result.success();
        } catch (Exception e) {
            log.error("更新个人信息失败", e);
            return Result.error("更新个人信息失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/avatar")//支持头像更新
    public Result updateAvatar(@RequestBody Map<String, String> request) {
        String imageUrl = request.get("image");
        personalInfoService.updateAvatar(imageUrl);
        return Result.success();
    }

    @PostMapping("/update-username")
    public Result updateUsername(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        personalInfoService.updateUsername(username);
        return Result.success();
    }

    @PostMapping("/update-password")
    public Result updatePassword(@RequestBody Map<String, String> req) {
        String password = req.get("password");
        personalInfoService.updatePassword(password);
        return Result.success();
    }

    @GetMapping("/test")
    public Result testConnection() {
        try {
            User user = personalInfoService.getPersonalInfo();
            return Result.success(user);
        } catch (Exception e) {
            log.error("测试连接失败", e);
            return Result.error("测试连接失败: " + e.getMessage());
        }
    }
}
