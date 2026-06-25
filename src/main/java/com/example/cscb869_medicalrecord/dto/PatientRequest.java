package com.example.cscb869_medicalrecord.dto;

import com.example.cscb869_medicalrecord.enums.HealthInsuranceStatus;

public record PatientRequest(
        String firstName,
        String lastName,
        String fullName,
        String ssn,
        Long doctorId,
        HealthInsuranceStatus insuranceStatus)
{
}
