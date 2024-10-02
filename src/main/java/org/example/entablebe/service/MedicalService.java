package org.example.entablebe.service;

import org.example.entablebe.pojo.medical.DiseaseTreatmentResponse;

public interface MedicalService {

    DiseaseTreatmentResponse getAllDiseaseForUser(Long userId);
}
