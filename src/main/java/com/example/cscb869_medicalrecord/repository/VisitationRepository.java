package com.example.cscb869_medicalrecord.repository;

import com.example.cscb869_medicalrecord.entity.Visitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VisitationRepository extends JpaRepository<Visitation, Long> {

    List<Visitation> findByPatientId(Long patientId);
    List<Visitation> findByDoctorId(Long doctorId);
}
