package com.example.cscb869_medicalrecord.controller;

import com.example.cscb869_medicalrecord.dto.DiagnosisRequest;
import com.example.cscb869_medicalrecord.dto.DiagnosisResponse;
import com.example.cscb869_medicalrecord.service.DiagnosisService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnoses")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @GetMapping
    public List<DiagnosisResponse> getAll() {
        return diagnosisService.findAll();
    }

    @GetMapping("/{id}")
    public DiagnosisResponse getById(@PathVariable Long id) {
        return diagnosisService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DiagnosisResponse create(@RequestBody DiagnosisRequest request) {
        return diagnosisService.create(request);
    }

    @PutMapping("/{id}")
    public DiagnosisResponse update(@PathVariable Long id,
                                    @RequestBody DiagnosisRequest request) {
        return diagnosisService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        diagnosisService.delete(id);
    }
}