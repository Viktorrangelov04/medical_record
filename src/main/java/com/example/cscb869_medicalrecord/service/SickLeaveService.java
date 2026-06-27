package com.example.cscb869_medicalrecord.service;

import com.example.cscb869_medicalrecord.dto.SickLeaveRequest;
import com.example.cscb869_medicalrecord.dto.SickLeaveResponse;
import com.example.cscb869_medicalrecord.entity.SickLeave;
import com.example.cscb869_medicalrecord.entity.Visitation;
import com.example.cscb869_medicalrecord.repository.SickLeaveRepository;
import com.example.cscb869_medicalrecord.repository.VisitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SickLeaveService {

    private final SickLeaveRepository sickLeaveRepository;
    private final VisitationRepository visitationRepository;

    public SickLeaveService(SickLeaveRepository sickLeaveRepository,
                            VisitationRepository visitationRepository) {
        this.sickLeaveRepository = sickLeaveRepository;
        this.visitationRepository = visitationRepository;
    }

    public List<SickLeaveResponse> findAll() {
        return sickLeaveRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public SickLeaveResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public SickLeaveResponse create(SickLeaveRequest request) {
        if (sickLeaveRepository.findByVisitationId(request.visitationId()).isPresent()) {
            throw new RuntimeException("A sick leave already exists for visitation ID: " + request.visitationId());
        }

        SickLeave sickLeave = new SickLeave();
        applyRequest(sickLeave, request);
        return toResponse(sickLeaveRepository.save(sickLeave));
    }

    public SickLeaveResponse update(Long id, SickLeaveRequest request) {
        SickLeave sickLeave = getOrThrow(id);

        if (!sickLeave.getVisitation().getId().equals(request.visitationId())) {
            if (sickLeaveRepository.findByVisitationId(request.visitationId()).isPresent()) {
                throw new RuntimeException("A sick leave already exists for visitation ID: " + request.visitationId());
            }
        }

        applyRequest(sickLeave, request);
        return toResponse(sickLeaveRepository.save(sickLeave));
    }

    public void delete(Long id) {
        sickLeaveRepository.deleteById(id);
    }

    private SickLeave getOrThrow(Long id) {
        return sickLeaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sick leave not found: " + id));
    }

    private void applyRequest(SickLeave sickLeave, SickLeaveRequest request) {
        Visitation visitation = visitationRepository.findById(request.visitationId())
                .orElseThrow(() -> new RuntimeException("Visitation not found: " + request.visitationId()));

        sickLeave.setStartDate(request.startDate());
        sickLeave.setDays(request.days());
        sickLeave.setVisitation(visitation);
    }

    private SickLeaveResponse toResponse(SickLeave s) {
        Visitation v = s.getVisitation();
        String patientFullName = v.getPatient().getFirstName() + " " + v.getPatient().getLastName();
        String doctorFullName = v.getDoctor().getFirstName() + " " + v.getDoctor().getLastName();

        return new SickLeaveResponse(
                s.getId(),
                s.getStartDate(),
                s.getDays(),
                v.getId(),
                patientFullName,
                doctorFullName
        );
    }
}