package com.clinic.controller;

import com.clinic.data.DataStore;
import com.clinic.model.Patient;
import com.clinic.model.SpecialistReferral;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;

public class SpecialistReferralController {

    @FXML private ComboBox<Patient> patientCombo;
    @FXML private ComboBox<String> specialtyCombo;
    @FXML private TextField specialistNameField;
    @FXML private DatePicker dateField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextArea reasonField;
    @FXML private Label statusLabel;

    @FXML private TableView<SpecialistReferral> referralsTable;
    @FXML private TableColumn<SpecialistReferral, String> colPatient;
    @FXML private TableColumn<SpecialistReferral, String> colSpecialty;
    @FXML private TableColumn<SpecialistReferral, String> colSpecialist;
    @FXML private TableColumn<SpecialistReferral, String> colDate;
    @FXML private TableColumn<SpecialistReferral, String> colStatus;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        patientCombo.setItems(DataStore.getInstance().getPatients());
        specialtyCombo.setItems(FXCollections.observableArrayList(
                "Ortodoncia", "Endodoncia", "Periodoncia", "Cirugía Oral y Maxilofacial",
                "Odontopediatría", "Rehabilitación Oral", "Radiología Oral"));
        statusCombo.setItems(FXCollections.observableArrayList("Pendiente", "Atendida", "Cancelada"));
        dateField.setValue(java.time.LocalDate.now());

        colPatient.setCellValueFactory(d -> {
            Patient p = DataStore.getInstance().getPatientById(d.getValue().getPatientId());
            return new SimpleStringProperty(p != null ? p.getFullName() : "-");
        });
        colSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        colSpecialist.setCellValueFactory(new PropertyValueFactory<>("specialistName"));
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDate() != null ? d.getValue().getDate().format(DATE_FMT) : "-"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        referralsTable.setItems(DataStore.getInstance().getReferrals());
    }

    @FXML
    private void handleSave() {
        if (patientCombo.getValue() == null || specialtyCombo.getValue() == null || dateField.getValue() == null) {
            showStatus("Selecciona el paciente, la especialidad y la fecha.", true);
            return;
        }

        DataStore.getInstance().addReferral(
                patientCombo.getValue().getId(),
                specialtyCombo.getValue(),
                specialistNameField.getText().isBlank() ? "Por asignar" : specialistNameField.getText().trim(),
                reasonField.getText().trim(),
                dateField.getValue(),
                statusCombo.getValue() != null ? statusCombo.getValue() : "Pendiente"
        );

        showStatus("Solicitud enviada correctamente.", false);
        handleClear();
    }

    @FXML
    private void handleClear() {
        specialtyCombo.setValue(null);
        specialistNameField.clear();
        dateField.setValue(java.time.LocalDate.now());
        statusCombo.setValue(null);
        reasonField.clear();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("hint-label", "error-label", "success-label");
        statusLabel.getStyleClass().add(isError ? "error-label" : "success-label");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }
}
