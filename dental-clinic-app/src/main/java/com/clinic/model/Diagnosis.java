package com.clinic.model;

import java.time.LocalDate;

public class Diagnosis {
    private final int id;
    private final int patientId;
    private String pathology;
    private String tooth;
    private String severity;
    private LocalDate date;
    private String dentist;
    private String notes;

    public Diagnosis(int id, int patientId, String pathology, String tooth, String severity,
                      LocalDate date, String dentist, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.pathology = pathology;
        this.tooth = tooth;
        this.severity = severity;
        this.date = date;
        this.dentist = dentist;
        this.notes = notes;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPathology() { return pathology; }
    public String getTooth() { return tooth; }
    public String getSeverity() { return severity; }
    public LocalDate getDate() { return date; }
    public String getDentist() { return dentist; }
    public String getNotes() { return notes; }
}
