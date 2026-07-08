package com.clinic.model;

import java.time.LocalDate;

public class SpecialistReferral {
    private final int id;
    private final int patientId;
    private String specialty;
    private String specialistName;
    private String reason;
    private LocalDate date;
    private String status;

    public SpecialistReferral(int id, int patientId, String specialty, String specialistName,
                               String reason, LocalDate date, String status) {
        this.id = id;
        this.patientId = patientId;
        this.specialty = specialty;
        this.specialistName = specialistName;
        this.reason = reason;
        this.date = date;
        this.status = status;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getSpecialty() { return specialty; }
    public String getSpecialistName() { return specialistName; }
    public String getReason() { return reason; }
    public LocalDate getDate() { return date; }
    public String getStatus() { return status; }
}
