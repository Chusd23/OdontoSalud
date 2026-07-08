package com.clinic.controller;

import com.clinic.data.DataStore;
import com.clinic.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ClinicalHistorySearchController {

    @FXML private TextField searchField;

    @FXML private TableView<Patient> resultsTable;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, String> colDoc;
    @FXML private TableColumn<Patient, String> colPhone;

    @FXML private VBox detailBox;
    @FXML private Label patientHeaderLabel;
    @FXML private Label patientSubHeaderLabel;

    @FXML private TableView<ProcedureRecord> proceduresTable;
    @FXML private TableColumn<ProcedureRecord, String> pColName;
    @FXML private TableColumn<ProcedureRecord, String> pColTooth;
    @FXML private TableColumn<ProcedureRecord, String> pColDate;
    @FXML private TableColumn<ProcedureRecord, String> pColDentist;
    @FXML private TableColumn<ProcedureRecord, String> pColStatus;
    @FXML private TableColumn<ProcedureRecord, String> pColCost;

    @FXML private TableView<SpecialistReferral> referralsTable;
    @FXML private TableColumn<SpecialistReferral, String> rColSpecialty;
    @FXML private TableColumn<SpecialistReferral, String> rColSpecialist;
    @FXML private TableColumn<SpecialistReferral, String> rColReason;
    @FXML private TableColumn<SpecialistReferral, String> rColDate;
    @FXML private TableColumn<SpecialistReferral, String> rColStatus;

    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> aColDate;
    @FXML private TableColumn<Appointment, String> aColTime;
    @FXML private TableColumn<Appointment, String> aColDentist;
    @FXML private TableColumn<Appointment, String> aColReason;
    @FXML private TableColumn<Appointment, String> aColStatus;

    @FXML private TableView<Payment> paymentsTable;
    @FXML private TableColumn<Payment, String> payColDate;
    @FXML private TableColumn<Payment, String> payColConcept;
    @FXML private TableColumn<Payment, String> payColMethod;
    @FXML private TableColumn<Payment, String> payColAmount;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a");
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colDoc.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDocType() + " " + d.getValue().getDocNumber()));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        pColName.setCellValueFactory(new PropertyValueFactory<>("procedureName"));
        pColTooth.setCellValueFactory(new PropertyValueFactory<>("tooth"));
        pColDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDate() != null ? d.getValue().getDate().format(DATE_FMT) : "-"));
        pColDentist.setCellValueFactory(new PropertyValueFactory<>("dentist"));
        pColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        pColCost.setCellValueFactory(d -> new SimpleStringProperty(CURRENCY.format(d.getValue().getCost())));

        rColSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        rColSpecialist.setCellValueFactory(new PropertyValueFactory<>("specialistName"));
        rColReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        rColDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDate() != null ? d.getValue().getDate().format(DATE_FMT) : "-"));
        rColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        aColDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDate() != null ? d.getValue().getDate().format(DATE_FMT) : "-"));
        aColTime.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTime() != null ? d.getValue().getTime().format(TIME_FMT) : "-"));
        aColDentist.setCellValueFactory(new PropertyValueFactory<>("dentist"));
        aColReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        aColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        payColDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDate() != null ? d.getValue().getDate().format(DATE_FMT) : "-"));
        payColConcept.setCellValueFactory(new PropertyValueFactory<>("concept"));
        payColMethod.setCellValueFactory(new PropertyValueFactory<>("method"));
        payColAmount.setCellValueFactory(d -> new SimpleStringProperty(CURRENCY.format(d.getValue().getAmount())));

        resultsTable.setItems(DataStore.getInstance().getPatients());

        resultsTable.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<>();
            row.setOnMouseClicked((MouseEvent event) -> {
                if (!row.isEmpty()) {
                    showPatientHistory(row.getItem());
                }
            });
            return row;
        });
    }

    @FXML
    private void handleSearch() {
        resultsTable.setItems(DataStore.getInstance().searchPatients(searchField.getText()));
        detailBox.setVisible(false);
        detailBox.setManaged(false);
    }

    private void showPatientHistory(Patient patient) {
        patientHeaderLabel.setText(patient.getFullName());
        String age = patient.getBirthDate() != null
                ? java.time.Period.between(patient.getBirthDate(), java.time.LocalDate.now()).getYears() + " años"
                : "Edad no registrada";
        patientSubHeaderLabel.setText(patient.getDocType() + " " + patient.getDocNumber()
                + "  ·  " + age
                + "  ·  Grupo sanguíneo: " + (patient.getBloodType() != null ? patient.getBloodType() : "N/A")
                + "  ·  Alergias: " + (patient.getAllergies() == null || patient.getAllergies().isBlank()
                    ? "Ninguna reportada" : patient.getAllergies()));

        proceduresTable.setItems(DataStore.getInstance().getProceduresForPatient(patient.getId()));
        referralsTable.setItems(DataStore.getInstance().getReferralsForPatient(patient.getId()));
        appointmentsTable.setItems(DataStore.getInstance().getAppointmentsForPatient(patient.getId()));
        paymentsTable.setItems(DataStore.getInstance().getPaymentsForPatient(patient.getId()));

        detailBox.setVisible(true);
        detailBox.setManaged(true);
    }
}
