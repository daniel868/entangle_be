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
import java.util.stream.IntStream;

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

                    TypedQuery<Competence> query = transactionalEntityManager.createQuery("select c from Competence c where c.id in (1,2)", Competence.class);
                    List<Competence> userCompetence = query.getResultList();

                    userCompetence.forEach(userEntangle::addCompetence);

                    transactionalEntityManager.persist(userEntangle);

                    insertMockData(transactionalEntityManager, userEntangle);
                    platformTransactionManager.commit(transaction);
                    transactionalEntityManager.close();
                } catch (Exception e) {
                    if (!transaction.isCompleted()) {
                        platformTransactionManager.rollback(transaction);
                    }
                    logger.error(e);
                }
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                TypedQuery<Disease> namedQuery = entityManager.createNamedQuery("Disease.fetchAllDiseaseForUser", Disease.class);
                List<Disease> response = namedQuery.getResultList();
                logger.debug("Response size: {}", response.size());
            }
        };
    }

    private void insertMockData(EntityManager entityManager,
                                UserEntangle userEntangle) {
        IntStream.range(0, 4).forEach(i -> {
            logger.debug("Index : {}", i);
            Treatment treatment1 = new Treatment();

            TreatmentItem item1 = new TreatmentItem();
            item1.setDescription("Treatment_item_1");
            item1.setType(CompetenceType.D1.getName());

            TreatmentItem item2 = new TreatmentItem();
            item2.setDescription("Treatment_item_2");
            item2.setType(CompetenceType.D2.getName());

            treatment1.addTreatmentItem(item1);
            treatment1.addTreatmentItem(item2);

            Treatment treatment4 = new Treatment();
            TreatmentItem itemTreatment4 = new TreatmentItem();
            itemTreatment4.setDescription("Treatment_item_4");
            itemTreatment4.setType(CompetenceType.D4.getName());
            treatment4.addTreatmentItem(itemTreatment4);


            Treatment treatment3 = new Treatment();
            TreatmentItem item3 = new TreatmentItem();
            item3.setDescription("Treatment_item_1");
            item3.setType(CompetenceType.D3.getName());
            treatment3.addTreatmentItem(item3);

            userEntangle.addTreatment(treatment1);
            userEntangle.addTreatment(treatment4);
            userEntangle.addTreatment(treatment3);

            Disease disease1 = new Disease();
            disease1.setName("disease1");
            disease1.addTreatment(treatment1);
            disease1.addTreatment(treatment3);

            Disease disease2 = new Disease();
            disease2.setName("disease2");
            disease2.addTreatment(treatment4);
            entityManager.persist(disease1);
            entityManager.persist(disease2);
        });

        entityManager.persist(userEntangle);
    }
}

