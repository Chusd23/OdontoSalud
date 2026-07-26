package com.clinic.controller;

import com.clinic.data.DataStore;
import com.clinic.model.Professional;
import com.clinic.util.FieldFormatters;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProfessionalRegisterController {

    @FXML private TextField namesField;
    @FXML private TextField lastNamesField;
    @FXML private ComboBox<String> docTypeCombo;
    @FXML private TextField docNumberField;
    @FXML private ComboBox<String> specialtyCombo;
    @FXML private TextField licenseField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private Label statusLabel;

    @FXML private TableView<Professional> professionalsTable;
    @FXML private TableColumn<Professional, String> colName;
    @FXML private TableColumn<Professional, String> colDoc;
    @FXML private TableColumn<Professional, String> colSpecialty;
    @FXML private TableColumn<Professional, String> colLicense;
    @FXML private TableColumn<Professional, String> colPhone;

    @FXML
    public void initialize() {
        docTypeCombo.setItems(FXCollections.observableArrayList("CC", "CE", "PA"));
        specialtyCombo.setItems(FXCollections.observableArrayList(
                "Odontología General", "Ortodoncia", "Endodoncia", "Periodoncia",
                "Cirugía Oral y Maxilofacial", "Odontopediatría", "Rehabilitación Oral"));
        FieldFormatters.numericOnly(docNumberField, 15);

        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colDoc.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDocType() + " " + data.getValue().getDocNumber()));
        colSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        colLicense.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        professionalsTable.setItems(DataStore.getInstance().getProfessionals());
    }

    @FXML
    private void handleSave() {
        if (namesField.getText().isBlank() || lastNamesField.getText().isBlank()
                || docTypeCombo.getValue() == null || docNumberField.getText().isBlank()
                || specialtyCombo.getValue() == null) {
            showStatus("Completa los campos obligatorios (*).", true);
            return;
        }

        String docNumber = docNumberField.getText().trim();
        if (DataStore.getInstance().professionalExists(docNumber)) {
            showStatus("Ya existe un profesional registrado con ese número de documento.", true);
            return;
        }

        DataStore.getInstance().addProfessional(
                namesField.getText().trim(),
                lastNamesField.getText().trim(),
                docTypeCombo.getValue(),
                docNumber,
                specialtyCombo.getValue(),
                licenseField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim()
        );

        showStatus("Profesional registrado correctamente.", false);
        handleClear();
    }

    @FXML
    private void handleClear() {
        namesField.clear();
        lastNamesField.clear();
        docTypeCombo.setValue(null);
        docNumberField.clear();
        specialtyCombo.setValue(null);
        licenseField.clear();
        phoneField.clear();
        emailField.clear();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("hint-label", "error-label", "success-label");
        statusLabel.getStyleClass().add(isError ? "error-label" : "success-label");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }
}
