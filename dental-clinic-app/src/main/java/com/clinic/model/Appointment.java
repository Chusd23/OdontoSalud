package com.clinic.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private final int id;
    private final int patientId;
    private LocalDate date;
    private LocalTime time;
    private String dentist;
    private String reason;
    private String status;
    private boolean paid;
    private double value;

    public Appointment(int id, int patientId, LocalDate date, LocalTime time,
                        String dentist, String reason, String status, double value) {
        this.id = id;
        this.patientId = patientId;
        this.date = date;
        this.time = time;
        this.dentist = dentist;
        this.reason = reason;
        this.status = status;
        this.paid = false;
        this.value = value;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getDentist() { return dentist; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
    public double getValue() { return value; }
}
