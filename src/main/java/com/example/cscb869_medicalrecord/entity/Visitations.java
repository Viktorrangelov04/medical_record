package com.example.cscb869_medicalrecord.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="visitations")
public class Visitations extends org.example.entity.BaseEntity {
    @Column(nullable = false)
    private LocalDate visitationDate;

    @ManyToOne
    @JoinColumn(name="doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name="patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "diagnosis_id", nullable = false)
    private Diagnosis diagnosis;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String treatment;

    @Column(nullable = false)
    private BigDecimal totalCost;

    @Column(nullable = false)
    private BigDecimal pricePaidByPatient;

    @Column(nullable = false)
    private BigDecimal pricePaidByNzok;

}
