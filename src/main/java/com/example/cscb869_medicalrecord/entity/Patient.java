package com.example.cscb869_medicalrecord.entity;

import com.example.cscb869_medicalrecord.enums.HealthInsuranceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="patients")
public class Patient extends BaseEntity {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true, length = 10)
    private String ssn;

    @ManyToOne
    @JoinColumn(name="doctor_id", nullable=false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HealthInsuranceStatus insuranceStatus;


    public String getFullName() {
        return firstName + " " + lastName;
    }
}
