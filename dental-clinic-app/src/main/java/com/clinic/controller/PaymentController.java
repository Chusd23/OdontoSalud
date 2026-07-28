package com.clinic.controller;

import com.clinic.data.DataStore;
import com.clinic.model.Appointment;
import com.clinic.model.Patient;
import com.clinic.model.Payment;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import com.clinic.util.FieldFormatters;
public class PaymentController {

    @FXML private ComboBox<Appointment> appointmentCombo;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> methodCombo;
    @FXML private TextField conceptField;
    @FXML private Label statusLabel;

    @FXML private TableView<Payment> paymentsTable;
    @FXML private TableColumn<Payment, String> colPatient;
    @FXML private TableColumn<Payment, String> colDate;
    @FXML private TableColumn<Payment, String> colConcept;
    @FXML private TableColumn<Payment, String> colMethod;
    @FXML private TableColumn<Payment, String> colAmount;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a");
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    @FXML
    public void initialize() {
        methodCombo.setItems(FXCollections.observableArrayList("Efectivo", "Tarjeta", "Transferencia"));
        FieldFormatters.decimalOnly(amountField);
        appointmentCombo.setItems(DataStore.getInstance().getUnpaidAppointments());
        appointmentCombo.setConverter(new StringConverter<>() {

            @Override
            public String toString(Appointment a) {
                if (a == null) return "";
                Patient p = DataStore.getInstance().getPatientById(a.getPatientId());
                String name = p != null ? p.getFullName() : "Paciente desconocido";
                return name + "  ·  " + a.getDate().format(DATE_FMT) + " " + a.getTime().format(TIME_FMT)
                        + "  ·  " + a.getReason() + "  ·  " + CURRENCY.format(a.getValue());
            }
            @Override
            public Appointment fromString(String string) { return null; }
        });

        colPatient.setCellValueFactory(d -> {
            Patient p = DataStore.getInstance().getPatientById(d.getValue().getPatientId());
            return new SimpleStringProperty(p != null ? p.getFullName() : "-");
        });
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDate() != null ? d.getValue().getDate().format(DATE_FMT) : "-"));
        colConcept.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getConcept()));
        colMethod.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMethod()));
        colAmount.setCellValueFactory(d -> new SimpleStringProperty(CURRENCY.format(d.getValue().getAmount())));

        paymentsTable.setItems(DataStore.getInstance().getPayments());
    }

    @FXML
    private void handleSave() {
        Appointment appointment = appointmentCombo.getValue();
        if (appointment == null || amountField.getText().isBlank() || methodCombo.getValue() == null) {
            showStatus("Selecciona la cita, el monto y el método de pago.", true);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountField.getText().trim().replace(",", ""));
        } catch (NumberFormatException e) {
            showStatus("El monto debe ser un valor numérico.", true);
            return;
        }

        DataStore.getInstance().addPayment(
                appointment.getPatientId(),
                appointment.getId(),
                amount,
                methodCombo.getValue(),
                conceptField.getText().isBlank() ? appointment.getReason() : conceptField.getText().trim(),
                java.time.LocalDate.now()
        );

        showStatus("Cobro registrado correctamente. La cita se marcó como pagada.", false);
        handleClear();
        appointmentCombo.setItems(DataStore.getInstance().getUnpaidAppointments());
    }

    @FXML
    private void handleClear() {
        appointmentCombo.setValue(null);
        amountField.clear();
        methodCombo.setValue(null);
        conceptField.clear();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("hint-label", "error-label", "success-label");
        statusLabel.getStyleClass().add(isError ? "error-label" : "success-label");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }
}
