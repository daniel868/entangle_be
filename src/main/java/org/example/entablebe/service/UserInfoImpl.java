package org.example.entablebe.service;

import com.google.auth.ServiceAccountSigner;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.exceptions.PasswordDoesNotMatchException;
import org.example.entablebe.pojo.userInfo.ChangePasswordRequest;
import org.example.entablebe.pojo.userInfo.UserInfoResponse;
import org.example.entablebe.repository.UserRepository;
import org.example.entablebe.utils.AppConstants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoImpl implements UserInfoService {
    private final static Logger logger = LogManager.getLogger(UserInfoImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExecutorService executorService;

    public UserInfoImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ExecutorService executors) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.executorService = executors;
    }


    @Override
    public UserInfoResponse buildUserInfoResponse(Long userId) {
        Optional<UserEntangle> userById = userRepository.findById(userId);
        if (userById.isPresent()) {
            UserEntangle userEntangle = userById.get();
            Map<String, List<String>> characteristics = extractCharacteristics(userEntangle.getInfo(), userId);
            return UserInfoResponse.builder()
                    .competences(characteristics.get("competences"))
                    .qualification(characteristics.get("qualifications"))
                    .email(userEntangle.getEmail())
                    .username(userEntangle.getUsername())
                    .build();
        }
        return null;
    }

    @Override
    @Transactional
    public Map<String, Object> changePassword(Long userId,
                                              ChangePasswordRequest changePasswordRequest) {

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

        return response;
    }

    @Override
    @Transactional
    public Map<String, Object> changeUsername(Long userId, String newUsername) {
        Map<String, Object> response = new HashMap<>();

        UserEntangle userEntangle = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        userEntangle.setUsername(newUsername);
        UserEntangle userSaved = userRepository.save(userEntangle);
        logger.debug("Username changed for userId: {}", userSaved.getId());

        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> changeEmail(Long userId, String newEmail) {
        Map<String, Object> response = new HashMap<>();

        UserEntangle userEntangle = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        userEntangle.setEmail(newEmail);
        userRepository.save(userEntangle);
        logger.debug("Email changed for userId: {}", userId);
        response.put("success", true);

        return response;
    }

    //TODO: use this method later
    public Map<String, Object> uploadProfileImage(MultipartFile multipartFile) {
        try {

            Map<String, Object> response = new HashMap<>();
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(Thread.currentThread().
                    getContextClassLoader().
                    getResourceAsStream("google_secret.json"));

            BlobId blobId = BlobId.of("test_bucket_upload_image", multipartFile.getOriginalFilename());
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("media")
                    .build();

            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(googleCredentials)
                    .build()
                    .getService();

            Blob blob = storage.create(blobInfo, multipartFile.getBytes());
            URL presignedUrl = storage.signUrl(blobInfo, 15, TimeUnit.DAYS, Storage.SignUrlOption.signWith((ServiceAccountSigner) googleCredentials));
            response.put("success", presignedUrl);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Could not upload success profile picture ", e);
        }
    }

    public Map<String, Object> uploadProfileImageAsBase64(@Nonnull MultipartFile multipartFile) {
        try {
            Map<String, Object> response = new HashMap<>();
            UserEntangle currentAuth = (UserEntangle) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserEntangle userEntangle = userRepository.findById(currentAuth.getId()).orElse(null);

            if (multipartFile.getSize() > AppConstants.MB_SIZE_IN_BYTES * 10) {
                logger.debug("Maxim file size exceed for file :{}", multipartFile.getOriginalFilename());
                throw new RuntimeException("Maxim file size exceed for file " + multipartFile.getOriginalFilename());
            }

            if (userEntangle != null) {
                String encodedBytes = Base64.getEncoder().encodeToString(multipartFile.getBytes());
                userEntangle.setProfileImage(encodedBytes);
                userRepository.save(userEntangle);
                response.put("success", true);
            }
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Could not upload success profile picture ", e);
        }
    }

    public String loadProfileImageAsBase64(Long userId) {
        try {
            UserEntangle userEntangle = userRepository.findById(userId).orElse(null);
            if (userEntangle == null) {
                throw new UsernameNotFoundException("Could not found user with id: " + userId);
            }
            return userEntangle.getProfileImage();
        } catch (Exception e) {
            throw new RuntimeException("Could not load success profile image ", e);
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
