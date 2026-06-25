package com.example.cscb869_medicalrecord.dto;

import com.example.cscb869_medicalrecord.enums.HealthInsuranceStatus;

public record PatientRequest(
        String name,
        String ssn,
        Long doctorId,
        HealthInsuranceStatus insuranceStatus)
{
}
