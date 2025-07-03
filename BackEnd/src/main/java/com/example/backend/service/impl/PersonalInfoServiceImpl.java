package com.example.backend.service.impl;

import com.example.backend.mapper.PersonalInfoMapper;
import com.example.backend.pojo.TaboosAndPreferenceInfo;
import com.example.backend.pojo.User;
import com.example.backend.service.PersonalInfoService;
import com.example.backend.utils.CurrentHold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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
        log.info(taboosAndPreferenceInfo.toString());
        personalInfoMapper.updatePersonalInfo(getCurrentUserId(),taboosAndPreferenceInfo);
        return ;
    }

    Integer getCurrentUserId(){
        return CurrentHold.getCurrentUserId();
    }
}
