package com.example.cscb869_medicalrecord.service;

import com.example.cscb869_medicalrecord.dto.DoctorRequest;
import com.example.cscb869_medicalrecord.dto.DoctorResponse;
import com.example.cscb869_medicalrecord.dto.SpecialtyResponse;
import com.example.cscb869_medicalrecord.entity.Doctor;
import com.example.cscb869_medicalrecord.entity.Specialty;
import com.example.cscb869_medicalrecord.entity.User;
import com.example.cscb869_medicalrecord.enums.Role;
import com.example.cscb869_medicalrecord.repository.DoctorRepository;
import com.example.cscb869_medicalrecord.repository.SpecialtyRepository;
import com.example.cscb869_medicalrecord.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository,
                         SpecialtyRepository specialtyRepository,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.specialtyRepository = specialtyRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        doctor = doctorRepository.save(doctor);

        User user = new User();
        user.setUsername(doctor.getUin());
        user.setPassword(passwordEncoder.encode("doctor123"));
        user.setRole(Role.DOCTOR);
        userRepository.save(user);
        return toResponse(doctor);
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
