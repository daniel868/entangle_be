package org.example.entablebe.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.service.UserInfoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Object getUserInfo(HttpServletRequest request,
                              HttpServletResponse response) {
        UserEntangle authUser = (UserEntangle) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userInfoService.buildUserInfoResponse(authUser.getId());
    }
}
