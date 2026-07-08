package com.clinic.controller;

import com.clinic.MainApp;
import com.clinic.data.DataStore;
import com.clinic.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = DataStore.getInstance().authenticate(username, password);
        if (user == null) {
            showError("Usuario o contraseña incorrectos.");
            return;
        }

        DataStore.getInstance().setCurrentUser(user);
        try {
            MainApp.setRoot("view/main.fxml", "OdontoSalud - " + user.getFullName());
        } catch (IOException e) {
            showError("No se pudo cargar la aplicación.");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
