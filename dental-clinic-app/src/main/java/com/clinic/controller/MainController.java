package com.clinic.controller;

import com.clinic.MainApp;
import com.clinic.data.DataStore;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML private StackPane contentArea;
    @FXML private Label userLabel;

    @FXML private Button navPatientRegister;
    @FXML private Button navHistorySearch;
    @FXML private Button navProcedures;
    @FXML private Button navReferral;
    @FXML private Button navAppointments;
    @FXML private Button navPayments;
    @FXML private Button navProfessionals;
    @FXML private Button navCalendar;
    @FXML private Button navDiagnosis;
    @FXML private Button navReports;

    private List<Button> navButtons;

    @FXML
    public void initialize() {
        navButtons = List.of(navPatientRegister, navHistorySearch, navProcedures,
                navReferral, navAppointments, navPayments, navProfessionals,
                navCalendar, navDiagnosis, navReports);

        var user = DataStore.getInstance().getCurrentUser();
        if (user != null) {
            userLabel.setText(user.getFullName() + "\n" + user.getRole());
        }

        showPatientRegister();
    }

    @FXML public void showPatientRegister() { loadView("view/patient_register.fxml", navPatientRegister); }
    @FXML public void showHistorySearch() { loadView("view/clinical_history_search.fxml", navHistorySearch); }
    @FXML public void showProcedures() { loadView("view/procedure_register.fxml", navProcedures); }
    @FXML public void showReferral() { loadView("view/specialist_referral.fxml", navReferral); }
    @FXML public void showAppointments() { loadView("view/appointment.fxml", navAppointments); }
    @FXML public void showPayments() { loadView("view/payment.fxml", navPayments); }
    @FXML public void showProfessionals() { loadView("view/professional_register.fxml", navProfessionals); }
    @FXML public void showCalendar() { loadView("view/appointment_calendar.fxml", navCalendar); }
    @FXML public void showDiagnosis() { loadView("view/diagnosis_register.fxml", navDiagnosis); }
    @FXML public void showReports() { loadView("view/reports.fxml", navReports); }

    @FXML
    public void handleLogout() {
        DataStore.getInstance().setCurrentUser(null);
        try {
            MainApp.setRoot("view/login.fxml", "OdontoSalud - Consultorio Odontológico");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath, Button activeButton) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
            setActiveButton(activeButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button active) {
        for (Button b : navButtons) {
            b.getStyleClass().remove("nav-button-active");
        }
        if (!active.getStyleClass().contains("nav-button-active")) {
            active.getStyleClass().add("nav-button-active");
        }
    }
}
