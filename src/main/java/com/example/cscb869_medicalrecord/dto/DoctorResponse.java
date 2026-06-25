package com.example.cscb869_medicalrecord.dto;

import java.math.BigDecimal;
import java.util.Set;

public record DoctorResponse(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        String uin,
        Set<SpecialtyResponse> specialties,
        BigDecimal examFee
) {}
