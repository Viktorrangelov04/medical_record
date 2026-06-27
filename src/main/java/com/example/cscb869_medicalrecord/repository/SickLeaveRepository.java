package com.example.cscb869_medicalrecord.repository;

import com.example.cscb869_medicalrecord.entity.SickLeave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {
    Optional<SickLeave> findByVisitationId(Long visitationId);
}
