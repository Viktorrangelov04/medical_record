package com.example.cscb869_medicalrecord.service;

import com.example.cscb869_medicalrecord.dto.PatientResponse;
import com.example.cscb869_medicalrecord.dto.VisitationResponse;
import com.example.cscb869_medicalrecord.dto.stats.DoctorCountResponse;
import com.example.cscb869_medicalrecord.dto.stats.DoctorRevenueResponse;
import com.example.cscb869_medicalrecord.dto.stats.MonthCountResponse;
import com.example.cscb869_medicalrecord.entity.Diagnosis;
import com.example.cscb869_medicalrecord.entity.Patient;
import com.example.cscb869_medicalrecord.entity.Visitation;
import com.example.cscb869_medicalrecord.repository.PatientRepository;
import com.example.cscb869_medicalrecord.repository.SickLeaveRepository;
import com.example.cscb869_medicalrecord.repository.VisitationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final PatientRepository patientRepository;
    private final VisitationRepository visitationRepository;
    private final SickLeaveRepository sickLeaveRepository;

    public ReportService(PatientRepository patientRepository,
                         VisitationRepository visitationRepository,
                         SickLeaveRepository sickLeaveRepository) {
        this.patientRepository = patientRepository;
        this.visitationRepository = visitationRepository;
        this.sickLeaveRepository = sickLeaveRepository;
    }

    public List<PatientResponse> getPatientsByDiagnosis(Long diagnosisId) {
        return visitationRepository.findPatientsByDiagnosisId(diagnosisId).stream()
                .map(this::toPatientResponse)
                .toList();
    }

    public Diagnosis getMostCommonDiagnosis() {
        List<Diagnosis> result = visitationRepository.findMostCommonDiagnosis(PageRequest.of(0, 1));
        return result.isEmpty() ? null : result.get(0);
    }

    public List<PatientResponse> getPatientsByPersonalDoctor(Long doctorId) {
        return patientRepository.findByDoctorId(doctorId).stream()
                .map(this::toPatientResponse)
                .toList();
    }

    public BigDecimal getTotalAmountPaidByPatients() {
        return visitationRepository.getTotalAmountPaidByPatients();
    }

    public List<DoctorRevenueResponse> getAmountPaidByPatientsGroupedByDoctor() {
        return visitationRepository.getAmountPaidByPatientsGroupedByDoctor().stream()
                .map(obj -> new DoctorRevenueResponse(
                        (Long) obj[0],
                        obj[1] + " " + obj[2],
                        (BigDecimal) obj[3]))
                .toList();
    }

    public List<DoctorCountResponse> getPatientCountPerDoctor() {
        return patientRepository.countPatientsPerDoctor().stream()
                .map(obj -> new DoctorCountResponse(
                        (Long) obj[0],
                        obj[1] + " " + obj[2],
                        (Long) obj[3]))
                .toList();
    }

    public List<DoctorCountResponse> getVisitationCountPerDoctor() {
        return visitationRepository.countVisitationsPerDoctor().stream()
                .map(obj -> new DoctorCountResponse(
                        (Long) obj[0],
                        obj[1] + " " + obj[2],
                        (Long) obj[3]))
                .toList();
    }

    public List<VisitationResponse> getPatientVisitationHistory(Long patientId) {
        return visitationRepository.findByPatientId(patientId).stream()
                .map(this::toVisitationResponse)
                .toList();
    }

    public List<VisitationResponse> getVisitationsByDoctorAndPeriod(Long doctorId, LocalDate start, LocalDate end) {
        return visitationRepository.findVisitationsByDoctorAndPeriod(doctorId, start, end).stream()
                .map(this::toVisitationResponse)
                .toList();
    }

    public MonthCountResponse getMostActiveMonthForSickLeaves() {
        List<Object[]> result = sickLeaveRepository.findMostActiveMonthForSickLeaves(PageRequest.of(0, 1));
        if (result.isEmpty()) return null;
        Object[] row = result.get(0);
        return new MonthCountResponse((Integer) row[0], (Long) row[1]);
    }

    public List<DoctorCountResponse> getDoctorsWithMostSickLeaves() {
        return sickLeaveRepository.findDoctorsWithMostSickLeaves(PageRequest.of(0, 3)).stream() // Топ 3
                .map(obj -> new DoctorCountResponse(
                        (Long) obj[0],
                        obj[1] + " " + obj[2],
                        (Long) obj[3]))
                .toList();
    }

    private PatientResponse toPatientResponse(Patient p) {
        return new PatientResponse(
                p.getId(),
                p.getFirstName(),
                p.getLastName(),
                p.getFirstName() + " " + p.getLastName(),
                p.getSsn(),
                p.getDoctor().getId(),
                p.getDoctor().getFirstName() + " " + p.getDoctor().getLastName(),
                p.getInsuranceStatus()
        );
    }

    private VisitationResponse toVisitationResponse(Visitation v) {
        return new VisitationResponse(
                v.getId(),
                v.getDate(),
                v.getDoctor().getId(),
                v.getDoctor().getFirstName() + " " + v.getDoctor().getLastName(),
                v.getPatient().getId(),
                v.getPatient().getFirstName() + " " + v.getPatient().getLastName(),
                v.getDiagnosis() != null ? v.getDiagnosis().getId() : null,
                v.getDiagnosis() != null ? v.getDiagnosis().getName() : null,
                v.getTreatment(),
                v.getPrice(),
                v.getPayer()
        );
    }
}
