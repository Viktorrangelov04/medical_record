package com.example.cscb869_medicalrecord.dto.stats;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PatientVisitationResponse(
        Long visitationId,
        LocalDate date,
        String doctorName,
        String diagnosisName,
        String treatment,
        BigDecimal price
) {}
