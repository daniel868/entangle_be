package org.example.entablebe.service;

import org.example.entablebe.pojo.generic.GenericSuccessPageableResponse;
import org.example.entablebe.pojo.medical.DiseaseDto;
import org.springframework.data.domain.Pageable;

public interface MedicalService {

    GenericSuccessPageableResponse<DiseaseDto> getAllDiseaseForUser(Long userId, Pageable pageable);
}
