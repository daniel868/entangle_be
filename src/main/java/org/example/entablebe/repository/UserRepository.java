package org.example.entablebe.repository;

import jakarta.persistence.NamedQuery;
import org.example.entablebe.entity.UserEntangle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntangle, Long> {

    @Query(value = "select us from UserEntangle us where us.username = :username")
    UserEntangle findByUsername(String username);

    @Query(value = "select us from UserEntangle us where us.email =:email ")
    List<UserEntangle> findUserByEmail(String email);

    UserEntangle fetchUserWithCompetences(Long userId);
}
