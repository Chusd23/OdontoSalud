package com.clinic.controller;

import com.clinic.data.DataStore;
import com.clinic.model.Appointment;
import com.clinic.model.Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.clinic.util.FieldFormatters;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AppointmentController {

    @FXML private ComboBox<Patient> patientCombo;
    @FXML private DatePicker dateField;
    @FXML private TextField timeField;
    @FXML private TextField dentistField;
    @FXML private TextField reasonField;
    @FXML private Label statusLabel;
    @FXML private TextField valueField;

    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> colPatient;
    @FXML private TableColumn<Appointment, String> colDate;
    @FXML private TableColumn<Appointment, String> colTime;
    @FXML private TableColumn<Appointment, String> colDentist;
    @FXML private TableColumn<Appointment, String> colReason;
    @FXML private TableColumn<Appointment, String> colStatus;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FMT_12H = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter TIME_FMT_24H = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        patientCombo.setItems(DataStore.getInstance().getPatients());
        dateField.setValue(java.time.LocalDate.now());
        FieldFormatters.decimalOnly(valueField);

        colPatient.setCellValueFactory(d -> {
            Patient p = DataStore.getInstance().getPatientById(d.getValue().getPatientId());
            return new SimpleStringProperty(p != null ? p.getFullName() : "-");
        });
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDate() != null ? d.getValue().getDate().format(DATE_FMT) : "-"));
        colTime.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTime() != null ? d.getValue().getTime().format(TIME_FMT_12H) : "-"));
        colDentist.setCellValueFactory(new PropertyValueFactory<>("dentist"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        appointmentsTable.setItems(DataStore.getInstance().getAppointments());
    }

    @FXML
    private void handleSave() {
        if (patientCombo.getValue() == null || dateField.getValue() == null || timeField.getText().isBlank()) {
            showStatus("Selecciona el paciente, la fecha y la hora.", true);
            return;
        }

        LocalTime time;
        try {
            time = LocalTime.parse(timeField.getText().trim(), TIME_FMT_24H);
        } catch (DateTimeParseException e) {
            showStatus("La hora debe tener el formato HH:mm, ej: 14:30.", true);
            return;
        }
        double value;
        try {
            value = valueField.getText().isBlank() ? 0 : Double.parseDouble(valueField.getText().trim());
        } catch (NumberFormatException e) {
            showStatus("El valor de la cita debe ser numérico.", true);
            return;
        }

        DataStore.getInstance().addAppointment(
                patientCombo.getValue().getId(),
                dateField.getValue(),
                time,
                dentistField.getText().trim(),
                reasonField.getText().trim(),
                "Programada",
                value
        );

        showStatus("Cita agendada correctamente.", false);
        handleClear();
    }

    @FXML
    private void handleClear() {
        dateField.setValue(java.time.LocalDate.now());
        timeField.clear();
        dentistField.clear();
        reasonField.clear();
        valueField.clear();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("hint-label", "error-label", "success-label");
        statusLabel.getStyleClass().add(isError ? "error-label" : "success-label");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }
}
