package com.example.cscb869_medicalrecord.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="diagnosis")
public class Diagnosis extends org.example.entity.BaseEntity {
    @Column(nullable=false, unique = true)
    private String code;
    @Column(nullable=false)
    private String name;
}
