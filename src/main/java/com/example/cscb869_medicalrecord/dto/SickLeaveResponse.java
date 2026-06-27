package com.example.cscb869_medicalrecord.dto;

import java.time.LocalDate;

public record SickLeaveResponse(
        Long id,
        LocalDate startDate,
        int days,
        Long visitationId,
        String patientName,
        String doctorName
) {}
