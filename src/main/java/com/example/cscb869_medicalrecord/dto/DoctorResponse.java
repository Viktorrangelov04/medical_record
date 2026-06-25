package com.example.cscb869_medicalrecord.dto;

import java.math.BigDecimal;
import java.util.Set;

public record DoctorResponse(String firstName, String lastName, String email, Set<String> specialtyName, BigDecimal examFee) {
}
