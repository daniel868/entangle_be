package org.example.entablebe.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

public class HibernateUtils {

    public static EntityManager getTransactionEntityManager(EntityManagerFactory entityManagerFactory) {
        return EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
    }

    public static void applyPaginationOnQuery(Pageable pageable, Query query) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int firstResult = (pageNumber - 1) * pageSize;

        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);
    }

    public static String buildSearchString(String searchString) {
        if (searchString == null) {
            return AppConstants.LIKE_OPERATOR;
        }
        return searchString + AppConstants.LIKE_OPERATOR;
    }
}
