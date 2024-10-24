package org.example.entablebe.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.pojo.auth.AuthenticateResponse;
import org.example.entablebe.pojo.auth.ResetPasswordRequest;
import org.example.entablebe.pojo.auth.LoginRequest;
import org.example.entablebe.pojo.auth.RegisterRequest;
import org.example.entablebe.repository.UserRepository;
import org.example.entablebe.utils.AppUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

    @Value("${jwt.expiration.duration.minutes}")
    private long tokenDurationInMinutes;

    @Value("${cypher.encryption.key}")
    private String cypherEncryptionKey;

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthServiceImpl(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public AuthenticateResponse registerUser(RegisterRequest registerRequest) {
        logger.debug("registerUser - with email: {} and username: {}", registerRequest.getUsername(), registerRequest.getUsername());
        UserEntangle userEntangle = new UserEntangle();
        userEntangle.setUsername(registerRequest.getUsername());
        userEntangle.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userEntangle.setEmail(registerRequest.getEmail());
        userEntangle.setInfo(mapUserInfo(registerRequest));

        try {
            String emailToken = generateEmailToken(registerRequest.getUsername());
            userEntangle.setAccountActivate(false);
            userEntangle.setValidationEmailToken(emailToken);
            emailService.sendLinkActivation(registerRequest.getEmail(), emailToken);
        } catch (Exception e) {
            logger.error("Error generation email validation token for {}", userEntangle.getUsername());
            throw new RuntimeException("Error generation email validation token for " + userEntangle.getUsername(), e);
        }

        UserEntangle savedUser = userRepository.save(userEntangle);
        String jwtToken = jwtService.generateToken(new HashMap<>(), savedUser);

        return buildAuthenticateResponse(jwtToken, savedUser, false);
    }

    @Override
    public AuthenticateResponse loginUser(LoginRequest loginRequest) {
        logger.debug("startLogin - with username: {}", loginRequest.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        UserEntangle existingUser = userRepository.findByUsername(loginRequest.getUsername());
        if (existingUser != null) {
            String jwtToken = jwtService.generateToken(new HashMap<>(), existingUser);
            return buildAuthenticateResponse(jwtToken, existingUser, existingUser.isAccountActivate());
        }
        throw new UsernameNotFoundException("Username or password is incorrect");
    }

    @Override
    @Transactional
    public AuthenticateResponse activateUserAccount(String emailToken) {
        logger.debug("activateUser - with token: {}", emailToken);
        if (emailToken == null || emailToken.isEmpty()) {
            return null;
        }
        try {
            String decryptedPayload = AppUtils.decrypt(emailToken, cypherEncryptionKey);
            String username = extractUsername(decryptedPayload);
            logger.debug("activateUser - with username: {}", username);

            UserEntangle userByUsername = userRepository.findByUsername(username);
            if (userByUsername == null) {
                throw new UsernameNotFoundException("Username " + username + " not found");
            }
            String decodedToken = URLDecoder.decode(userByUsername.getValidationEmailToken(), "UTF-8");
            if (!decodedToken.equals(emailToken)) {
                throw new RuntimeException("Invalid Email token for user:" + username);
            }

            userByUsername.setAccountActivate(true);
            userRepository.save(userByUsername);
            String jwtToken = jwtService.generateToken(new HashMap<>(), userByUsername);

            return buildAuthenticateResponse(jwtToken, userByUsername, true);

        } catch (Exception e) {
            logger.error("Error activating account for token: {}", emailToken, e);
            throw new RuntimeException("Error activating account for token:" + emailToken, e);
        }
    }

    @Override
    public boolean sendResetAccountPassword(String emailAddress) {
        try {
            List<UserEntangle> userByEmail = userRepository.findUserByEmail(emailAddress);
            if (userByEmail.isEmpty()) {
                return false;
            }
            //extract first match
            UserEntangle userEntangle = userByEmail.get(0);
            String resetPasswordToken = generateEmailToken(userEntangle.getUsername());
            emailService.sendResetPasswordLink(emailAddress, resetPasswordToken);
            return true;
        } catch (Exception e) {
            logger.error("Exception occurred when sending reset link for email: {}", emailAddress, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean resetAccountPassword(ResetPasswordRequest request) {
        try {
            if (request.getNewPassword().isEmpty() || request.getEmailToken().isEmpty()){
                return false;
            }

            String decryptedPayload = AppUtils.decrypt(request.getEmailToken(), cypherEncryptionKey);
            String username = extractUsername(decryptedPayload);

            logger.debug("resetAccountPassword - with username: {}", username);

            UserEntangle user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("Username " + username + " not found");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            return true;
        } catch (Exception e) {
            logger.error("Exception occurred when resetting password ", e);
        }
        return false;
    }

    private String mapUserInfo(RegisterRequest registerRequest) {
        StringBuilder sb = new StringBuilder();
        if (!registerRequest.getCompetences().isEmpty()) {
            sb.append("competences:{");
            registerRequest.getCompetences()
                    .forEach(competence -> sb.append(competence).append(";"));
            sb.append("} ");
        }
        if (!registerRequest.getQualification().isEmpty()) {
            sb.append("qualifications:{");
            registerRequest.getQualification()
                    .forEach(qualification -> sb.append(qualification).append(";"));
            sb.append("}");
        }
        return sb.isEmpty() ? null : sb.toString().trim();
    }

    private AuthenticateResponse buildAuthenticateResponse(String token, UserEntangle userEntangle, boolean activateAccount) {
        return AuthenticateResponse.builder()
                .token(token)
                .username(userEntangle.getUsername())
                .userEmail(userEntangle.getEmail())
                .accountActivate(activateAccount)
                .expiresInSecond(tokenDurationInMinutes * 60)
                .build();
    }

    private String generateEmailToken(String username) throws Exception {
        String tokenPayload = "{username:" + username + "}";
        String encryptedToken = AppUtils.encrypt(tokenPayload, cypherEncryptionKey);

        return URLEncoder.encode(encryptedToken, StandardCharsets.UTF_8);
    }

    private String extractUsername(String decryptedPayload) {
        return decryptedPayload.replace("{username:", "")
                .replace("}", "");
    }
}
