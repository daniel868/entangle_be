package org.example.entablebe.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.pojo.userInfo.ChangePasswordRequest;
import org.example.entablebe.pojo.userInfo.UserInfoResponse;
import org.example.entablebe.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public boolean changePassword(Long userId,
                                  ChangePasswordRequest changePasswordRequest) {

        if (changePasswordRequest.getNewPassword().isEmpty() || changePasswordRequest.getCurrentPassword().isEmpty()) {
            return false;
        }

        try {
            UserEntangle userEntangle = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            boolean matches = passwordEncoder.matches(changePasswordRequest.getNewPassword(), userEntangle.getPassword());
            if (!matches) {
                throw new Exception("Current password does not match");
            }
            userEntangle.setPassword(passwordEncoder.encode(changePasswordRequest.getCurrentPassword()));
            userRepository.save(userEntangle);
            logger.debug("Successfully changed password for userId: {}", userId);

            return true;
        } catch (Exception e) {
            logger.error("Could not change password", e);
            return false;
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
