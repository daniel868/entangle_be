package org.example.entablebe.service;

import org.example.entablebe.entity.Disease;
import org.example.entablebe.entity.UserEntangle;
import org.example.entablebe.pojo.generic.GenericSuccessPageableResponse;
import org.example.entablebe.pojo.generic.GenericSuccessResponse;
import org.example.entablebe.pojo.medical.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MedicalService {

    GenericSuccessPageableResponse<DiseaseDto> getAllDiseaseForUser(Long userId, Pageable pageable, String searchString);

    List<TreatmentItemDto> getTreatmentItem(String searchString);

    boolean addNewTreatment(Long diseaseId, Long userId, List<TreatmentItemDto> treatmentItems);

    boolean removeDisease(Long diseaseId);

    boolean addNewDisease(Long userId, DiseaseRequestDto diseaseRequestDto);

    Disease initializePatientDisease(String patientContactInfo, String patientSituation, String patientName);

    boolean updateTreatmentForDisease(Long treatmentId, List<TreatmentItemDto> treatmentItemsDto);

    MedicalNoteDto addNewNote(MedicalNoteDto dto, Long diseaseId);

    List<MedicalNoteDto> getAllNotesForDisease(Long diseaseId);
}
