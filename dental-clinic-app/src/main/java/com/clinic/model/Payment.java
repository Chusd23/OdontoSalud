package com.clinic.model;

import java.time.LocalDate;

public class Payment {
    private final int id;
    private final int patientId;
    private final int appointmentId;
    private double amount;
    private String method;
    private String concept;
    private LocalDate date;

    public Payment(int id, int patientId, int appointmentId, double amount,
                    String method, String concept, LocalDate date) {
        this.id = id;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.method = method;
        this.concept = concept;
        this.date = date;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getAppointmentId() { return appointmentId; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getConcept() { return concept; }
    public LocalDate getDate() { return date; }
}
