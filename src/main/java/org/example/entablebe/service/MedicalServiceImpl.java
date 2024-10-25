package org.example.entablebe.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.*;
import org.example.entablebe.pojo.generic.GenericSuccessPageableResponse;
import org.example.entablebe.pojo.medical.DiseaseDto;
import org.example.entablebe.pojo.medical.DiseaseRequestDto;
import org.example.entablebe.pojo.medical.TreatmentDto;
import org.example.entablebe.pojo.medical.TreatmentItemDto;
import org.example.entablebe.repository.DiseaseRepository;
import org.example.entablebe.repository.UserRepository;
import org.example.entablebe.utils.AppConstants;
import org.example.entablebe.utils.AppUtils;
import org.example.entablebe.utils.HibernateUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class MedicalServiceImpl implements MedicalService {
    private static final Logger logger = LogManager.getLogger(MedicalServiceImpl.class);

    private final DiseaseRepository diseaseRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final UserRepository userRepository;

    private final Comparator<Treatment> treatmentComparator = Comparator.comparing(Treatment::getInsertDate);

    public MedicalServiceImpl(DiseaseRepository diseaseRepository, EntityManagerFactory entityManagerFactory, UserRepository userRepository) {
        this.diseaseRepository = diseaseRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.userRepository = userRepository;
    }

    @Override
    public GenericSuccessPageableResponse<DiseaseDto> getAllDiseaseForUser(Long userId, Pageable pageable, String searchString) {
        EntityManager transactionEntityManager = HibernateUtils.getTransactionEntityManager(entityManagerFactory);
        TypedQuery<Disease> query = transactionEntityManager.createNamedQuery("Disease.fetchAllAvailableDisease", Disease.class);
        String search = buildSearchString(searchString);
        query.setParameter("searchString", search);

        HibernateUtils.applyPaginationOnQuery(pageable, query);
        List<Disease> diseases = query.getResultList();

        List<DiseaseDto> diseaseDtoList = diseases.stream().map(disease -> {
            DiseaseDto diseaseDto = new DiseaseDto();
            diseaseDto.setId(disease.getId());
            diseaseDto.setDiseaseName(disease.getName());
            diseaseDto.setTreatments(mapTreatmentForDisease(disease));
            return diseaseDto;
        }).toList();

        Integer totalCount = diseaseRepository.countAllDisease(search);
        return AppUtils.buildPageableResponse(diseaseDtoList, pageable, totalCount);
    }

    @Override
    @Transactional
    public boolean addNewTreatment(Long diseaseId,
                                   Long userId,
                                   List<TreatmentItemDto> treatmentItems) {
        Disease diseaseById;
        UserEntangle userEntangle;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            TypedQuery<Disease> diseaseByIdQuery = entityManager.createNamedQuery("Disease.fetchDiseaseById", Disease.class);
            diseaseByIdQuery.setParameter("diseaseId", diseaseId);
            diseaseById = diseaseByIdQuery.getSingleResult();
            if (diseaseById == null) {
                throw new RuntimeException("Could not find disease with id:" + diseaseId);
            }

            TypedQuery<UserEntangle> userByIdQuery = entityManager.createNamedQuery("UserEntangle.fetchUserById", UserEntangle.class);
            userByIdQuery.setParameter("userId", userId);
            userEntangle = userByIdQuery.getSingleResult();

            if (userEntangle == null) {
                throw new RuntimeException("Could not find user with id:" + userId);
            }

        } catch (Exception e) {
            logger.error("Error on fetching disease", e);
            throw e;
        }

        Set<TreatmentItem> items = treatmentItems.stream().map(treatmentItemDto -> {
            TreatmentItem treatmentItem = new TreatmentItem();
            treatmentItem.setType(treatmentItemDto.getType());
            treatmentItem.setDescription(treatmentItemDto.getDescription());
            return treatmentItem;
        }).collect(Collectors.toSet());

        Treatment newTreatment = new Treatment();
        newTreatment.setUser(userEntangle);
        newTreatment.addTreatmentItems(items);
        newTreatment.setInsertDate(new Date());

        diseaseById.addTreatment(newTreatment);

        diseaseRepository.save(diseaseById);
        logger.debug("Finish inserting treatment for diseaseId: {}", diseaseById);
        return true;
    }

    @Override
    public boolean addNewDisease(Long userId, DiseaseRequestDto diseaseRequestDto) {
        UserEntangle userEntangle = userRepository.findById(userId).orElse(null);

        if (userEntangle == null) {
            throw new RuntimeException("Could not found user with id: " + userId);
        }

        Disease disease = new Disease();
        disease.setName(diseaseRequestDto.getDiseaseName());

        if (!diseaseRequestDto.getItems().isEmpty()) {
            Treatment treatment = new Treatment();
            treatment.setUser(userEntangle);
            treatment.setInsertDate(new Date());

            Set<TreatmentItem> treatmentItems = diseaseRequestDto.getItems().stream().map(itemDto -> {
                TreatmentItem item = new TreatmentItem();
                item.setDescription(itemDto.getDescription());
                item.setType(itemDto.getType());
                return item;
            }).collect(Collectors.toSet());

            treatment.addTreatmentItems(treatmentItems);
            disease.addTreatment(treatment);
        }
        Disease saved = diseaseRepository.save(disease);

        logger.debug("Saved disease with id: {}", saved.getId());
        return true;
    }

    @Override
    @Transactional
    public boolean removeDisease(Long diseaseId) {
        Disease diseaseById = diseaseRepository.findById(diseaseId).orElse(null);

        if (diseaseById == null) {
            throw new RuntimeException("Could not found disease with id: " + diseaseId);
        }

        diseaseRepository.delete(diseaseById);
        logger.debug("Finish deleting disease with id: {}", diseaseId);
        return true;
    }

    private List<TreatmentDto> mapTreatmentForDisease(Disease disease) {
        return disease.getTreatments().stream()
                .sorted(treatmentComparator)
                .map(treatment -> {
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

    private String buildSearchString(String searchString) {
        if (searchString == null) {
            return AppConstants.LIKE_OPERATOR;
        }
        return searchString + AppConstants.LIKE_OPERATOR;
    }
}
