package org.example.entablebe.controller.handler;

import org.example.entablebe.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    private final EmailService emailService;


    public ErrorHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @ExceptionHandler(value = Exception.class)
    public void handleGenericException(Exception exception) {
        logger.error("Error encountered. Will notify on email for exception: ", exception);
        emailService.notifyTechnicalException(exception);
    }
}
