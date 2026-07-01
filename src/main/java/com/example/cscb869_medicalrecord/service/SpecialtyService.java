package com.example.cscb869_medicalrecord.service;

import com.example.cscb869_medicalrecord.dto.SpecialtyRequest;
import com.example.cscb869_medicalrecord.entity.Specialty;
import com.example.cscb869_medicalrecord.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

@Service
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    public void create(SpecialtyRequest request) {
        if (specialtyRepository.existsByName(request.name())) {
            throw new RuntimeException("Specialty already exists: " + request.name());
        }
        Specialty specialty = new Specialty();
        specialty.setName(request.name());
        specialtyRepository.save(specialty);
    }
}