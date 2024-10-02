package org.example.entablebe.service;

import org.example.entablebe.pojo.medical.DiseaseTreatmentResponse;
import org.example.entablebe.repository.DiseaseRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicalServiceImpl implements MedicalService {

    private final DiseaseRepository diseaseRepository;

    public MedicalServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public DiseaseTreatmentResponse getAllDiseaseForUser(Long userId) {

        return null;
    }
}
