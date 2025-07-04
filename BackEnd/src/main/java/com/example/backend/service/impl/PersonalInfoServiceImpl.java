package com.example.backend.service.impl;

import com.example.backend.mapper.PersonalInfoMapper;
import com.example.backend.pojo.TaboosAndPreferenceInfo;
import com.example.backend.pojo.User;
import com.example.backend.service.PersonalInfoService;
import com.example.backend.utils.CurrentHold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;


@Slf4j
@Service
public class PersonalInfoServiceImpl implements PersonalInfoService {

    @Autowired
    PersonalInfoMapper personalInfoMapper;

    @Override
    public User getPersonalInfo() {
        return personalInfoMapper.getAll(getCurrentUserId());
    }

    @Override
    public void updatePersonalInfo(TaboosAndPreferenceInfo taboosAndPreferenceInfo) {
        try {
            log.info("更新个人信息: {}", taboosAndPreferenceInfo.toString());
            Integer userId = getCurrentUserId();
            log.info("当前用户ID: {}", userId);
            // 只处理饮食相关字段，去除用户名和密码相关逻辑
            personalInfoMapper.updatePersonalInfo(userId, taboosAndPreferenceInfo);
            log.info("个人信息更新成功");
        } catch (Exception e) {
            log.error("更新个人信息失败", e);
            throw new RuntimeException("更新个人信息失败: " + e.getMessage());
        }
    }
    
    @Override
    public void updateAvatar(String imageUrl) {//支持头像更新
        personalInfoMapper.updateAvatar(getCurrentUserId(), imageUrl);
    }
    
    @Override
    public boolean isUsernameAvailable(String username) {
        Integer userId = getCurrentUserId();
        int count = personalInfoMapper.checkUsernameExists(username, userId);
        return count == 0;
    }

    @Override
    public void updateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (!isUsernameAvailable(username)) {
            throw new RuntimeException("用户名已存在，请选择其他用户名");
        }
        Integer userId = getCurrentUserId();
        log.info("开始更新用户名 - 用户ID: {}, 新用户名: {}", userId, username);
        
        // 验证用户ID是否正确
        if (userId == null) {
            log.error("用户ID为空，无法更新用户名");
            throw new RuntimeException("用户ID为空，请重新登录");
        }
        
        // 先查询当前用户信息
        User currentUser = personalInfoMapper.getAll(userId);
        if (currentUser == null) {
            log.error("未找到用户信息 - 用户ID: {}", userId);
            throw new RuntimeException("用户信息不存在");
        }
        log.info("当前用户信息 - 用户ID: {}, 当前用户名: {}", userId, currentUser.getUsername());
        
        try {
            personalInfoMapper.updateUsername(userId, username);
            log.info("用户名更新成功 - 用户ID: {}, 新用户名: {}", userId, username);
            
            // 验证更新是否成功
            User updatedUser = personalInfoMapper.getAll(userId);
            log.info("更新后用户信息 - 用户ID: {}, 新用户名: {}", userId, updatedUser.getUsername());
            
        } catch (Exception e) {
            log.error("用户名更新失败 - 用户ID: {}, 新用户名: {}, 错误: {}", userId, username, e.getMessage(), e);
            throw new RuntimeException("用户名更新失败: " + e.getMessage());
        }
    }

    @Override
    public void updatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
        Integer userId = getCurrentUserId();
        personalInfoMapper.updatePassword(userId, hashedPassword);
    }

    Integer getCurrentUserId(){
        return CurrentHold.getCurrentUserId();
    }
}
