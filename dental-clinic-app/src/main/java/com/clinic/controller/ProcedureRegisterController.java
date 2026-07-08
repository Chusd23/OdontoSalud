package com.clinic.controller;

import com.clinic.data.DataStore;
import com.clinic.model.Patient;
import com.clinic.model.ProcedureRecord;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ProcedureRegisterController {

    @FXML private ComboBox<Patient> patientCombo;
    @FXML private TextField procedureNameField;
    @FXML private TextField toothField;
    @FXML private DatePicker dateField;
    @FXML private TextField dentistField;
    @FXML private TextField costField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextArea notesField;
    @FXML private Label statusLabel;

    @FXML private TableView<ProcedureRecord> proceduresTable;
    @FXML private TableColumn<ProcedureRecord, String> colPatient;
    @FXML private TableColumn<ProcedureRecord, String> colName;
    @FXML private TableColumn<ProcedureRecord, String> colTooth;
    @FXML private TableColumn<ProcedureRecord, String> colDate;
    @FXML private TableColumn<ProcedureRecord, String> colStatus;
    @FXML private TableColumn<ProcedureRecord, String> colCost;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    @FXML
    public void initialize() {
        patientCombo.setItems(DataStore.getInstance().getPatients());
        statusCombo.setItems(FXCollections.observableArrayList("Pendiente", "En proceso", "Realizado"));
        dateField.setValue(java.time.LocalDate.now());

        colPatient.setCellValueFactory(d -> {
            Patient p = DataStore.getInstance().getPatientById(d.getValue().getPatientId());
            return new SimpleStringProperty(p != null ? p.getFullName() : "-");
        });
        colName.setCellValueFactory(new PropertyValueFactory<>("procedureName"));
        colTooth.setCellValueFactory(new PropertyValueFactory<>("tooth"));
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDate() != null ? d.getValue().getDate().format(DATE_FMT) : "-"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCost.setCellValueFactory(d -> new SimpleStringProperty(CURRENCY.format(d.getValue().getCost())));

        proceduresTable.setItems(DataStore.getInstance().getProcedures());
    }

    @FXML
    private void handleSave() {
        if (patientCombo.getValue() == null || procedureNameField.getText().isBlank() || dateField.getValue() == null) {
            showStatus("Selecciona el paciente e indica el procedimiento y la fecha.", true);
            return;
        }

        double cost = 0;
        try {
            if (!costField.getText().isBlank()) {
                cost = Double.parseDouble(costField.getText().trim().replace(",", ""));
            }
        } catch (NumberFormatException e) {
            showStatus("El costo debe ser un valor numérico.", true);
            return;
        }

        DataStore.getInstance().addProcedure(
                patientCombo.getValue().getId(),
                procedureNameField.getText().trim(),
                toothField.getText().isBlank() ? "-" : toothField.getText().trim(),
                dateField.getValue(),
                dentistField.getText().trim(),
                notesField.getText().trim(),
                cost,
                statusCombo.getValue() != null ? statusCombo.getValue() : "Pendiente"
        );

        showStatus("Procedimiento registrado correctamente.", false);
        handleClear();
    }

    @FXML
    private void handleClear() {
        procedureNameField.clear();
        toothField.clear();
        dateField.setValue(java.time.LocalDate.now());
        dentistField.clear();
        costField.clear();
        statusCombo.setValue(null);
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
