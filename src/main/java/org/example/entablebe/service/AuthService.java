package org.example.entablebe.service;

import org.example.entablebe.pojo.auth.LoginRequest;
import org.example.entablebe.pojo.auth.RegisterRequest;

public interface AuthService {
    String registerUser(RegisterRequest registerRequest);
    String loginUser(LoginRequest loginRequest);
}
