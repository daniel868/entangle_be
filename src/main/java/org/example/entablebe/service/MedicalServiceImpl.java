package org.example.entablebe.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.entablebe.entity.*;
import org.example.entablebe.pojo.generic.GenericSuccessPageableResponse;
import org.example.entablebe.pojo.medical.DiseaseDto;
import org.example.entablebe.pojo.medical.TreatmentDto;
import org.example.entablebe.repository.DiseaseRepository;
import org.example.entablebe.repository.UserRepository;
import org.example.entablebe.utils.AppUtils;
import org.example.entablebe.utils.HibernateUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MedicalServiceImpl implements MedicalService {

    private final DiseaseRepository diseaseRepository;
    private final UserRepository userRepository;
    private final EntityManagerFactory entityManagerFactory;

    public MedicalServiceImpl(DiseaseRepository diseaseRepository, UserRepository userRepository, EntityManagerFactory entityManagerFactory) {
        this.diseaseRepository = diseaseRepository;
        this.userRepository = userRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public GenericSuccessPageableResponse<DiseaseDto> getAllDiseaseForUser(Long userId, Pageable pageable) {
        UserEntangle user = userRepository.fetchUserWithCompetences(userId);
        List<String> userCompetenceType = user.getCompetences()
                .stream()
                .map(competence -> competence.getType().getName())
                .toList();

        EntityManager transactionEntityManager = HibernateUtils.getTransactionEntityManager(entityManagerFactory);
        TypedQuery<Disease> query = transactionEntityManager.createNamedQuery("Disease.fetchAllDiseaseForUser", Disease.class);
        HibernateUtils.applyPaginationOnQuery(pageable, query);
        query.setParameter("userTreatmentTypes", userCompetenceType);
        List<Disease> diseases = query.getResultList();

        List<DiseaseDto> diseaseDtoList = diseases.stream().map(disease -> {
            DiseaseDto diseaseDto = new DiseaseDto();
            diseaseDto.setDiseaseName(disease.getName());
            diseaseDto.setTreatments(mapTreatmentForDisease(disease));
            return diseaseDto;
        }).toList();

        Integer totalCount = diseaseRepository.countAllDisease();
        return AppUtils.buildPageableResponse(diseaseDtoList, pageable, totalCount);
    }

    private Map<String, List<TreatmentDto>> mapTreatmentForDisease(Disease disease) {
        Map<String, List<TreatmentDto>> response = new HashMap<>();
        disease.getTreatments().forEach(treatment -> {
            String treatmentType = treatment.getTreatmentType().trim();
            if (response.containsKey(treatmentType)) {
                List<TreatmentDto> diseaseTypeTreatments = response.get(treatmentType);
                TreatmentDto newTreatment = new TreatmentDto();
                newTreatment.setDescription(treatment.getDescription());
                diseaseTypeTreatments.add(newTreatment);
                response.put(treatmentType, diseaseTypeTreatments);
            } else {
                List<TreatmentDto> treatmentDtoList = new ArrayList<>();
                TreatmentDto newTreatment = new TreatmentDto();
                newTreatment.setDescription(treatment.getDescription());
                treatmentDtoList.add(newTreatment);
                response.put(treatmentType, treatmentDtoList);
            }
        });
        return response;
    }
}
