package org.example.entablebe;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.apache.catalina.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.*;
import org.example.entablebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@SpringBootApplication
@EnableAsync
public class EntableBeApplication {
    public static final Logger logger = LogManager.getLogger(EntableBeApplication.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(EntableBeApplication.class, args);
    }

    @Bean
    ApplicationRunner init(PlatformTransactionManager platformTransactionManager,
                           EntityManagerFactory entityManagerFactory) {
        return args -> {
            if (!env.acceptsProfiles("heroku")) {
                TransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
                TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
                try {
                    boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
                    logger.info("Actual transaction active: {}", actualTransactionActive);
                    EntityManager transactionalEntityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);

                    UserEntangle userEntangle = new UserEntangle();
                    userEntangle.setUsername("test");
                    userEntangle.setPassword(passwordEncoder.encode("test"));
                    userEntangle.setEmail("sincalexandrudaniel@gmail.com");
                    userEntangle.setAccountActivate(true);
                    userEntangle.setInfo("competences:{mock condition test mock condition test mock condition test ;mock condition test  mock condition test  mock condition test ;}");

                    Competence competenceD1 = new Competence();
                    competenceD1.setType(CompetenceType.QUALIFICATION);
                    competenceD1.setName("D1");


                    Competence competenceD2 = new Competence();
                    competenceD2.setType(CompetenceType.QUALIFICATION);
                    competenceD2.setName("D2");

                    userEntangle.addCompetence(competenceD1);
                    userEntangle.addCompetence(competenceD2);

                    transactionalEntityManager.persist(userEntangle);


                    UserEntangle result = userRepository.fetchUserWithCompetences(1L);
                    insertMockData(transactionalEntityManager, userEntangle);

                    platformTransactionManager.commit(transaction);
                    transactionalEntityManager.close();
                } catch (Exception e) {

                    logger.error(e);
                }
            }

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TypedQuery<Disease> namedQuery = entityManager.createNamedQuery("Disease.fetchAllDiseaseForUser", Disease.class);
            namedQuery.setParameter("userId", 1L);
            List<Disease> response = namedQuery.getResultList();
            logger.debug("Response size: {}", response.size());
        };
    }

    private void insertMockData(EntityManager entityManager, UserEntangle userEntangle) {
        Treatment treatment1 = new Treatment();
        treatment1.setDescription("treatment1_1");

        Treatment treatment2 = new Treatment();
        treatment2.setDescription("treatment1_2");

        userEntangle.addTreatment(treatment1);
        userEntangle.addTreatment(treatment2);

        Disease disease1 = new Disease();
        disease1.setName("disease1");
        disease1.addTreatment(treatment1);
        disease1.addTreatment(treatment2);

        Disease disease2 = new Disease();
        disease2.setName("disease2");

        userEntangle.getCompetences().forEach(treatment1::addCompetences);
        userEntangle.getCompetences().forEach(treatment2::addCompetences);

        entityManager.persist(disease1);
        entityManager.persist(disease2);
        entityManager.persist(userEntangle);
    }
}

