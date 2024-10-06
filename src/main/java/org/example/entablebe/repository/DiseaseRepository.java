package org.example.entablebe.repository;

import org.example.entablebe.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    Integer countAllDisease();
}
