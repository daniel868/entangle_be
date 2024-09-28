package org.example.entablebe.service;

import org.example.entablebe.pojo.userInfo.ChangePasswordRequest;
import org.example.entablebe.pojo.userInfo.UserInfoResponse;

public interface UserInfoService {
    UserInfoResponse buildUserInfoResponse(Long userId);

    boolean changePassword(Long userId,
                           ChangePasswordRequest changePasswordRequest);
}
