package com.example.cscb869_medicalrecord.controller;

import com.example.cscb869_medicalrecord.dto.VisitationRequest;
import com.example.cscb869_medicalrecord.dto.VisitationResponse;
import com.example.cscb869_medicalrecord.service.VisitationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitations")
public class VisitationController {

    private final VisitationService visitationService;

    public VisitationController(VisitationService visitationService) {
        this.visitationService = visitationService;
    }

    @GetMapping
    public List<VisitationResponse> getAll() {
        return visitationService.findAll();
    }

    @GetMapping("/{id}")
    public VisitationResponse getById(@PathVariable Long id) {
        return visitationService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitationResponse create(@RequestBody VisitationRequest request) {
        return visitationService.create(request);
    }

    @PutMapping("/{id}")
    public VisitationResponse update(@PathVariable Long id,
                                     @RequestBody VisitationRequest request) {
        return visitationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        visitationService.delete(id);
    }
}
