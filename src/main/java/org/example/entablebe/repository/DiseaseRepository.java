package org.example.entablebe.repository;

import org.example.entablebe.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    List<Disease> fetchAllDiseaseForUser(List<String> userTreatmentTypes);
}
