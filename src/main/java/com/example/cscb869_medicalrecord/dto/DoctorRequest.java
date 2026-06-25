package com.example.cscb869_medicalrecord.dto;

import com.example.cscb869_medicalrecord.entity.Specialty;

import java.math.BigDecimal;
import java.util.Set;

public record DoctorRequest(
        String firstName,
        String lastName,
        String fullName,
        String uin,
        Set<Long> specialtyIds,
        BigDecimal examFee
) {}
