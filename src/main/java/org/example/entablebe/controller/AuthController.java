package org.example.entablebe.controller;

import org.example.entablebe.pojo.auth.ActivateEmailResponse;
import org.example.entablebe.pojo.auth.LoginRequest;
import org.example.entablebe.pojo.auth.RegisterRequest;
import org.example.entablebe.pojo.auth.ResetPasswordRequest;
import org.example.entablebe.service.AuthService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.web.bind.annotation.*;

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
        return authService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest loginRequest) {
        Jsoup.clean(loginRequest.toString(), Safelist.basic());
        return authService.loginUser(loginRequest);
    }

    @PostMapping("/activate")
    public Object activateAccount(@RequestBody ActivateEmailResponse response) {
        Jsoup.clean(response.toString(), Safelist.basic());
        return authService.activateUserAccount(response.getEmailToken());
    }

    @PostMapping("/sendResetPasswordLink")
    public Object resetAccountPasswordStep1(@RequestParam String emailAddress) {
        Jsoup.clean(emailAddress, Safelist.basic());
        boolean emailSent = authService.sendResetAccountPassword(emailAddress);
        Map<String, Object> response = new HashMap<>();
        response.put("emailSend", emailSent);
        return response;
    }

    @PostMapping("/resetAccountPassword")
    public Object resectAccountPassword(@RequestBody ResetPasswordRequest request) {
        Jsoup.clean(request.toString(), Safelist.basic());
        boolean passwordChanged = authService.resetAccountPassword(request);
        Map<String, Object> response = new HashMap<>();
        response.put("passwordChanged", passwordChanged);
        return response;
    }
}
