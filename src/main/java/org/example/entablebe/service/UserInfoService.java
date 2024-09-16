package org.example.entablebe.service;

import org.example.entablebe.pojo.userInfo.UserInfoResponse;

public interface UserInfoService {
    UserInfoResponse buildUserInfoResponse(Long userId);
}
