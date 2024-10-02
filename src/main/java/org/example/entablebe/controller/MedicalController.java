package org.example.entablebe.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medical")
public class MedicalController {

    @RequestMapping(value = "/diseaseTreaments", method = RequestMethod.GET)
    public Object diseaseTreatments(HttpServletRequest request,
                                    HttpServletResponse response) {

    }
}
