package org.example.entablebe.service;

import org.example.entablebe.pojo.userInfo.ChangePasswordRequest;
import org.example.entablebe.pojo.userInfo.UserInfoResponse;

import java.util.Map;

public interface UserInfoService {
    UserInfoResponse buildUserInfoResponse(Long userId);

    Map<String, Object> changePassword(Long userId, ChangePasswordRequest changePasswordRequest);

    Map<String, Object> changeUsername(Long userId, String newUsername);

    Map<String, Object> changeEmail(Long userId, String newEmail);
}
