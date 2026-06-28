package com.example.cscb869_medicalrecord.controller;

import com.example.cscb869_medicalrecord.dto.DoctorRequest;
import com.example.cscb869_medicalrecord.dto.DoctorResponse;
import com.example.cscb869_medicalrecord.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<DoctorResponse> getAll() {
        return doctorService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponse create(@RequestBody DoctorRequest request) {
        return doctorService.create(request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        doctorService.delete(id);
    }
}
