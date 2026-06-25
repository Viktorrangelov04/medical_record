package com.example.cscb869_medicalrecord.service;

import com.example.cscb869_medicalrecord.dto.PatientRequest;
import com.example.cscb869_medicalrecord.dto.PatientResponse;
import com.example.cscb869_medicalrecord.entity.Doctor;
import com.example.cscb869_medicalrecord.entity.Patient;
import com.example.cscb869_medicalrecord.repository.DoctorRepository;
import com.example.cscb869_medicalrecord.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PatientService(PatientRepository patientRepository,
                          DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public PatientResponse findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + id));
        return toResponse(patient);
    }

    public PatientResponse create(PatientRequest request) {
        if (patientRepository.existsBySsn(request.ssn())) {
            throw new RuntimeException("SSN already exists: " + request.ssn());
        }
        Patient patient = new Patient();
        applyRequest(patient, request);
        return toResponse(patientRepository.save(patient));
    }

    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + id));
        applyRequest(patient, request);
        return toResponse(patientRepository.save(patient));
    }

    public void delete(Long id) {
        patientRepository.deleteById(id);
    }

    private void applyRequest(Patient patient, PatientRequest request) {
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + request.doctorId()));
        patient.setName(request.name());
        patient.setSsn(request.ssn());
        patient.setDoctor(doctor);
        patient.setInsuranceStatus(request.insuranceStatus());
    }

    private PatientResponse toResponse(Patient p) {
        return new PatientResponse(
                p.getId(),
                p.getName(),
                p.getSsn(),
                p.getDoctor().getId(),
                p.getDoctor().getName(),
                p.getInsuranceStatus()
        );
    }
}
