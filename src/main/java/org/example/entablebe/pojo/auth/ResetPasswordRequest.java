package org.example.entablebe.pojo.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResetPasswordRequest {
    private String emailToken;
    private String newPassword;
}
