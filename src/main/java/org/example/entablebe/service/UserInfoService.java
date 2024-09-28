package org.example.entablebe.service;

import org.example.entablebe.pojo.userInfo.ChangePasswordRequest;
import org.example.entablebe.pojo.userInfo.UserInfoResponse;

public interface UserInfoService {
    UserInfoResponse buildUserInfoResponse(Long userId);

    Object changePassword(Long userId,
                          ChangePasswordRequest changePasswordRequest);

    Object changeUsername(Long userId,
                          String newUsername);

    Object changeEmail(Long userId,
                       String newEmail);
}
