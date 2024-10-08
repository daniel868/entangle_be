package org.example.entablebe.service;

import org.example.entablebe.pojo.auth.AuthenticateResponse;
import org.example.entablebe.pojo.auth.ResetPasswordRequest;
import org.example.entablebe.pojo.auth.LoginRequest;
import org.example.entablebe.pojo.auth.RegisterRequest;

public interface AuthService {
    AuthenticateResponse registerUser(RegisterRequest registerRequest);

    AuthenticateResponse loginUser(LoginRequest loginRequest);

    AuthenticateResponse activateUserAccount(String emailToken);

    boolean sendResetAccountPassword(String emailToken);

    boolean resetAccountPassword(ResetPasswordRequest request);
}
