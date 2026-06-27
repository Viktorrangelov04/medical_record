package com.example.cscb869_medicalrecord.controller;

import com.example.cscb869_medicalrecord.dto.SickLeaveRequest;
import com.example.cscb869_medicalrecord.dto.SickLeaveResponse;
import com.example.cscb869_medicalrecord.service.SickLeaveService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sick-leaves")
public class SickLeaveController {

    private final SickLeaveService sickLeaveService;

    public SickLeaveController(SickLeaveService sickLeaveService) {
        this.sickLeaveService = sickLeaveService;
    }

    @GetMapping
    public List<SickLeaveResponse> getAll() {
        return sickLeaveService.findAll();
    }

    @GetMapping("/{id}")
    public SickLeaveResponse getById(@PathVariable Long id) {
        return sickLeaveService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SickLeaveResponse create(@RequestBody SickLeaveRequest request) {
        return sickLeaveService.create(request);
    }

    @PutMapping("/{id}")
    public SickLeaveResponse update(@PathVariable Long id, @RequestBody SickLeaveRequest request) {
        return sickLeaveService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        sickLeaveService.delete(id);
    }
}
