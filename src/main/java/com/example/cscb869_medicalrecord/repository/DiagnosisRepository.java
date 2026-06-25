package com.example.cscb869_medicalrecord.repository;

import com.example.cscb869_medicalrecord.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
}
