package com.example.cscb869_medicalrecord.dto;

import java.time.LocalDate;

public record SickLeaveRequest(
        LocalDate startDate,
        int days,
        Long visitationId
) {}
