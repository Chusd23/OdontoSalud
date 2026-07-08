package com.clinic.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public final class FieldFormatters {

    private FieldFormatters() {}

    public static void numericOnly(TextField field) {
        numericOnly(field, Integer.MAX_VALUE);
    }

    public static void numericOnly(TextField field, int maxLength) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }
            if (newText.matches("\\d{0," + maxLength + "}")) {
                return change;
            }
            return null;
        });
        field.setTextFormatter(formatter);
    }
}
