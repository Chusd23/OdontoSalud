package com.clinic.controller;

import com.clinic.data.DataStore;
import com.clinic.model.Appointment;
import com.clinic.model.Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class AppointmentCalendarController {

    @FXML private Label monthYearLabel;
    @FXML private GridPane calendarGrid;

    @FXML private Label selectedDateLabel;
    @FXML private TableView<Appointment> dayAppointmentsTable;
    @FXML private TableColumn<Appointment, String> colTime;
    @FXML private TableColumn<Appointment, String> colPatient;
    @FXML private TableColumn<Appointment, String> colDentist;
    @FXML private TableColumn<Appointment, String> colStatus;

    @FXML private VBox detailBox;
    @FXML private Label alertBanner;
    @FXML private Label detailPatientLabel;
    @FXML private Label detailReasonLabel;
    @FXML private Label detailStatusLabel;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a");
    private static final Locale ES = new Locale("es", "ES");

    private YearMonth currentMonth = YearMonth.now();
    private LocalDate selectedDate;

    @FXML
    public void initialize() {
        colTime.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTime() != null ? d.getValue().getTime().format(TIME_FMT) : "-"));
        colPatient.setCellValueFactory(d -> {
            Patient p = DataStore.getInstance().getPatientById(d.getValue().getPatientId());
            return new SimpleStringProperty(p != null ? p.getFullName() : "-");
        });
        colDentist.setCellValueFactory(new PropertyValueFactory<>("dentist"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        dayAppointmentsTable.setRowFactory(tv -> {
            TableRow<Appointment> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty()) showAppointmentDetail(row.getItem());
            });
            return row;
        });

        buildCalendar();
    }

    @FXML
    private void handlePrevMonth() {
        currentMonth = currentMonth.minusMonths(1);
        buildCalendar();
    }

    @FXML
    private void handleNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        buildCalendar();
    }

    private void buildCalendar() {
        monthYearLabel.setText(capitalize(currentMonth.getMonth().getDisplayName(TextStyle.FULL, ES))
                + " " + currentMonth.getYear());

        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();

        for (int i = 0; i < 7; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / 7);
            calendarGrid.getColumnConstraints().add(cc);
        }
        for (int i = 0; i < 6; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            rc.setPercentHeight(100.0 / 6);
            calendarGrid.getRowConstraints().add(rc);
        }

        LocalDate firstOfMonth = currentMonth.atDay(1);
        int startCol = firstOfMonth.getDayOfWeek().getValue() - 1; // Lunes = 0
        int daysInMonth = currentMonth.lengthOfMonth();

        int row = 0, col = startCol;
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            calendarGrid.add(buildDayCell(date), col, row);
            col++;
            if (col > 6) { col = 0; row++; }
        }

        dayAppointmentsTable.getItems().clear();
        selectedDateLabel.setText("Selecciona un día en el calendario");
        selectedDate = null;
        hideDetail();
    }

    private VBox buildDayCell(LocalDate date) {
        var appts = DataStore.getInstance().getAppointmentsByDate(date);

        VBox cell = new VBox(4);
        cell.setAlignment(Pos.TOP_CENTER);
        cell.getStyleClass().add("calendar-day");
        if (date.equals(LocalDate.now())) cell.getStyleClass().add("calendar-day-today");
        if (!appts.isEmpty()) cell.getStyleClass().add("calendar-day-has-appt");

        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dayLabel.getStyleClass().add("calendar-day-number");
        cell.getChildren().add(dayLabel);

        if (!appts.isEmpty()) {
            Label badge = new Label(appts.size() + (appts.size() == 1 ? " cita" : " citas"));
            badge.getStyleClass().add("calendar-day-badge");
            cell.getChildren().add(badge);
        }

        cell.setOnMouseClicked(e -> selectDate(date, cell));
        return cell;
    }

    private void selectDate(LocalDate date, VBox cell) {
        selectedDate = date;
        for (var node : calendarGrid.getChildren()) {
            node.getStyleClass().remove("calendar-day-selected");
        }
        cell.getStyleClass().add("calendar-day-selected");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", ES);
        selectedDateLabel.setText(capitalize(date.format(fmt)));
        dayAppointmentsTable.setItems(DataStore.getInstance().getAppointmentsByDate(date));
        hideDetail();
    }

    private void showAppointmentDetail(Appointment appt) {
        Patient p = DataStore.getInstance().getPatientById(appt.getPatientId());

        detailPatientLabel.setText(p != null ? p.getFullName() : "-");
        detailReasonLabel.setText("Motivo: " + appt.getReason() + "  ·  Odontólogo: " + appt.getDentist());
        detailStatusLabel.setText("Estado: " + appt.getStatus());

        String alerts = p != null ? p.getMedicalAlerts() : null;
        if (alerts != null && !alerts.isBlank()) {
            alertBanner.setText("⚠ Alerta médica: " + alerts);
            alertBanner.setVisible(true);
            alertBanner.setManaged(true);
        } else {
            alertBanner.setVisible(false);
            alertBanner.setManaged(false);
        }

        detailBox.setVisible(true);
        detailBox.setManaged(true);
    }

    private void hideDetail() {
        detailBox.setVisible(false);
        detailBox.setManaged(false);
    }

    private String capitalize(String s) {
        if (s == null || s.isBlank()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
