package com.example.cscb869_medicalrecord.repository;

import com.example.cscb869_medicalrecord.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findBySsn(String ssn);
    List<Patient> findByDoctorId(Long doctorId);
    boolean existsBySsn(String ssn);
}
