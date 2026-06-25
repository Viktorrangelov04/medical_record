package com.example.cscb869_medicalrecord.dto;

import com.example.cscb869_medicalrecord.enums.Payer;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VisitationResponse(
        Long id,
        LocalDate date,
        Long doctorId,
        String doctorName,
        Long patientId,
        String patientName,
        Long diagnosisId,
        String diagnosisName,
        String treatment,
        BigDecimal price,
        Payer payer
) {}
