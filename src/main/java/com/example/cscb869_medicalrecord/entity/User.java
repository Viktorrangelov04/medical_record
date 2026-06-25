package com.example.cscb869_medicalrecord.entity;

import com.example.cscb869_medicalrecord.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="users")
public class User extends org.example.entity.BaseEntity {
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    private Patient patient;

    @OneToOne
    private Doctor doctor;
}
