package org.example.entablebe.repository;

import jakarta.persistence.NamedQuery;
import org.example.entablebe.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    @Query(value = "select t from Treatment t left join fetch t.items where t.id =:id")
    Treatment findTreatmentByIdWithItems(Long id);
}
