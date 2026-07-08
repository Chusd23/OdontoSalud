package com.clinic.model;

import java.time.LocalDate;

public class ProcedureRecord {
    private final int id;
    private final int patientId;
    private String procedureName;
    private String tooth;
    private LocalDate date;
    private String dentist;
    private String notes;
    private double cost;
    private String status;

    public ProcedureRecord(int id, int patientId, String procedureName, String tooth,
                            LocalDate date, String dentist, String notes, double cost, String status) {
        this.id = id;
        this.patientId = patientId;
        this.procedureName = procedureName;
        this.tooth = tooth;
        this.date = date;
        this.dentist = dentist;
        this.notes = notes;
        this.cost = cost;
        this.status = status;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getProcedureName() { return procedureName; }
    public String getTooth() { return tooth; }
    public LocalDate getDate() { return date; }
    public String getDentist() { return dentist; }
    public String getNotes() { return notes; }
    public double getCost() { return cost; }
    public String getStatus() { return status; }
}
