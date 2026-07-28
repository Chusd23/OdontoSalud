package com.clinic.controller;

import com.clinic.data.DataStore;
import com.clinic.model.Patient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.clinic.util.FieldFormatters;
import java.time.format.DateTimeFormatter;

public class PatientRegisterController {

    @FXML private TextField namesField;
    @FXML private TextField lastNamesField;
    @FXML private ComboBox<String> docTypeCombo;
    @FXML private TextField docNumberField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<String> genderCombo;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private ComboBox<String> bloodTypeCombo;
    @FXML private TextArea allergiesField;
    @FXML private TextArea medicalAlertsField;
    @FXML private Label statusLabel;

    @FXML private TableView<Patient> patientsTable;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, String> colDoc;
    @FXML private TableColumn<Patient, String> colPhone;
    @FXML private TableColumn<Patient, String> colEmail;
    @FXML private TableColumn<Patient, String> colRegDate;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        docTypeCombo.setItems(FXCollections.observableArrayList("CC", "TI", "CE", "PA", "RC"));
        genderCombo.setItems(FXCollections.observableArrayList("Masculino", "Femenino", "Otro"));
        bloodTypeCombo.setItems(FXCollections.observableArrayList(
                "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"));
        FieldFormatters.numericOnly(docNumberField, 15);
        FieldFormatters.numericOnly(phoneField, 10);

        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colDoc.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDocType() + " " + data.getValue().getDocNumber()));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRegDate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getRegistrationDate().format(FMT)));

        patientsTable.setItems(DataStore.getInstance().getPatients());
    }

    @FXML
    private void handleSave() {
        if (namesField.getText().isBlank() || lastNamesField.getText().isBlank()
                || docTypeCombo.getValue() == null || docNumberField.getText().isBlank()) {
            showStatus("Completa los campos obligatorios (*).", true);
            return;
        }

        DataStore.getInstance().addPatient(
                namesField.getText().trim(),
                lastNamesField.getText().trim(),
                docTypeCombo.getValue(),
                docNumberField.getText().trim(),
                birthDatePicker.getValue(),
                genderCombo.getValue(),
                phoneField.getText().trim(),
                emailField.getText().trim(),
                addressField.getText().trim(),
                bloodTypeCombo.getValue(),
                allergiesField.getText().trim(),
                medicalAlertsField.getText().trim()
        );

        showStatus("Paciente registrado correctamente.", false);
        handleClear();
    }

    @FXML
    private void handleClear() {
        namesField.clear();
        lastNamesField.clear();
        docTypeCombo.setValue(null);
        docNumberField.clear();
        birthDatePicker.setValue(null);
        genderCombo.setValue(null);
        phoneField.clear();
        emailField.clear();
        addressField.clear();
        bloodTypeCombo.setValue(null);
        allergiesField.clear();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("hint-label", "error-label", "success-label");
        statusLabel.getStyleClass().add(isError ? "error-label" : "success-label");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }
}
