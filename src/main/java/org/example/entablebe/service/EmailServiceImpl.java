package org.example.entablebe.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class EmailServiceImpl implements EmailService {
    private final static Logger logger = LogManager.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String smtpUsername;


    @Value("${base.path}")
    private String basePath;

    private final JavaMailSender mailSender;
    private final ExecutorService executorService;

    public EmailServiceImpl(JavaMailSender mailSender, ExecutorService executorService) {
        this.mailSender = mailSender;
        this.executorService = executorService;
    }

    @Override
    public void sendLinkActivation(String toEmail, String payload) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(smtpUsername);
        message.setTo(toEmail);
        message.setSubject("Entangle Email activation");

        StringBuilder emailTemplate = new StringBuilder();
        emailTemplate.append("Hello\n");
        emailTemplate.append("This is your activation link for entangle account: \n");
        emailTemplate.append("Url: ").append(buildActivationUrl(payload)).append("\n");
        emailTemplate.append("Goodbye\n");
        message.setText(emailTemplate.toString());

        executorService.submit(() -> {
            logger.info("Sending activation email for email: {}", toEmail);
            mailSender.send(message);
            logger.info("Finish sending email for email: {}", toEmail);
        });

    }

    @Override
    public void sendResetPasswordLink(String toEmail, String payload) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(smtpUsername);
        message.setTo(toEmail);

        message.setSubject("Entangle Reset Password");

        StringBuilder emailTemplate = new StringBuilder();
        emailTemplate.append("Hello\n");
        emailTemplate.append("This is your reset link for your account: \n");
        emailTemplate.append("Url: ").append(buildResetActionUrl(payload)).append("\n");
        emailTemplate.append("Goodbye\n");
        message.setText(emailTemplate.toString());

        executorService.submit(() -> {
            logger.info("Sending reset link for email: {}", toEmail);
            mailSender.send(message);
            logger.info("Finish sending reset link for email: {}", toEmail);
        });
    }

    private String buildActivationUrl(String payload) {
        return basePath + "email-validation?emailToken=" + payload;
    }

    private String buildResetActionUrl(String payload) {
        return basePath + "reset-password-step2?emailToken=" + payload;
    }
}
