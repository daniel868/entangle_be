package org.example.entablebe.service;

public interface EmailService {
    void sendLinkActivation(String toEmail, String payload);
}
