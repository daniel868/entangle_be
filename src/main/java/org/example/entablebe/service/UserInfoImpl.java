package org.example.entablebe.service;

import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.pojo.userInfo.UserInfoResponse;
import org.example.entablebe.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserInfoImpl implements UserInfoService {
    private final UserRepository userRepository;

    public UserInfoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserInfoResponse buildUserInfoResponse(Long userId) {
        Optional<UserEntangle> userById = userRepository.findById(userId);
        if (userById.isPresent()) {
            String userInfo = userById.get().getInfo();
            return UserInfoResponse.builder()
                    .competences(extractCompetences(userInfo))
                    .qualification(extractQualification(userInfo))
                    .build();
        }
        return null;
    }

    private List<String> extractQualification(String userInfo) {
        String[] qualificationSplit = userInfo.split("qualifications:");
        if (qualificationSplit.length > 1) {
            String splitQualification = qualificationSplit[1];
            splitQualification = splitQualification.replace("{", "")
                    .replace("}", "");
            return extractCharacteristics(splitQualification);
        }
        return new ArrayList<>();
    }

    private List<String> extractCompetences(String userInfo) {
        String[] competencesSplit = userInfo.split("competences:");
        if (competencesSplit.length > 1) {
            String splitCompetences = competencesSplit[1];
            splitCompetences = splitCompetences.replace("{", "")
                    .replace("}", "");
            return extractCharacteristics(splitCompetences);
        }
        return new ArrayList<>();
    }

    private List<String> extractCharacteristics(String info) {
        return Arrays.stream(info.split(";"))
                .map(String::trim)
                .toList();
    }
}
