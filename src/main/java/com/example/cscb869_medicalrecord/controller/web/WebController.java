package com.example.cscb869_medicalrecord.controller.web;

import com.example.cscb869_medicalrecord.dto.*;
import com.example.cscb869_medicalrecord.enums.HealthInsuranceStatus;
import com.example.cscb869_medicalrecord.repository.SpecialtyRepository;
import com.example.cscb869_medicalrecord.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // нов импорт

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/web")
public class WebController {

    private final DiagnosisService diagnosisService;
    private final VisitationService visitationService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final ReportService reportService;
    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyService specialtyService;

    public WebController(PatientService patientService,
                         DoctorService doctorService,
                         ReportService reportService,
                         DiagnosisService diagnosisService,
                         VisitationService visitationService,
                         SpecialtyRepository specialtyRepository,
                         SpecialtyService specialtyService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.reportService = reportService;
        this.diagnosisService = diagnosisService;
        this.visitationService = visitationService;
        this.specialtyRepository = specialtyRepository;
        this.specialtyService = specialtyService;
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/patients")
    public String patientsPage(Model model) {
        model.addAttribute("patients", patientService.findAll());
        return "patients";
    }

    @GetMapping("/reports")
    public String reportsPage(Model model) {
        model.addAttribute("totalRevenue", reportService.getTotalAmountPaidByPatients());
        model.addAttribute("mostCommonDiagnosis", reportService.getMostCommonDiagnosis());
        model.addAttribute("revenueByDoctor", reportService.getAmountPaidByPatientsGroupedByDoctor());
        model.addAttribute("patientCountPerDoctor", reportService.getPatientCountPerDoctor());
        model.addAttribute("visitationCountPerDoctor", reportService.getVisitationCountPerDoctor());
        model.addAttribute("mostActiveMonth", reportService.getMostActiveMonthForSickLeaves());
        model.addAttribute("doctorsWithMostSickLeaves", reportService.getDoctorsWithMostSickLeaves());
        return "reports";
    }

    @GetMapping("/visitations/new")
    public String showCreateVisitationForm(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("diagnoses", diagnosisService.findAll());
        return "create-visitation";
    }

    @PostMapping("/visitations/new")
    public String createNewVisitation(@RequestParam String date,
                                      @RequestParam Long doctorId,
                                      @RequestParam Long patientId,
                                      @RequestParam(required = false) Long diagnosisId,
                                      @RequestParam String treatment,
                                      @RequestParam(required = false) boolean borderSickLeave,
                                      @RequestParam(required = false) String sickLeaveStartDate,
                                      @RequestParam(required = false) Integer sickLeaveDays,
                                      RedirectAttributes redirectAttributes) {
        try {
            LocalDate localDate = LocalDate.parse(date);

            LocalDate sd = (sickLeaveStartDate != null && !sickLeaveStartDate.isEmpty())
                    ? LocalDate.parse(sickLeaveStartDate) : null;

            VisitationRequest request = new VisitationRequest(
                    localDate, doctorId, patientId, diagnosisId, treatment,
                    borderSickLeave, sd, sickLeaveDays
            );

            visitationService.create(request);
            return "redirect:/web/home";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/web/visitations/new";
        }
    }

    @GetMapping("/setup")
    public String showSetupPage(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("specialties", specialtyRepository.findAll());
        return "setup";
    }

    @PostMapping("/patients/new")
    public String webCreatePatient(@RequestParam String firstName,
                                   @RequestParam String lastName,
                                   @RequestParam String ssn,
                                   @RequestParam Long doctorId,
                                   @RequestParam String insuranceStatus,
                                   RedirectAttributes redirectAttributes) {
        try {
            HealthInsuranceStatus status = HealthInsuranceStatus.valueOf(insuranceStatus);
            PatientRequest request = new PatientRequest(
                    firstName, lastName,firstName + " " + lastName, ssn, doctorId, status
            );
            patientService.create(request);
            redirectAttributes.addFlashAttribute("success", "Успешно регистриран пациент!");
            redirectAttributes.addFlashAttribute("activeTab", "patient"); // записваме кой таб е бил
            return "redirect:/web/setup";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            redirectAttributes.addFlashAttribute("activeTab", "patient");
            return "redirect:/web/setup";
        }
    }

    @PostMapping("/doctors/new")
    public String webCreateDoctor(@RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam String uin,
                                  @RequestParam BigDecimal examFee,
                                  @RequestParam(required = false) Set<Long> specialtyIds,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (specialtyIds == null) {
                specialtyIds = new HashSet<>();
            }
            DoctorRequest request = new DoctorRequest(
                    firstName, lastName, firstName + " " + lastName, uin, specialtyIds, examFee
            );
            doctorService.create(request);
            redirectAttributes.addFlashAttribute("success", "Успешно регистриран лекар!");
            redirectAttributes.addFlashAttribute("activeTab", "doctor");
            return "redirect:/web/setup";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            redirectAttributes.addFlashAttribute("activeTab", "doctor");
            return "redirect:/web/setup";
        }
    }

    @PostMapping("/diagnoses/new")
    public String webCreateDiagnosis(@RequestParam String name,
                                     @RequestParam String code,
                                     RedirectAttributes redirectAttributes) {
        try {
            DiagnosisRequest request = new DiagnosisRequest(name, code);
            diagnosisService.create(request);
            redirectAttributes.addFlashAttribute("success", "Успешно добавена диагноза!");
            redirectAttributes.addFlashAttribute("activeTab", "diagnosis");
            return "redirect:/web/setup";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            redirectAttributes.addFlashAttribute("activeTab", "diagnosis");
            return "redirect:/web/setup";
        }
    }

    @PostMapping("/specialties/new")
    public String webCreateSpecialty(@RequestParam String name,
                                     RedirectAttributes redirectAttributes) {
        try {
            SpecialtyRequest request = new SpecialtyRequest(name);
            specialtyService.create(request);
            redirectAttributes.addFlashAttribute("success", "Успешно добавена специалност!");
            redirectAttributes.addFlashAttribute("activeTab", "specialty");
            return "redirect:/web/setup";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            redirectAttributes.addFlashAttribute("activeTab", "specialty");
            return "redirect:/web/setup";
        }
    }
}