package com.example.cscb869_medicalrecord.service;

import com.example.cscb869_medicalrecord.dto.DiagnosisRequest;
import com.example.cscb869_medicalrecord.dto.DiagnosisResponse;
import com.example.cscb869_medicalrecord.entity.Diagnosis;
import com.example.cscb869_medicalrecord.repository.DiagnosisRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisService(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    public List<DiagnosisResponse> findAll() {
        return diagnosisRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public DiagnosisResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public DiagnosisResponse create(DiagnosisRequest request) {
        if (diagnosisRepository.existsByName(request.name())) {
            throw new RuntimeException("Diagnosis already exists: " + request.name());
        }
        Diagnosis diagnosis = new Diagnosis();
        applyRequest(diagnosis, request);
        return toResponse(diagnosisRepository.save(diagnosis));
    }

    public DiagnosisResponse update(Long id, DiagnosisRequest request) {
        Diagnosis diagnosis = getOrThrow(id);
        applyRequest(diagnosis, request);
        return toResponse(diagnosisRepository.save(diagnosis));
    }

    public void delete(Long id) {
        diagnosisRepository.deleteById(id);
    }

    private Diagnosis getOrThrow(Long id) {
        return diagnosisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found: " + id));
    }

    private void applyRequest(Diagnosis diagnosis, DiagnosisRequest request) {
        diagnosis.setName(request.name());
    }

    private DiagnosisResponse toResponse(Diagnosis d) {
        return new DiagnosisResponse(d.getId(), d.getName());
    }
}
