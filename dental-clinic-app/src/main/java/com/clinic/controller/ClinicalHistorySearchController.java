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
import java.util.Objects;

public class ClinicalHistorySearchController {

    @FXML private TextField searchField;

    @FXML private TableView<Patient> resultsTable;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, String> colDoc;
    @FXML private TableColumn<Patient, String> colPhone;

    @FXML private VBox detailBox;
    @FXML private Label alertBanner;
    @FXML private Label patientHeaderLabel;
    @FXML private Label patientSubHeaderLabel;
    @FXML private Button editButton;

    @FXML private VBox editBox;
    @FXML private TextField editPhoneField;
    @FXML private TextField editEmailField;
    @FXML private TextField editAddressField;
    @FXML private ComboBox<String> editBloodTypeCombo;
    @FXML private TextArea editAllergiesField;
    @FXML private TextArea editMedicalAlertsField;
    @FXML private Label editStatusLabel;

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

    @FXML private TableView<Diagnosis> diagnosesTable;
    @FXML private TableColumn<Diagnosis, String> dColPathology;
    @FXML private TableColumn<Diagnosis, String> dColTooth;
    @FXML private TableColumn<Diagnosis, String> dColSeverity;
    @FXML private TableColumn<Diagnosis, String> dColDate;
    @FXML private TableColumn<Diagnosis, String> dColDentist;

    @FXML private VBox changeLogAllowedBox;
    @FXML private VBox changeLogDeniedBox;
    @FXML private TableView<ChangeLogEntry> changeLogTable;
    @FXML private TableColumn<ChangeLogEntry, String> cColDate;
    @FXML private TableColumn<ChangeLogEntry, String> cColUser;
    @FXML private TableColumn<ChangeLogEntry, String> cColField;
    @FXML private TableColumn<ChangeLogEntry, String> cColOld;
    @FXML private TableColumn<ChangeLogEntry, String> cColNew;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    private Patient currentPatient;

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

