package com.example.cscb869_medicalrecord.service;

import com.example.cscb869_medicalrecord.config.SecurityUtils;
import com.example.cscb869_medicalrecord.dto.VisitationRequest;
import com.example.cscb869_medicalrecord.dto.VisitationResponse;
import com.example.cscb869_medicalrecord.entity.*;
import com.example.cscb869_medicalrecord.enums.HealthInsuranceStatus;
import com.example.cscb869_medicalrecord.enums.Payer;
import com.example.cscb869_medicalrecord.enums.Role;
import com.example.cscb869_medicalrecord.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitationService {

    private final VisitationRepository visitationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final SickLeaveRepository sickLeaveRepository;
    private final SecurityUtils securityUtils;

    public VisitationService(VisitationRepository visitationRepository,
                             DoctorRepository doctorRepository,
                             PatientRepository patientRepository,
                             DiagnosisRepository diagnosisRepository,
                             SickLeaveRepository sickLeaveRepository,
                             SecurityUtils securityUtils
                             ) {
        this.visitationRepository = visitationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.securityUtils = securityUtils;
        this.sickLeaveRepository = sickLeaveRepository;
    }

    public List<VisitationResponse> findAll() {
        return visitationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public VisitationResponse findById(Long id) {
        Visitation visitation = getOrThrow(id);
        User currentUser = securityUtils.getCurrentUser();

        if (currentUser.getRole() == Role.PATIENT) {
            Long patientIdOfVisitation = visitation.getPatient().getId();
            Long loggedInPatientId = currentUser.getPatient().getId();

            if (!patientIdOfVisitation.equals(loggedInPatientId)) {
                throw new RuntimeException("Access Denied: You cannot view other patients' records.");
            }
        }

        return toResponse(visitation);
    }

    public VisitationResponse create(VisitationRequest request) {
        Visitation visitation = new Visitation();
        applyRequest(visitation, request);
        Visitation savedVisitation = visitationRepository.save(visitation);

        if (request.borderSickLeave() && request.sickLeaveStartDate() != null && request.sickLeaveDays() != null) {
            SickLeave sickLeave = new SickLeave();
            sickLeave.setStartDate(request.sickLeaveStartDate());
            sickLeave.setDays(request.sickLeaveDays());
            sickLeave.setVisitation(savedVisitation);
            sickLeaveRepository.save(sickLeave);
        }

        return toResponse(savedVisitation);
    }

    public VisitationResponse update(Long id, VisitationRequest request) {
        Visitation visitation = getOrThrow(id);
        User currentUser = securityUtils.getCurrentUser();

        if (currentUser.getRole() == Role.DOCTOR) {
            Long doctorIdOfVisitation = visitation.getDoctor().getId();
            Long loggedInDoctorId = currentUser.getDoctor().getId();

            if (!doctorIdOfVisitation.equals(loggedInDoctorId)) {
                throw new RuntimeException("Access Denied: You can only edit your own visitations.");
            }
        } else if (currentUser.getRole() == Role.PATIENT) {
            throw new RuntimeException("Access Denied: Patients cannot edit visitations.");
        }

        applyRequest(visitation, request);
        return toResponse(visitationRepository.save(visitation));
    }

    public void delete(Long id) {
        visitationRepository.deleteById(id);
    }

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

        visitation.setPrice(doctor.getExamFee());

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
