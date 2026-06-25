package com.example.cscb869_medicalrecord.service;

import com.example.cscb869_medicalrecord.dto.DoctorRequest;
import com.example.cscb869_medicalrecord.dto.DoctorResponse;
import com.example.cscb869_medicalrecord.dto.SpecialtyResponse;
import com.example.cscb869_medicalrecord.entity.Doctor;
import com.example.cscb869_medicalrecord.entity.Specialty;
import com.example.cscb869_medicalrecord.repository.DoctorRepository;
import com.example.cscb869_medicalrecord.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;

    public DoctorService(DoctorRepository doctorRepository,
                         SpecialtyRepository specialtyRepository) {
        this.doctorRepository = doctorRepository;
        this.specialtyRepository = specialtyRepository;
    }

    public List<DoctorResponse> findAll() {
        return doctorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public DoctorResponse findById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + id));
        return toResponse(doctor);
    }

    public DoctorResponse create(DoctorRequest request) {
        if (doctorRepository.existsByUin(request.uin())) {
            throw new RuntimeException("UIN already exists: " + request.uin());
        }
        Doctor doctor = new Doctor();
        applyRequest(doctor, request);
        return toResponse(doctorRepository.save(doctor));
    }

    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + id));
        applyRequest(doctor, request);
        return toResponse(doctorRepository.save(doctor));
    }

    public void delete(Long id) {
        doctorRepository.deleteById(id);
    }


    private void applyRequest(Doctor doctor, DoctorRequest request) {
        doctor.setFirstName(request.firstName());
        doctor.setLastName(request.lastName());
        doctor.setUin(request.uin());
        doctor.setExamFee(request.examFee());

        Set<Specialty> specialties = new HashSet<>();
        if (request.specialtyIds() != null) {
            for (Long specialtyId : request.specialtyIds()) {
                Specialty specialty = specialtyRepository.findById(specialtyId)
                        .orElseThrow(() ->
                                new RuntimeException("Specialty not found: " + specialtyId));
                specialties.add(specialty);
            }
        }
        doctor.setSpecialties(specialties);
    }

    private DoctorResponse toResponse(Doctor d) {
        Set<SpecialtyResponse> specialtyResponses = d.getSpecialties().stream()
                .map(s -> new SpecialtyResponse(s.getId(), s.getName()))
                .collect(Collectors.toSet());

        return new DoctorResponse(
                d.getId(),
                d.getFirstName(),
                d.getLastName(),
                d.getFullName(),
                d.getUin(),
                specialtyResponses,
                d.getExamFee()
        );
    }
}