        dColPathology.setCellValueFactory(new PropertyValueFactory<>("pathology"));
        dColTooth.setCellValueFactory(new PropertyValueFactory<>("tooth"));
        dColSeverity.setCellValueFactory(new PropertyValueFactory<>("severity"));
        dColDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDate() != null ? d.getValue().getDate().format(DATE_FMT) : "-"));
        dColDentist.setCellValueFactory(new PropertyValueFactory<>("dentist"));

        cColDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTimestamp() != null ? d.getValue().getTimestamp().format(DATETIME_FMT) : "-"));
        cColUser.setCellValueFactory(new PropertyValueFactory<>("changedBy"));
        cColField.setCellValueFactory(new PropertyValueFactory<>("field"));
        cColOld.setCellValueFactory(new PropertyValueFactory<>("oldValue"));
        cColNew.setCellValueFactory(new PropertyValueFactory<>("newValue"));

        editBloodTypeCombo.setItems(javafx.collections.FXCollections.observableArrayList(
                "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"));

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
        currentPatient = patient;
        hideEditForm();

        patientHeaderLabel.setText(patient.getFullName());
        String age = patient.getBirthDate() != null
                ? java.time.Period.between(patient.getBirthDate(), java.time.LocalDate.now()).getYears() + " años"
                : "Edad no registrada";
        patientSubHeaderLabel.setText(patient.getDocType() + " " + patient.getDocNumber()
                + "  ·  " + age
                + "  ·  Grupo sanguíneo: " + (patient.getBloodType() != null ? patient.getBloodType() : "N/A")
                + "  ·  Alergias: " + (patient.getAllergies() == null || patient.getAllergies().isBlank()
                    ? "Ninguna reportada" : patient.getAllergies()));

        // HU09: alerta médica visible de forma prioritaria antes que cualquier otro dato.
        if (patient.getMedicalAlerts() != null && !patient.getMedicalAlerts().isBlank()) {
            alertBanner.setText("⚠ ALERTA MÉDICA: " + patient.getMedicalAlerts());
            alertBanner.setVisible(true);
            alertBanner.setManaged(true);
        } else {
            alertBanner.setVisible(false);
            alertBanner.setManaged(false);
        }

        proceduresTable.setItems(DataStore.getInstance().getProceduresForPatient(patient.getId()));
        referralsTable.setItems(DataStore.getInstance().getReferralsForPatient(patient.getId()));
        appointmentsTable.setItems(DataStore.getInstance().getAppointmentsForPatient(patient.getId()));
        paymentsTable.setItems(DataStore.getInstance().getPaymentsForPatient(patient.getId()));
        diagnosesTable.setItems(DataStore.getInstance().getDiagnosesForPatient(patient.getId()));

        // HU12 escenario 3: restringe la consulta del historial de cambios a roles autorizados.
        User user = DataStore.getInstance().getCurrentUser();
        boolean canViewChangeLog = user != null && !"Recepción".equalsIgnoreCase(user.getRole());
        changeLogAllowedBox.setVisible(canViewChangeLog);
        changeLogAllowedBox.setManaged(canViewChangeLog);
        changeLogDeniedBox.setVisible(!canViewChangeLog);
        changeLogDeniedBox.setManaged(!canViewChangeLog);
        if (canViewChangeLog) {
            changeLogTable.setItems(DataStore.getInstance().getChangeLogForPatient(patient.getId()));
        }

        detailBox.setVisible(true);
        detailBox.setManaged(true);
    }

    @FXML
    private void handleToggleEdit() {
        if (currentPatient == null) return;
        editPhoneField.setText(currentPatient.getPhone());
        editEmailField.setText(currentPatient.getEmail());
        editAddressField.setText(currentPatient.getAddress());
        editBloodTypeCombo.setValue(currentPatient.getBloodType());
        editAllergiesField.setText(currentPatient.getAllergies());
        editMedicalAlertsField.setText(currentPatient.getMedicalAlerts());

        editStatusLabel.setVisible(false);
        editStatusLabel.setManaged(false);
        editBox.setVisible(true);
        editBox.setManaged(true);
    }

    @FXML
    private void handleCancelEdit() {
        hideEditForm();
    }

    @FXML
    private void handleSaveEdit() {
        if (currentPatient == null) return;

        logIfChanged("Teléfono", currentPatient.getPhone(), editPhoneField.getText().trim());
        logIfChanged("Correo", currentPatient.getEmail(), editEmailField.getText().trim());
        logIfChanged("Dirección", currentPatient.getAddress(), editAddressField.getText().trim());
        logIfChanged("Grupo sanguíneo", currentPatient.getBloodType(), editBloodTypeCombo.getValue());
        logIfChanged("Alergias", currentPatient.getAllergies(), editAllergiesField.getText().trim());
        logIfChanged("Alertas médicas", currentPatient.getMedicalAlerts(), editMedicalAlertsField.getText().trim());

        currentPatient.setPhone(editPhoneField.getText().trim());
        currentPatient.setEmail(editEmailField.getText().trim());
        currentPatient.setAddress(editAddressField.getText().trim());
        currentPatient.setBloodType(editBloodTypeCombo.getValue());
        currentPatient.setAllergies(editAllergiesField.getText().trim());
        currentPatient.setMedicalAlerts(editMedicalAlertsField.getText().trim());

        resultsTable.refresh();
        showPatientHistory(currentPatient);
        hideEditForm();
    }

    private void logIfChanged(String field, String oldValue, String newValue) {
        String o = oldValue == null ? "" : oldValue;
        String n = newValue == null ? "" : newValue;
        if (!Objects.equals(o, n)) {
            DataStore.getInstance().logPatientChange(currentPatient.getId(), field, o, n);
        }
    }

    private void hideEditForm() {
        editBox.setVisible(false);
        editBox.setManaged(false);
    }
}
