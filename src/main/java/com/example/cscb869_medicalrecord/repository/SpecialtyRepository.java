package com.example.cscb869_medicalrecord.repository;

import com.example.cscb869_medicalrecord.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    Optional<Specialty> findById(Long specialtyId);
    boolean existsByName(String name);
    Optional<Specialty> findByName(String name);
}
