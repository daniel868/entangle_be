package org.example.entablebe.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.exceptions.PasswordDoesNotMatchException;
import org.example.entablebe.pojo.generic.GenericErrorResponse;
import org.example.entablebe.pojo.generic.GenericSuccessResponse;
import org.example.entablebe.pojo.userInfo.ChangePasswordRequest;
import org.example.entablebe.service.UserInfoService;
import org.example.entablebe.utils.AppConstants;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    private final static Logger logger = LogManager.getLogger(UserInfoController.class);
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

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public Object changeUserPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        Jsoup.clean(changePasswordRequest.toString(), Safelist.basic());
        if (changePasswordRequest.getNewPassword().isEmpty() || changePasswordRequest.getCurrentPassword().isEmpty()) {
            return new GenericErrorResponse(
                    AppConstants.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST.value()
            );
        }
        UserEntangle authUser = (UserEntangle) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Map<String, Object> response = userInfoService.changePassword(authUser.getId(), changePasswordRequest);
            return new GenericSuccessResponse<>(response);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found with id: {}", authUser.getId());
            return new GenericErrorResponse(
                    AppConstants.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        } catch (PasswordDoesNotMatchException e) {
            logger.error("Password does not match for userId: {}", authUser.getId());
            return new GenericErrorResponse(
                    AppConstants.INVALID_PASSWORD,
                    HttpStatus.BAD_REQUEST.value()
            );
        } catch (Exception e) {
            logger.error("Could not change password", e);
            return new GenericErrorResponse(
                    AppConstants.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

    @RequestMapping(value = "/changeEmail", method = RequestMethod.POST)
    public Object changeEmail(@RequestParam(name = "newEmail") String newEmail) {
        Jsoup.clean(newEmail, Safelist.basic());

        if (newEmail.isEmpty() || !AppConstants.EMAIL_PATTERN.matcher(newEmail).matches()) {
            return new GenericErrorResponse(
                    AppConstants.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST.value()
            );
        }
        UserEntangle authUser = (UserEntangle) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Map<String, Object> response = userInfoService.changeEmail(authUser.getId(), newEmail);
            return new GenericSuccessResponse<>(response);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found with id: {}", authUser.getId());
            return new GenericErrorResponse(
                    AppConstants.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        } catch (Exception e) {
            logger.error("Could not change email for userId: {}", authUser.getId(), e);
            return new GenericErrorResponse(
                    AppConstants.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

    @RequestMapping(value = "/changeUsername", method = RequestMethod.POST)
    public Object changeUsername(@RequestParam(name = "newUsername") String newUsername) {
        Jsoup.clean(newUsername, Safelist.basic());

        if (newUsername.length() < 4) {
            return new GenericErrorResponse(
                    AppConstants.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST.value()
            );
        }
        UserEntangle authUser = (UserEntangle) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Map<String, Object> response = userInfoService.changeUsername(authUser.getId(), newUsername);
            return new GenericSuccessResponse<>(response);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found with id: {}", authUser.getId());
            return new GenericErrorResponse(
                    AppConstants.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        } catch (Exception e) {
            logger.error("Could not change username for userId: {}", authUser.getId(), e);
            return new GenericErrorResponse(
                    AppConstants.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

    @PostMapping(value = "/uploadProfilePicture")
    public Object uploadProfilePicture(@RequestParam("file") MultipartFile multipartFile,
                                       HttpServletRequest request) {
        logger.debug("Trying to upload file : {}", multipartFile.getOriginalFilename());
        try {
            return userInfoService.uploadProfileImageAsBase64(multipartFile);
        } catch (Exception e) {
            logger.error("Error while uploading file: {}", multipartFile.getOriginalFilename(), e);
            return new GenericErrorResponse(
                    AppConstants.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

    @RequestMapping(path = "/profilePicture", method = RequestMethod.GET)
    public Object getProfilePicture() {
        UserEntangle currentAuth = (UserEntangle) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.debug("Trying to get profile picture for user : {}", currentAuth.getUsername());
        try {
            String base64Image = userInfoService.loadProfileImageAsBase64(currentAuth.getId());
            return new GenericSuccessResponse<>(base64Image);
        } catch (Exception e) {
            logger.error("Error while fetching image for user: {}", currentAuth.getUsername(), e);
            return new GenericErrorResponse(
                    AppConstants.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

}
