package org.example.entablebe.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.pojo.generic.GenericSuccessResponse;
import org.example.entablebe.service.MedicalService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medical")
public class MedicalController {

    private final MedicalService medicalService;

    public MedicalController(MedicalService medicalService) {
        this.medicalService = medicalService;
    }

    @RequestMapping(value = "/diseaseTreatments", method = RequestMethod.GET, produces = {"application/json"})
    public Object diseaseTreatments(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Pageable pageable) {
        UserEntangle currentUser = (UserEntangle) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return medicalService.getAllDiseaseForUser(currentUser.getId(), pageable);
    }
}
