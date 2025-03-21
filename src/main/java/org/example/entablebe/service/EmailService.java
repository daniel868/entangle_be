package org.example.entablebe.service;

public interface EmailService {
    void sendLinkActivation(String toEmail, String payload);

    void sendResetPasswordLink(String toEmail, String payload);

    void notifyTechnicalException(Exception exception);
}
