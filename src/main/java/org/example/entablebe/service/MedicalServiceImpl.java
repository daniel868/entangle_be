package org.example.entablebe.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.*;
import org.example.entablebe.pojo.generic.GenericSuccessPageableResponse;
import org.example.entablebe.pojo.medical.DiseaseDto;
import org.example.entablebe.pojo.medical.DiseaseRequestDto;
import org.example.entablebe.pojo.medical.TreatmentDto;
import org.example.entablebe.pojo.medical.TreatmentItemDto;
import org.example.entablebe.repository.DiseaseRepository;
import org.example.entablebe.repository.MedicalRepository;
import org.example.entablebe.repository.TreatmentRepository;
import org.example.entablebe.repository.UserRepository;
import org.example.entablebe.utils.AppUtils;
import org.example.entablebe.utils.HibernateUtils;
import org.example.entablebe.utils.assemblers.PatientAssembler;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MedicalServiceImpl implements MedicalService {
    private static final Logger logger = LogManager.getLogger(MedicalServiceImpl.class);

    private final DiseaseRepository diseaseRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final UserRepository userRepository;
    private final MedicalRepository medicalRepository;
    private final TreatmentRepository treatmentRepository;
    private final PatientAssembler patientAssembler;

    private final Comparator<Treatment> treatmentComparator = Comparator.comparing(Treatment::getInsertDate);

    @Override
    public GenericSuccessPageableResponse<DiseaseDto> getAllDiseaseForUser(Long userId, Pageable pageable, String searchString) {
        EntityManager transactionEntityManager = HibernateUtils.getTransactionEntityManager(entityManagerFactory);
        TypedQuery<Disease> query = transactionEntityManager.createNamedQuery("Disease.fetchAllAvailableDisease", Disease.class);
        String search = HibernateUtils.buildSearchString(searchString);
        query.setParameter("searchString", search);

        HibernateUtils.applyPaginationOnQuery(pageable, query);
        List<Disease> diseases = query.getResultList();

        List<DiseaseDto> diseaseDtoList = diseases.stream().map(disease -> {
            DiseaseDto diseaseDto = new DiseaseDto();
            diseaseDto.setId(disease.getId());
            diseaseDto.setDiseaseName(disease.getName());
            diseaseDto.setTreatments(mapTreatmentForDisease(disease));
            if (disease.getPatient() != null) {
                diseaseDto.setPatientSituation(disease.getPatient().getPatientSituation());
                diseaseDto.setPatientInfo(patientAssembler.assemble(disease.getPatient()));
            }
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

            TypedQuery<Disease> diseaseByIdQuery = entityManager.createNamedQuery("Disease.fetchDiseaseByIdWithTreatments", Disease.class);
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
    public boolean updateTreatmentForDisease(Long treatmentId, List<TreatmentItemDto> treatmentItemsDto) {
        Treatment treatment = treatmentRepository.findTreatmentByIdWithItems(treatmentId);
        if (treatment == null) {
            throw new RuntimeException("Could not found treatment with id: " + treatmentId);
        }

        Set<TreatmentItem> items = treatment.getItems();
        //new items or updated items
        Map<String, TreatmentItem> itemsMapping = new HashMap<>();
        items.forEach(item -> itemsMapping.put(item.getType(), item));

        treatmentItemsDto.stream()
                .filter(item -> item.getDescription() != null)
                .forEach(itemDto -> {
                    TreatmentItem treatmentItem = itemsMapping.get(itemDto.getType());
                    if (treatmentItem != null && !treatmentItem.getDescription().equals(itemDto.getDescription())) {
                        treatmentItem.setDescription(itemDto.getDescription());
                    }
                    if (treatmentItem == null) {
                        TreatmentItem newItem = new TreatmentItem();
                        newItem.setDescription(itemDto.getDescription());
                        newItem.setType(itemDto.getType());
                        items.add(newItem);
                    }
                });

        //update new treatment items
        treatmentRepository.save(treatment);

        return true;
    }

    @Override
    public Disease initializePatientDisease(String patientContactInfo,
                                            String patientSituation,
                                            String patientName) {
        Disease disease = new Disease();
        disease.setName(patientName);
        Patient patient = new Patient();
        patient.setPatientSituation(patientSituation);
        patient.setPatientContactInfo(patientContactInfo);
        patient.setPatientName(patientName);

        disease.setPatient(patient);
        return diseaseRepository.save(disease);
    }

    @Override
    public List<TreatmentItemDto> getTreatmentItem(String searchString) {
        List<TreatmentItem> treatmentItems = medicalRepository.fetchMatchTreatmentItems(searchString);
        return treatmentItems.stream().map(item -> {
            TreatmentItemDto treatmentItemDto = new TreatmentItemDto();
            treatmentItemDto.setType(item.getType());
            treatmentItemDto.setDescription(item.getDescription());
            return treatmentItemDto;
        }).toList();
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
                    treatmentDto.setId(treatment.getId());
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
