package org.example.entablebe.repository;

import jakarta.persistence.NamedQuery;
import org.example.entablebe.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    Integer countAllDisease(String searchString);

    @Query(value = "select d from Disease d left join fetch d.medicalNotes where d.id =:id")
    Disease loadDiseaseWithMedicalNotes(@Param("id") Long id);
}
