package com.clinic.model;

import java.time.LocalDateTime;

public class ChangeLogEntry {
    private final int id;
    private final int patientId;
    private final String field;
    private final String oldValue;
    private final String newValue;
    private final String changedBy;
    private final LocalDateTime timestamp;

    public ChangeLogEntry(int id, int patientId, String field, String oldValue, String newValue, String changedBy) {
        this.id = id;
        this.patientId = patientId;
        this.field = field;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedBy = changedBy;
        this.timestamp = LocalDateTime.now();
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getField() { return field; }
    public String getOldValue() { return oldValue; }
    public String getNewValue() { return newValue; }
    public String getChangedBy() { return changedBy; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
