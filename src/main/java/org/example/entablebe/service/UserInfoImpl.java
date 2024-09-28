package org.example.entablebe.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.exceptions.PasswordDoesNotMatchException;
import org.example.entablebe.pojo.generic.GenericErrorResponse;
import org.example.entablebe.pojo.generic.GenericSuccessResponse;
import org.example.entablebe.pojo.userInfo.ChangePasswordRequest;
import org.example.entablebe.pojo.userInfo.UserInfoResponse;
import org.example.entablebe.repository.UserRepository;
import org.example.entablebe.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserInfoImpl implements UserInfoService {
    private final static Logger logger = LogManager.getLogger(UserInfoImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserInfoResponse buildUserInfoResponse(Long userId) {
        Optional<UserEntangle> userById = userRepository.findById(userId);
        if (userById.isPresent()) {
            String userInfo = userById.get().getInfo();
            Map<String, List<String>> characteristics = extractCharacteristics(userInfo, userId);
            return UserInfoResponse.builder()
                    .competences(characteristics.get("competences"))
                    .qualification(characteristics.get("qualifications"))
                    .build();
        }
        return null;
    }

    @Override
    @Transactional
    public Object changePassword(Long userId,
                                 ChangePasswordRequest changePasswordRequest) {

        if (changePasswordRequest.getNewPassword().isEmpty() || changePasswordRequest.getCurrentPassword().isEmpty()) {
            return new GenericErrorResponse(
                    AppConstants.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST.value()
            );
        }

        try {
            UserEntangle userEntangle = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

            boolean matches = passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), userEntangle.getPassword());
            if (!matches) {
                throw new PasswordDoesNotMatchException("Password does not match");
            }
            userEntangle.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(userEntangle);
            logger.debug("Successfully changed password for userId: {}", userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);

            return new GenericSuccessResponse<>(response);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found with id: {}", userId);
            return new GenericErrorResponse(
                    AppConstants.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        } catch (PasswordDoesNotMatchException e) {
            logger.error("Password does not match for userId: {}", userId);
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

    @Override
    @Transactional
    public Object changeUsername(Long userId, String newUsername) {
        if (newUsername == null || newUsername.length() < 4) {
            return new GenericErrorResponse(
                    AppConstants.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST.value()
            );
        }

        Map<String, Object> response = new HashMap<>();
        try {
            UserEntangle userEntangle = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
            userEntangle.setUsername(newUsername);
            userRepository.save(userEntangle);
            logger.debug("Username changed for userId: {}", userId);

            response.put("success", true);
            return new GenericSuccessResponse<>(response);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found with id: {}", userId);
            return new GenericErrorResponse(
                    AppConstants.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        } catch (Exception e) {
            logger.error("Could not change username for userId: {}", userId, e);
            return new GenericErrorResponse(
                    AppConstants.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

    @Override
    public Object changeEmail(Long userId, String newEmail) {
        Pattern emailPattern = Pattern.compile(AppConstants.EMAIL_REGEX);

        if (newEmail == null || !emailPattern.matcher(newEmail).matches()) {
            return new GenericErrorResponse(
                    AppConstants.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST.value()
            );
        }
        Map<String, Object> response = new HashMap<>();
        try {
            UserEntangle userEntangle = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

            userEntangle.setEmail(newEmail);
            userRepository.save(userEntangle);
            logger.debug("Email changed for userId: {}", userId);
            response.put("success", true);

            return new GenericSuccessResponse<>(response);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found with id: {}", userId);
            return new GenericErrorResponse(
                    AppConstants.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        } catch (Exception e) {
            logger.error("Could not change email for userId: {}", userId, e);
            return new GenericErrorResponse(
                    AppConstants.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

    private Map<String, List<String>> extractCharacteristics(String info, Long userId) {
        Map<String, List<String>> result = new HashMap<>();
        if (info == null) {
            return result;
        }
        try {
            String[] parts = info.split("(?<=[}])\\s+");
            for (String part : parts) {
                String[] keyValue = part.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    value = value.replace("{", "").replace("}", "");

                    List<String> characteristics = Arrays.stream(value.split(";"))
                            .map(String::trim)
                            .toList();
                    result.put(key, characteristics);
                }
            }
        } catch (Exception e) {
            logger.error("Error getting characteristics for userId: {}", userId);
        }
        return result;
    }
}
