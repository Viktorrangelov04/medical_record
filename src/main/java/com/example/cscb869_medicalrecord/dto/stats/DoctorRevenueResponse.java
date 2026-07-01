package com.example.cscb869_medicalrecord.dto.stats;

import java.math.BigDecimal;
public record DoctorRevenueResponse(Long doctorId, String doctorName, BigDecimal totalAmount) {}
