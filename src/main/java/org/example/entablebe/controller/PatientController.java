package org.example.entablebe.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.example.entablebe.pojo.contact.ContactRequest;
import org.example.entablebe.pojo.generic.GenericSuccessResponse;
import org.example.entablebe.service.ContactService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/patient")
@RestController
public class PatientController {

    private final ContactService contactService;

    public PatientController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    @RateLimiter(name = "newContactInitialize")
    public GenericSuccessResponse<Boolean> newContactInitialize(@RequestBody ContactRequest contactRequest) {
        boolean response = contactService.addNewContact(contactRequest);
        return new GenericSuccessResponse<>(response);
    }
}
