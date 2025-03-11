package org.example.entablebe.repository;

import org.example.entablebe.entity.MedicalNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalNoteRepository extends JpaRepository<MedicalNote, Long> {
}
