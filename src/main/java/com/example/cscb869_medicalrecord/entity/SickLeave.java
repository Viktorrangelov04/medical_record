package com.example.cscb869_medicalrecord.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="sick_leave")
public class SickLeave extends org.example.entity.BaseEntity {
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private int days;
    @OneToOne
    @JoinColumn(name = "examination_id", nullable = false)
    private Visitation visitation;
}
