package org.example.entablebe.service;

import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserInfoImpl implements UserInfoService {
    private final UserRepository userRepository;

    public UserInfoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserInfoService buildUserInfoResponse(Long userId) {
        Optional<UserEntangle> userById = userRepository.findById(userId);
        if (userById.isPresent()) {

        }
        return null;
    }

    private List<String> extractQualification(String userInfo) {
        String[] qualificationSplit = userInfo.split("qualifications:");
        if (qualificationSplit.length > 1) {
            return extractCharacteristics(qualificationSplit[1]);
        }
        return new ArrayList<>();
    }

    private List<String> extractCharacteristics(String info) {
        return Arrays.stream(info.split(";"))
                .map(String::trim)
                .toList();
    }
}
