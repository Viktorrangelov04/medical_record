package com.example.cscb869_medicalrecord.dto.stats;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DoctorVisitationResponse(
        Long visitationId,
        LocalDate date,
        String patientName,
        String diagnosisName,
        String treatment,
        BigDecimal price
) {}

