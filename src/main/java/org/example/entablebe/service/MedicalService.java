package org.example.entablebe.service;

import org.example.entablebe.entity.Disease;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.pojo.generic.GenericSuccessPageableResponse;
import org.example.entablebe.pojo.generic.GenericSuccessResponse;
import org.example.entablebe.pojo.medical.DiseaseDto;
import org.example.entablebe.pojo.medical.DiseaseRequestDto;
import org.example.entablebe.pojo.medical.TreatmentItemDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MedicalService {

    GenericSuccessPageableResponse<DiseaseDto> getAllDiseaseForUser(Long userId, Pageable pageable, String searchString);

    List<TreatmentItemDto> getTreatmentItem(String searchString);

    boolean addNewTreatment(Long diseaseId, Long userId, List<TreatmentItemDto> treatmentItems);

    boolean removeDisease(Long diseaseId);

    boolean addNewDisease(Long userId, DiseaseRequestDto diseaseRequestDto);

    Disease initializePatientDisease(String patientContactInfo, String patientSituation);
}
