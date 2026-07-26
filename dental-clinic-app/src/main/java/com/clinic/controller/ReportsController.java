package com.clinic.controller;

import com.clinic.data.DataStore;
import com.clinic.model.Patient;
import com.clinic.model.ProcedureRecord;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportsController {

    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private Label statusLabel;

    @FXML private Label totalPatientsLabel;
    @FXML private Label totalProceduresLabel;
    @FXML private Label totalRevenueLabel;

    @FXML private TableView<Map.Entry<String, Integer>> ageRangeTable;
    @FXML private TableColumn<Map.Entry<String, Integer>, String> ageRangeCol;
    @FXML private TableColumn<Map.Entry<String, Integer>, String> ageCountCol;

    @FXML private TableView<Map.Entry<String, Integer>> treatmentsTable;
    @FXML private TableColumn<Map.Entry<String, Integer>, String> treatmentNameCol;
    @FXML private TableColumn<Map.Entry<String, Integer>, String> treatmentCountCol;

    private Map<String, Integer> lastAgeRanges = new LinkedHashMap<>();
    private Map<String, Integer> lastTreatments = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        toDatePicker.setValue(LocalDate.now());
        fromDatePicker.setValue(LocalDate.now().minusMonths(6));

        ageRangeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKey()));
        ageCountCol.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getValue())));
        treatmentNameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKey()));
        treatmentCountCol.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getValue())));

        handleGenerate();
    }

    @FXML
    private void handleGenerate() {
        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();

        if (from == null || to == null || from.isAfter(to)) {
            showStatus("Selecciona un rango de fechas válido.", true);
            resetReportData();
            return;
        }
        if (to.isBefore(from)){
        	resetReportData();
        	showStatus("La fecha final del reporte debe ser mayor que la inicial.",true);
        	return;
        
        }

        ObservableList<Patient> patients = DataStore.getInstance().getPatients();
        totalPatientsLabel.setText(String.valueOf(patients.size()));

        Map<String, Integer> ageRanges = new LinkedHashMap<>();
        ageRanges.put("0-12 años", 0);
        ageRanges.put("13-18 años", 0);
        ageRanges.put("19-30 años", 0);
        ageRanges.put("31-45 años", 0);
        ageRanges.put("46-60 años", 0);
        ageRanges.put("61+ años", 0);

        for (Patient p : patients) {
            if (p.getBirthDate() == null) continue;
            int age = Period.between(p.getBirthDate(), LocalDate.now()).getYears();
            String bucket = age <= 12 ? "0-12 años" : age <= 18 ? "13-18 años" : age <= 30 ? "19-30 años"
                    : age <= 45 ? "31-45 años" : age <= 60 ? "46-60 años" : "61+ años";
            ageRanges.merge(bucket, 1, Integer::sum);
        }
        lastAgeRanges = ageRanges;
        ageRangeTable.setItems(FXCollections.observableArrayList(ageRanges.entrySet()));

        Map<String, Integer> treatments = new LinkedHashMap<>();
        int procedureCount = 0;
        double revenue = 0;
        for (ProcedureRecord pr : DataStore.getInstance().getProcedures()) {
            if (pr.getDate() == null || pr.getDate().isBefore(from) || pr.getDate().isAfter(to)) continue;
            treatments.merge(pr.getProcedureName(), 1, Integer::sum);
            procedureCount++;
            revenue += pr.getCost();
        }
        lastTreatments = treatments;
        treatmentsTable.setItems(FXCollections.observableArrayList(treatments.entrySet()));

        totalProceduresLabel.setText(String.valueOf(procedureCount));
        totalRevenueLabel.setText(java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es", "CO"))
                .format(revenue));

        if (procedureCount == 0) {
            showStatus("No existen tratamientos registrados para el periodo seleccionado.", true);
        } else {
            showStatus("Reporte generado para el periodo " + from + " a " + to + ".", false);
        }

    }
    private void resetReportData() {
        totalPatientsLabel.setText("0");
        totalProceduresLabel.setText("0");
        totalRevenueLabel.setText(java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es", "CO")).format(0));
        lastAgeRanges = new LinkedHashMap<>();
        lastTreatments = new LinkedHashMap<>();
        ageRangeTable.setItems(FXCollections.observableArrayList());
        treatmentsTable.setItems(FXCollections.observableArrayList());
    }   

    @FXML
    private void handleDownload() {
    	if (fromDatePicker.getValue() == null || toDatePicker.getValue() == null
    	        || toDatePicker.getValue().isBefore(fromDatePicker.getValue())) {
    	    resetReportData();
    	    showStatus("La fecha final del reporte debe ser mayor que la inicial.", true);
    	    return;
    	}
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar reporte");
        chooser.setInitialFileName("reporte_odontosalud.csv");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        var file = chooser.showSaveDialog(totalPatientsLabel.getScene().getWindow());
        if (file == null) return;

        try (java.io.OutputStreamWriter fw = new java.io.OutputStreamWriter(
                new java.io.FileOutputStream(file), java.nio.charset.StandardCharsets.UTF_8)) {
            fw.write('\uFEFF'); // BOM para que Excel detecte UTF-8 correctamente
            fw.write("Reporte OdontoSalud\n");
            fw.write("Periodo," + fromDatePicker.getValue() + " a " + toDatePicker.getValue() + "\n\n");
            fw.write("Total pacientes," + totalPatientsLabel.getText() + "\n");
            fw.write("Total tratamientos en periodo," + totalProceduresLabel.getText() + "\n");
            fw.write("Ingresos en periodo," + totalRevenueLabel.getText() + "\n\n");
            fw.write("Rango de edad,Cantidad\n");
            for (var e : lastAgeRanges.entrySet()) fw.write(e.getKey() + "," + e.getValue() + "\n");
            fw.write("\nTratamiento,Cantidad\n");
            for (var e : lastTreatments.entrySet()) fw.write(e.getKey() + "," + e.getValue() + "\n");
            showStatus("Reporte descargado en " + file.getAbsolutePath(), false);
        } catch (IOException e) {
            showStatus("No se pudo guardar el archivo: " + e.getMessage(), true);
        }
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("hint-label", "error-label", "success-label");
        statusLabel.getStyleClass().add(isError ? "error-label" : "success-label");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }
}
