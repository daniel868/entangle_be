package org.example.entablebe.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.entablebe.entity.CompetenceType;
import org.example.entablebe.entity.Disease;
import org.example.entablebe.entity.TreatmentItem;
import org.example.entablebe.pojo.generic.GenericSuccessPageableResponse;
import org.example.entablebe.pojo.medical.DiseaseDto;
import org.example.entablebe.pojo.medical.TreatmentDto;
import org.example.entablebe.pojo.medical.TreatmentItemDto;
import org.example.entablebe.repository.DiseaseRepository;
import org.example.entablebe.repository.UserRepository;
import org.example.entablebe.utils.AppUtils;
import org.example.entablebe.utils.HibernateUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        EntityManager transactionEntityManager = HibernateUtils.getTransactionEntityManager(entityManagerFactory);
        TypedQuery<Disease> query = transactionEntityManager.createNamedQuery("Disease.fetchAllDiseaseForUser", Disease.class);
        HibernateUtils.applyPaginationOnQuery(pageable, query);
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

    private List<TreatmentDto> mapTreatmentForDisease(Disease disease) {
        return disease.getTreatments().stream().map(treatment -> {
            TreatmentDto treatmentDto = new TreatmentDto();
            treatmentDto.setSpecialistName(treatment.getUser().getUsername());
            if (!treatment.getItems().isEmpty()) {
                treatmentDto.setItems(mapItemsToItemsDto(treatment.getItems()));
            }
            return treatmentDto;
        }).toList();
    }

    private List<TreatmentItemDto> mapItemsToItemsDto(Set<TreatmentItem> treatmentItems) {
        Map<String, TreatmentItemDto> indexType = new HashMap<>();
        List<TreatmentItemDto> itemList = treatmentItems.stream().map(item -> {
            TreatmentItemDto itemDto = new TreatmentItemDto();
            itemDto.setDescription(item.getDescription());
            itemDto.setType(item.getType());
            indexType.put(item.getType(), itemDto);
            return itemDto;
        }).collect(Collectors.toList());

        Arrays.stream(CompetenceType.values()).forEach(category -> {
            if (!indexType.containsKey(category.getName())) {
                itemList.add(new TreatmentItemDto(category.getName(), null));
            }
        });

        Comparator<CompetenceType> comparator = Comparator.comparingInt(CompetenceType::getIndex);
        itemList.sort(Comparator.comparing(treatmentItemDto -> CompetenceType.mapCompetenceType(treatmentItemDto.getType()), comparator));
        return itemList;
    }
}
