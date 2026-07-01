package com.example.cscb869_medicalrecord.repository;

import com.example.cscb869_medicalrecord.entity.SickLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {
    Optional<SickLeave> findByVisitationId(Long visitationId);

    @Query("SELECT MONTH(s.startDate), COUNT(s) FROM SickLeave s " +
            "GROUP BY MONTH(s.startDate) ORDER BY COUNT(s) DESC")
    List<Object[]> findMostActiveMonthForSickLeaves(Pageable pageable);

    @Query("SELECT s.visitation.doctor.id, s.visitation.doctor.firstName, s.visitation.doctor.lastName, COUNT(s) " +
            "FROM SickLeave s GROUP BY s.visitation.doctor.id, s.visitation.doctor.firstName, s.visitation.doctor.lastName " +
            "ORDER BY COUNT(s) DESC")
    List<Object[]> findDoctorsWithMostSickLeaves(Pageable pageable);
}
