package com.example.cscb869_medicalrecord.repository;

import com.example.cscb869_medicalrecord.entity.Diagnosis;
import com.example.cscb869_medicalrecord.entity.Patient;
import com.example.cscb869_medicalrecord.entity.Visitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface VisitationRepository extends JpaRepository<Visitation, Long> {

    List<Visitation> findByPatientId(Long patientId);
    List<Visitation> findByDoctorId(Long doctorId);

    @Query("SELECT DISTINCT v.patient FROM Visitation v WHERE v.diagnosis.id = :diagnosisId")
    List<Patient> findPatientsByDiagnosisId(@Param("diagnosisId") Long diagnosisId);

    @Query("SELECT v.diagnosis FROM Visitation v WHERE v.diagnosis IS NOT NULL " +
            "GROUP BY v.diagnosis ORDER BY COUNT(v) DESC")
    List<Diagnosis> findMostCommonDiagnosis(Pageable pageable);

    @Query("SELECT COALESCE(SUM(v.price), 0) FROM Visitation v WHERE v.payer = 'PATIENT'")
    BigDecimal getTotalAmountPaidByPatients();

    @Query("SELECT v.doctor.id, v.doctor.firstName, v.doctor.lastName, SUM(v.price) " +
            "FROM Visitation v WHERE v.payer = 'PATIENT' " +
            "GROUP BY v.doctor.id, v.doctor.firstName, v.doctor.lastName")
    List<Object[]> getAmountPaidByPatientsGroupedByDoctor();

    @Query("SELECT v.doctor.id, v.doctor.firstName, v.doctor.lastName, COUNT(v) " +
            "FROM Visitation v GROUP BY v.doctor.id, v.doctor.firstName, v.doctor.lastName")
    List<Object[]> countVisitationsPerDoctor();

    @Query("SELECT v FROM Visitation v WHERE " +
            "(:doctorId IS NULL OR v.doctor.id = :doctorId) AND " +
            "(:startDate IS NULL OR v.date >= :startDate) AND " +
            "(:endDate IS NULL OR v.date <= :endDate)")
    List<Visitation> findVisitationsByDoctorAndPeriod(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
