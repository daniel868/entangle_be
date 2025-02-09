package org.example.entablebe.repository;

import org.example.entablebe.entity.ContactEntangle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntangle, Long> {
}
