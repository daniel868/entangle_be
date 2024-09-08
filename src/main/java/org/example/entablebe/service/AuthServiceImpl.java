package org.example.entablebe.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.pojo.auth.LoginRequest;
import org.example.entablebe.pojo.auth.RegisterRequest;
import org.example.entablebe.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String registerUser(RegisterRequest registerRequest) {
        logger.debug("registerUser - with email: {} and username: {}", registerRequest.getUsername(), registerRequest.getUsername());
        UserEntangle userEntangle = new UserEntangle();
        userEntangle.setUsername(registerRequest.getUsername());
        userEntangle.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userEntangle.setEmail(registerRequest.getEmail());
        userEntangle.setInfo(mapUserInfo(registerRequest));

        UserEntangle savedUser = userRepository.save(userEntangle);

        return jwtService.generateToken(new HashMap<>(), savedUser);
    }

    @Override
    public String loginUser(LoginRequest loginRequest) {
        logger.debug("startLogin - with username: {}", loginRequest.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        UserEntangle existingUser = userRepository.findByUsername(loginRequest.getUsername());
        if (existingUser != null) {
            return jwtService.generateToken(new HashMap<>(), existingUser);
        }
        throw new UsernameNotFoundException("Username or password is incorrect");
    }

    private String mapUserInfo(RegisterRequest registerRequest) {
        StringBuilder sb = new StringBuilder();
        if (!registerRequest.getCompetences().isEmpty()) {
            sb.append("competences:{");
            registerRequest.getCompetences()
                    .forEach(competence -> sb.append(competence).append(";"));
            sb.append("} ");
        }
        if (!registerRequest.getQualification().isEmpty()){
            sb.append("qualifications:{");
            registerRequest.getQualification()
                    .forEach(qualification -> sb.append(qualification).append(";"));
            sb.append("}");
        }
        return sb.isEmpty() ? null : sb.toString().trim();
    }
}
