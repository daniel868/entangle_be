package org.example.entablebe.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.TreatmentItem;
import org.example.entablebe.repository.MedicalRepository;
import org.example.entablebe.utils.AppConstants;
import org.example.entablebe.utils.HibernateUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRepositoryImpl implements MedicalRepository {
    private static final Logger logger = LogManager.getLogger(MedicalRepositoryImpl.class);

    private final EntityManagerFactory entityManagerFactory;


    public MedicalRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<TreatmentItem> fetchMatchTreatmentItems(String searchString) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<TreatmentItem> treatmentItemQuery = entityManager.createNamedQuery("TreatmentItem.fetchMatchTreatmentItems", TreatmentItem.class);
            String buildSearchString = HibernateUtils.buildSearchString(searchString);
            treatmentItemQuery.setParameter("searchString", buildSearchString);
            treatmentItemQuery.setMaxResults(AppConstants.DEFAULT_PAGE_SIZE);

            return treatmentItemQuery.getResultList();
        } catch (Exception e) {
            logger.error("Error fetching treatmentItems: ", e);
            return List.of();
        }
    }

}
