package com.example.cscb869_medicalrecord.dto;

import com.example.cscb869_medicalrecord.enums.HealthInsuranceStatus;

public record PatientResponse(
        Long id,
        String name,
        String ssn,
        Long doctorId,
        String doctorName,
        HealthInsuranceStatus insuranceStatus)
{
}
