package com.example.cscb869_medicalrecord.repository;

import com.example.cscb869_medicalrecord.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
