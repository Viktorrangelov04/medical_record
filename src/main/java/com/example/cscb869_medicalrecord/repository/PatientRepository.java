package com.example.cscb869_medicalrecord.repository;

import com.example.cscb869_medicalrecord.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findBySsn(String ssn);
    List<Patient> findByDoctorId(Long doctorId);
    boolean existsBySsn(String ssn);

    @Query("SELECT p.doctor.id, p.doctor.firstName, p.doctor.lastName, COUNT(p) " +
            "FROM Patient p GROUP BY p.doctor.id, p.doctor.firstName, p.doctor.lastName")
    List<Object[]> countPatientsPerDoctor();
}
