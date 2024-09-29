package org.example.entablebe;

import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync
public class EntableBeApplication {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(EntableBeApplication.class, args);
    }

    @Bean
    ApplicationRunner init() {
        return args -> {
            if (!env.acceptsProfiles("heroku")) {
                UserEntangle userEntangle = new UserEntangle();
                userEntangle.setUsername("test");
                userEntangle.setPassword(passwordEncoder.encode("test"));
                userEntangle.setEmail("sincalexandrudaniel@gmail.com");
                userEntangle.setAccountActivate(true);
                userEntangle.setInfo("competences:{mock condition test mock condition test mock condition test ;mock condition test  mock condition test  mock condition test ;} " +
                        "qualifications:{D1 Physical;D4 Emotional;D6 Environmental;}");
                userRepository.save(userEntangle);
            }
        };
    }
}

