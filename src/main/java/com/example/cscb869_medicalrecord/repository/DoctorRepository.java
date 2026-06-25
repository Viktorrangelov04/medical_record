package com.example.cscb869_medicalrecord.repository;

import com.example.cscb869_medicalrecord.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUin(String uin);
    boolean existsByUin(String uin);
}
