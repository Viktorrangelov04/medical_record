package com.example.cscb869_medicalrecord.service;

import com.example.cscb869_medicalrecord.dto.VisitationRequest;
import com.example.cscb869_medicalrecord.dto.VisitationResponse;
import com.example.cscb869_medicalrecord.entity.*;
import com.example.cscb869_medicalrecord.enums.HealthInsuranceStatus;
import com.example.cscb869_medicalrecord.enums.Payer;
import com.example.cscb869_medicalrecord.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VisitationServiceTest {

    @Mock
    private VisitationRepository visitationRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DiagnosisRepository diagnosisRepository;

    @InjectMocks
    private VisitationService visitationService;

    private Doctor testDoctor;
    private Patient insuredPatient;
    private Patient uninsuredPatient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testDoctor = new Doctor();
        testDoctor.setFirstName("Петър");
        testDoctor.setLastName("Петров");
        testDoctor.setExamFee(new BigDecimal("40.00"));

        insuredPatient = new Patient();
        insuredPatient.setFirstName("Иван");
        insuredPatient.setLastName("Иванов");
        insuredPatient.setInsuranceStatus(HealthInsuranceStatus.INSURED);

        uninsuredPatient = new Patient();
        uninsuredPatient.setFirstName("Георги");
        uninsuredPatient.setLastName("Георгиев");
        uninsuredPatient.setInsuranceStatus(HealthInsuranceStatus.UNINSURED);
    }

    @Test
    void create_WhenPatientIsInsured_ShouldSetPayerToNZOK() {
        // Arrange
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(insuredPatient));
        when(visitationRepository.save(any(Visitation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VisitationRequest request = new VisitationRequest(
                LocalDate.now(), 1L, 1L, null, "Домашно лечение", false, null, null
        );

        // Act
        VisitationResponse response = visitationService.create(request);

        // Assert
        assertEquals(Payer.NZOK, response.payer()); // Заплаща се от НЗОК
        assertEquals(new BigDecimal("40.00"), response.price()); // Проверяваме цената
        verify(visitationRepository, times(1)).save(any(Visitation.class));
    }

    @Test
    void create_WhenPatientIsUninsured_ShouldSetPayerToPatient() {
        // Arrange
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(patientRepository.findById(2L)).thenReturn(Optional.of(uninsuredPatient));
        when(visitationRepository.save(any(Visitation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VisitationRequest request = new VisitationRequest(
                LocalDate.now(), 1L, 2L, null, "Предписани лекарства", false, null, null
        );

        // Act
        VisitationResponse response = visitationService.create(request);

        // Assert
        assertEquals(Payer.PATIENT, response.payer()); // Заплаща се лично от Пациента!
        assertEquals(new BigDecimal("40.00"), response.price());
    }
}