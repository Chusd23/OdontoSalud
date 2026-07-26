package com.clinic.controller;

import com.clinic.data.DataStore;
import com.clinic.model.Diagnosis;
import com.clinic.model.Patient;
import com.clinic.util.FieldFormatters;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DiagnosisController {

    @FXML private ComboBox<Patient> patientCombo;
    @FXML private ComboBox<String> pathologyCombo;
    @FXML private TextField toothField;
    @FXML private ComboBox<String> severityCombo;
    @FXML private DatePicker dateField;
    @FXML private TextField dentistField;
    @FXML private TextArea notesField;
    @FXML private Label statusLabel;

    @FXML private TableView<Diagnosis> diagnosesTable;
    @FXML private TableColumn<Diagnosis, String> colPatient;
    @FXML private TableColumn<Diagnosis, String> colPathology;
    @FXML private TableColumn<Diagnosis, String> colTooth;
    @FXML private TableColumn<Diagnosis, String> colSeverity;
    @FXML private TableColumn<Diagnosis, String> colDate;
    @FXML private TableColumn<Diagnosis, String> colDentist;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        patientCombo.setItems(DataStore.getInstance().getPatients());
        pathologyCombo.setItems(FXCollections.observableArrayList(
                "Caries dental", "Gingivitis", "Periodontitis", "Pulpitis reversible",
                "Pulpitis irreversible", "Absceso dental", "Maloclusión", "Bruxismo",
                "Fisura dental", "Necrosis pulpar", "Otro"));
        severityCombo.setItems(FXCollections.observableArrayList("Leve", "Moderada", "Severa"));
        dateField.setValue(LocalDate.now());
        FieldFormatters.numericOnly(toothField, 2);

        colPatient.setCellValueFactory(d -> {
            Patient p = DataStore.getInstance().getPatientById(d.getValue().getPatientId());
            return new SimpleStringProperty(p != null ? p.getFullName() : "-");
        });
        colPathology.setCellValueFactory(new PropertyValueFactory<>("pathology"));
        colTooth.setCellValueFactory(new PropertyValueFactory<>("tooth"));
        colSeverity.setCellValueFactory(new PropertyValueFactory<>("severity"));
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDate() != null ? d.getValue().getDate().format(DATE_FMT) : "-"));
        colDentist.setCellValueFactory(new PropertyValueFactory<>("dentist"));

        diagnosesTable.setItems(DataStore.getInstance().getDiagnoses());
    }

    @FXML
    private void handleSave() {
        if (patientCombo.getValue() == null || pathologyCombo.getValue() == null
                || severityCombo.getValue() == null || dateField.getValue() == null
                || dentistField.getText().isBlank()) {
            showStatus("Completa los campos obligatorios (*).", true);
            return;
        }

        DataStore.getInstance().addDiagnosis(
                patientCombo.getValue().getId(),
                pathologyCombo.getValue(),
                toothField.getText().trim(),
                severityCombo.getValue(),
                dateField.getValue(),
                dentistField.getText().trim(),
                notesField.getText().trim()
        );

        showStatus("Diagnóstico guardado y asociado a la historia clínica del paciente.", false);
        handleClear();
    }

    @FXML
    private void handleClear() {
        patientCombo.setValue(null);
        pathologyCombo.setValue(null);
        toothField.clear();
        severityCombo.setValue(null);
        dateField.setValue(LocalDate.now());
        dentistField.clear();
        notesField.clear();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("hint-label", "error-label", "success-label");
        statusLabel.getStyleClass().add(isError ? "error-label" : "success-label");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }
}
