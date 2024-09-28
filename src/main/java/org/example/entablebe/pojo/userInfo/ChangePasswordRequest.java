package org.example.entablebe.pojo.userInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
}
