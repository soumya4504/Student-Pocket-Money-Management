package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StudentDataLookup extends Application {
    private static final String CSV_FILE = System.getProperty("user.home") + File.separator + "students_data.csv";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Pocket Money Manager - Advanced Lookup");

        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(25));
        mainContainer.getStyleClass().add("root");

        // Header
        VBox header = new VBox(4);
        Label title = new Label("Student Record Inspector");
        title.getStyleClass().add("header-title");
        Label subtitle = new Label("Search and review student financial profile and AI assessment");
        subtitle.getStyleClass().add("header-subtitle");
        header.getChildren().addAll(title, subtitle);

        // Search Card
        HBox searchCard = new HBox(12);
        searchCard.getStyleClass().add("card");
        searchCard.setAlignment(Pos.CENTER_LEFT);

        Label studentIdLabel = new Label("Student ID:");
        studentIdLabel.getStyleClass().add("form-label");
        TextField studentIdField = new TextField();
        studentIdField.setPromptText("e.g. STU1001 or STU-101");
        studentIdField.setPrefWidth(220);

        Button lookupButton = new Button("🔍 Lookup Student");
        lookupButton.getStyleClass().add("btn-primary");

        Button openCsvBtn = new Button("📂 Open CSV File");
        openCsvBtn.getStyleClass().add("btn-secondary");

        searchCard.getChildren().addAll(studentIdLabel, studentIdField, lookupButton, openCsvBtn);

        // Results Display Card
        VBox resultCard = new VBox(10);
        resultCard.getStyleClass().add("card");
        VBox.setVgrow(resultCard, Priority.ALWAYS);

        Label resultTitle = new Label("Student Financial Dossier");
        resultTitle.getStyleClass().add("section-title");

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(380);
        resultArea.setWrapText(true);
        resultArea.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 13px;");
        resultArea.setText("Enter a Student ID above and click 'Lookup Student' to inspect their full record.");

        resultCard.getChildren().addAll(resultTitle, resultArea);

        mainContainer.getChildren().addAll(header, searchCard, resultCard);

        lookupButton.setOnAction(e -> {
            String studentId = studentIdField.getText().trim();
            if (studentId.isEmpty()) {
                showAlert("Validation Error", "Please enter a student ID to search.");
                return;
            }
            try {
                String result = findStudentRecord(studentId);
                if (result == null) {
                    showAlert("Not Found", "No record found for Student ID: " + studentId);
                    resultArea.setText("⚠️ No record found for Student ID: " + studentId + "\n\nPlease check the ID or open the CSV file to see all existing records.");
                } else {
                    resultArea.setText(result);
                }
            } catch (IOException ex) {
                showAlert("Error", "Failed to read the CSV file: " + ex.getMessage());
            }
        });

        openCsvBtn.setOnAction(e -> {
            File csv = new File(CSV_FILE);
            if (!csv.exists()) {
                showAlert("File Not Found", "No CSV file exists yet.");
                return;
            }
            try {
                getHostServices().showDocument(csv.toURI().toString());
            } catch (Exception ex) {
                showAlert("Info", "CSV file path: " + csv.getAbsolutePath());
            }
        });

        Scene scene = new Scene(mainContainer, 720, 600);
        try {
            File css = new File("style.css");
            if (css.exists()) scene.getStylesheets().add(css.toURI().toString());
        } catch (Exception ignored) {}

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String findStudentRecord(String studentId) throws IOException {
        File file = new File(CSV_FILE);
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String header = br.readLine();
            if (header == null) return null;
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equalsIgnoreCase(studentId)) {
                    return formatRecord(header, parts);
                }
            }
        }
        return null;
    }

    private String formatRecord(String headerLine, String[] dataParts) {
        String[] headers = headerLine.split(",");
        StringBuilder sb = new StringBuilder();
        sb.append("=====================================================\n");
        sb.append("         STUDENT FINANCIAL PROFILE & AUDIT           \n");
        sb.append("=====================================================\n\n");
        for (int i = 0; i < headers.length && i < dataParts.length; i++) {
            String key = headers[i].trim();
            String val = dataParts[i].trim();
            sb.append(String.format("%-24s : %s\n", key, val));
        }
        sb.append("\n=====================================================\n");
        sb.append("STATUS: Verified record in local database\n");
        return sb.toString();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
