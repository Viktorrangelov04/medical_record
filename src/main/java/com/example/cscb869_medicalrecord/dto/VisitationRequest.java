package com.example.cscb869_medicalrecord.dto;

import java.time.LocalDate;

public record VisitationRequest(
        LocalDate date,
        Long doctorId,
        Long patientId,
        Long diagnosisId,
        String treatment
) {}
