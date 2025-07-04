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
            
            // 如果提供了新用户名，检查是否可用
            if (taboosAndPreferenceInfo.getUsername() != null && !taboosAndPreferenceInfo.getUsername().trim().isEmpty()) {
                if (!isUsernameAvailable(taboosAndPreferenceInfo.getUsername())) {
                    throw new RuntimeException("用户名已存在，请选择其他用户名");
                }
            }
            
            // 如果提供了新密码，进行加密
            if (taboosAndPreferenceInfo.getPassword() != null && !taboosAndPreferenceInfo.getPassword().trim().isEmpty()) {
                String hashedPassword = BCrypt.hashpw(taboosAndPreferenceInfo.getPassword(), BCrypt.gensalt());
                taboosAndPreferenceInfo.setPassword(hashedPassword);
                log.info("密码已加密");
            }
            
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

    Integer getCurrentUserId(){
        return CurrentHold.getCurrentUserId();
    }
}
