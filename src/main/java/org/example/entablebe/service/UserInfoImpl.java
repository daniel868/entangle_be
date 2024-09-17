package org.example.entablebe.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.pojo.userInfo.UserInfoResponse;
import org.example.entablebe.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserInfoImpl implements UserInfoService {
    private final static Logger logger = LogManager.getLogger(UserInfoImpl.class);

    private final UserRepository userRepository;

    public UserInfoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserInfoResponse buildUserInfoResponse(Long userId) {
        Optional<UserEntangle> userById = userRepository.findById(userId);
        if (userById.isPresent()) {
            String userInfo = userById.get().getInfo();
            Map<String, List<String>> characteristics = extractCharacteristics(userInfo,userId);
            return UserInfoResponse.builder()
                    .competences(characteristics.get("competences"))
                    .qualification(characteristics.get("qualifications"))
                    .build();
        }
        return null;
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
