package com.example.cscb869_medicalrecord.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="doctor")
public class Doctor extends org.example.entity.BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true, length = 10)
    private String uin;
    @ManyToMany
    @JoinTable(
            name = "doctors_specialties",
            joinColumns=@JoinColumn(name="doctor_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name="specialty_id", nullable = false)

    )
    private Set<Specialty> specialties = new HashSet<>();

    private BigDecimal examFee;


}
