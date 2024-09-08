package org.example.entablebe.controller;

import org.example.entablebe.pojo.auth.LoginRequest;
import org.example.entablebe.pojo.auth.RegisterRequest;
import org.example.entablebe.service.AuthService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Object registerNewUser(@RequestBody RegisterRequest registerRequest) {
        Jsoup.clean(registerRequest.toString(), Safelist.basic());

        Map<String, Object> response = new HashMap<>();
        String token = authService.registerUser(registerRequest);
        response.put("token", token);
        response.put("username", registerRequest.getUsername());
        return response;
    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest loginRequest) {
        Jsoup.clean(loginRequest.toString(), Safelist.basic());

        Map<String, Object> response = new HashMap<>();
        String token = authService.loginUser(loginRequest);
        response.put("token", token);
        response.put("username", loginRequest.getUsername());
        return response;
    }

}
