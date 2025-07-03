package com.example.backend.service;

import com.example.backend.pojo.TaboosAndPreferenceInfo;
import com.example.backend.pojo.User;

public interface PersonalInfoService {
    User getPersonalInfo();

    void updatePersonalInfo(TaboosAndPreferenceInfo taboosAndPreferenceInfo);
}
