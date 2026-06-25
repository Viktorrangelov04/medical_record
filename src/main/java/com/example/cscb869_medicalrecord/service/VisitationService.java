package com.example.cscb869_medicalrecord.service;

import com.example.cscb869_medicalrecord.dto.VisitationRequest;
import com.example.cscb869_medicalrecord.dto.VisitationResponse;
import com.example.cscb869_medicalrecord.entity.Diagnosis;
import com.example.cscb869_medicalrecord.entity.Doctor;
import com.example.cscb869_medicalrecord.entity.Patient;
import com.example.cscb869_medicalrecord.entity.Visitation;
import com.example.cscb869_medicalrecord.enums.HealthInsuranceStatus;
import com.example.cscb869_medicalrecord.enums.Payer;
import com.example.cscb869_medicalrecord.repository.DiagnosisRepository;
import com.example.cscb869_medicalrecord.repository.DoctorRepository;
import com.example.cscb869_medicalrecord.repository.PatientRepository;
import com.example.cscb869_medicalrecord.repository.VisitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitationService {

    private final VisitationRepository visitationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DiagnosisRepository diagnosisRepository;

    public VisitationService(VisitationRepository visitationRepository,
                             DoctorRepository doctorRepository,
                             PatientRepository patientRepository,
                             DiagnosisRepository diagnosisRepository) {
        this.visitationRepository = visitationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.diagnosisRepository = diagnosisRepository;
    }

    public List<VisitationResponse> findAll() {
        return visitationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public VisitationResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public VisitationResponse create(VisitationRequest request) {
        Visitation visitation = new Visitation();
        applyRequest(visitation, request);
        return toResponse(visitationRepository.save(visitation));
    }

    public VisitationResponse update(Long id, VisitationRequest request) {
        Visitation visitation = getOrThrow(id);
        applyRequest(visitation, request);
        return toResponse(visitationRepository.save(visitation));
    }

    public void delete(Long id) {
        visitationRepository.deleteById(id);
    }

    // ---- helpers ----

    private Visitation getOrThrow(Long id) {
        return visitationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visitation not found: " + id));
    }

    private void applyRequest(Visitation visitation, VisitationRequest request) {
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + request.doctorId()));
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new RuntimeException("Patient not found: " + request.patientId()));

        Diagnosis diagnosis = null;
        if (request.diagnosisId() != null) {
            diagnosis = diagnosisRepository.findById(request.diagnosisId())
                    .orElseThrow(() ->
                            new RuntimeException("Diagnosis not found: " + request.diagnosisId()));
        }

        visitation.setDate(request.date());
        visitation.setDoctor(doctor);
        visitation.setPatient(patient);
        visitation.setDiagnosis(diagnosis);
        visitation.setTreatment(request.treatment());

        // --- БИЗНЕС ЛОГИКА ---
        // 1. цената идва от таксата на лекаря
        visitation.setPrice(doctor.getExamFee());

        // 2. кой плаща - според осигурителния статус на пациента
        Payer payer = patient.getInsuranceStatus() == HealthInsuranceStatus.INSURED
                ? Payer.NZOK
                : Payer.PATIENT;
        visitation.setPayer(payer);
    }

    private VisitationResponse toResponse(Visitation v) {
        return new VisitationResponse(
                v.getId(),
                v.getDate(),
                v.getDoctor().getId(),
                v.getDoctor().getFullName(),
                v.getPatient().getId(),
                v.getPatient().getFullName(),
                v.getDiagnosis() != null ? v.getDiagnosis().getId() : null,
                v.getDiagnosis() != null ? v.getDiagnosis().getName() : null,
                v.getTreatment(),
                v.getPrice(),
                v.getPayer()
        );
    }
}
