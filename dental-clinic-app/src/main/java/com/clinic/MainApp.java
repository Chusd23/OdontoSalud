package com.clinic;
import javafx.scene.image.Image;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Tema moderno estilo Primer (GitHub) claro, con acentos suaves
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        Parent root = loadFxml("view/login.fxml");
        Scene scene = new Scene(root, 1000, 650);
        scene.getStylesheets().add(Objects.requireNonNull(
                MainApp.class.getResource("css/styles.css")).toExternalForm());

        stage.setScene(scene);
        stage.setTitle("SonrisaClinic - Consultorio Odontológico");
        stage.getIcons().add(new Image(Objects.requireNonNull(
                MainApp.class.getResourceAsStream("images/icono.JPG"))));
        stage.setMinWidth(960);
        stage.setMinHeight(600);
        stage.show();
    }

    /** Carga un FXML relativo al paquete com.clinic.resources y devuelve la raíz. */
    public static Parent loadFxml(String relativePath) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(relativePath));
        return loader.load();
    }

    /** Cambia la escena completa de la ventana principal (usado para login -> app). */
    public static void setRoot(String relativePath, String title) throws IOException {
        Parent root = loadFxml(relativePath);
        Scene scene = new Scene(root, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        scene.getStylesheets().add(Objects.requireNonNull(
                MainApp.class.getResource("css/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        if (title != null) primaryStage.setTitle(title);
    }

    public static Stage getPrimaryStage() { return primaryStage; }

    public static void main(String[] args) {
        launch(args);
    }
}
