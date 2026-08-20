package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class StudentPocketMoneyManagerSave extends Application {
    private static final String CSV_FILE = System.getProperty("user.home") + File.separator + "students_data.csv";

    private Label fileLinkLabel = new Label();
    private Label savingBehaviorLabel = new Label("-");
    private Label financialStressLabel = new Label("-");
    private Label spendingTypeLabel = new Label("-");
    private Label futureLabel = new Label("-");
    private Label healthScoreLabel = new Label("100 / 100");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Pocket Money Manager - Smart Entry");

        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(25));
        mainContainer.getStyleClass().add("root");

        // Header
        VBox header = new VBox(4);
        Label title = new Label("Student Pocket Money Manager");
        title.getStyleClass().add("header-title");
        Label subtitle = new Label("Enter student allowance & expenses to predict financial habits");
        subtitle.getStyleClass().add("header-subtitle");
        header.getChildren().addAll(title, subtitle);

        // Grid Card
        GridPane grid = new GridPane();
        grid.getStyleClass().add("card");
        grid.setPadding(new Insets(20));
        grid.setVgap(12);
        grid.setHgap(15);

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("e.g. STU1001");
        TextField pocketMoneyField = new TextField();
        pocketMoneyField.setPromptText("e.g. 500");
        TextField expenseField = new TextField();
        expenseField.setPromptText("e.g. 350");
        TextField savingsField = new TextField();
        savingsField.setPromptText("e.g. 150");
        TextField educationMoneyField = new TextField();
        educationMoneyField.setPromptText("e.g. 80");
        TextField transportCostField = new TextField();
        transportCostField.setPromptText("e.g. 40");
        TextField borrowMoneyAmountField = new TextField();
        borrowMoneyAmountField.setPromptText("e.g. 0");

        grid.add(createLabel("Student ID:"), 0, 0);
        grid.add(studentIdField, 1, 0);
        grid.add(createLabel("Pocket Money Given ($):"), 0, 1);
        grid.add(pocketMoneyField, 1, 1);
        grid.add(createLabel("Total Expense ($):"), 0, 2);
        grid.add(expenseField, 1, 2);
        grid.add(createLabel("Monthly Savings ($):"), 0, 3);
        grid.add(savingsField, 1, 3);
        grid.add(createLabel("Education Money ($):"), 0, 4);
        grid.add(educationMoneyField, 1, 4);
        grid.add(createLabel("Transport Cost ($):"), 0, 5);
        grid.add(transportCostField, 1, 5);
        grid.add(createLabel("Borrow Money Amount ($):"), 0, 6);
        grid.add(borrowMoneyAmountField, 1, 6);

        HBox btnBox = new HBox(12);
        Button submitButton = new Button("💾 Submit & Predict");
        submitButton.getStyleClass().add("btn-primary");

        Button downloadButton = new Button("📂 Open CSV File");
        downloadButton.getStyleClass().add("btn-secondary");

        Button sampleButton = new Button("⚡ Fill Sample Data");
        sampleButton.getStyleClass().add("btn-secondary");

        btnBox.getChildren().addAll(submitButton, downloadButton, sampleButton);
        grid.add(btnBox, 1, 7);

        // Results Card
        GridPane resultsGrid = new GridPane();
        resultsGrid.getStyleClass().add("card");
        resultsGrid.setPadding(new Insets(15));
        resultsGrid.setVgap(10);
        resultsGrid.setHgap(15);

        resultsGrid.add(createLabel("Saving Behavior:"), 0, 0);
        resultsGrid.add(savingBehaviorLabel, 1, 0);
        resultsGrid.add(createLabel("Financial Stress:"), 0, 1);
        resultsGrid.add(financialStressLabel, 1, 1);
        resultsGrid.add(createLabel("Spending Type:"), 0, 2);
        resultsGrid.add(spendingTypeLabel, 1, 2);
        resultsGrid.add(createLabel("Future Outlook:"), 0, 3);
        resultsGrid.add(futureLabel, 1, 3);
        resultsGrid.add(createLabel("Health Score:"), 0, 4);
        resultsGrid.add(healthScoreLabel, 1, 4);
        resultsGrid.add(createLabel("CSV Storage:"), 0, 5);
        resultsGrid.add(fileLinkLabel, 1, 5);
        fileLinkLabel.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");

        savingBehaviorLabel.setStyle("-fx-text-fill: #22d3ee; -fx-font-weight: bold;");
        financialStressLabel.setStyle("-fx-text-fill: #00f5a0; -fx-font-weight: bold;");
        spendingTypeLabel.setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;");
        futureLabel.setStyle("-fx-text-fill: #d946ef; -fx-font-weight: bold;");
        healthScoreLabel.setStyle("-fx-text-fill: #00f5a0; -fx-font-weight: bold; -fx-font-size: 14px;");

        submitButton.setOnAction(e -> {
            try {
                handleSubmission(studentIdField,
                        pocketMoneyField,
                        expenseField,
                        savingsField,
                        educationMoneyField,
                        transportCostField,
                        borrowMoneyAmountField);
            } catch (IOException ex) {
                showAlert("Error", "Failed to save data to CSV: " + ex.getMessage());
            }
        });

        sampleButton.setOnAction(e -> {
            studentIdField.setText("STU-" + (100 + (int)(Math.random() * 900)));
            pocketMoneyField.setText("550");
            expenseField.setText("380");
            savingsField.setText("170");
            educationMoneyField.setText("90");
            transportCostField.setText("40");
            borrowMoneyAmountField.setText("0");
        });

        downloadButton.setOnAction(e -> {
            File csvFile = new File(CSV_FILE);
            if (!csvFile.exists()) {
                showAlert("File Not Found", "CSV file does not exist yet. Please add data first.");
                return;
            }
            try {
                getHostServices().showDocument(csvFile.toURI().toString());
            } catch (Exception ex) {
                showAlert("Error", "CSV Path: " + csvFile.getAbsolutePath());
            }
        });

        mainContainer.getChildren().addAll(header, grid, resultsGrid);

        ScrollPane sp = new ScrollPane(mainContainer);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(sp, 700, 750);
        try {
            File css = new File("style.css");
            if (css.exists()) scene.getStylesheets().add(css.toURI().toString());
        } catch (Exception ignored) {}

        primaryStage.setScene(scene);
        primaryStage.show();

        fileLinkLabel.setText(new File(CSV_FILE).getAbsolutePath());
    }

    private Label createLabel(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("form-label");
        return l;
    }

    private void handleSubmission(TextField studentIdField,
                                  TextField pocketMoneyField,
                                  TextField expenseField,
                                  TextField savingsField,
                                  TextField educationMoneyField,
                                  TextField transportCostField,
                                  TextField borrowMoneyAmountField) throws IOException {

        String studentId = studentIdField.getText().trim();
        String pocketMoneyStr = pocketMoneyField.getText().trim();
        String expenseStr = expenseField.getText().trim();
        String savingsStr = savingsField.getText().trim();
        String educationMoneyStr = educationMoneyField.getText().trim();
        String transportCostStr = transportCostField.getText().trim();
        String borrowMoneyAmountStr = borrowMoneyAmountField.getText().trim();

        if (studentId.isEmpty() || pocketMoneyStr.isEmpty() || expenseStr.isEmpty()
                || savingsStr.isEmpty() || educationMoneyStr.isEmpty() || transportCostStr.isEmpty()
                || borrowMoneyAmountStr.isEmpty()) {
            showAlert("Validation Error", "Please fill all fields.");
            return;
        }

        double pocketMoney, expense, savings, educationMoney, transportCost, borrowMoneyAmount;
        try {
            pocketMoney = Double.parseDouble(pocketMoneyStr);
            expense = Double.parseDouble(expenseStr);
            savings = Double.parseDouble(savingsStr);
            educationMoney = Double.parseDouble(educationMoneyStr);
            transportCost = Double.parseDouble(transportCostStr);
            borrowMoneyAmount = Double.parseDouble(borrowMoneyAmountStr);
        } catch (NumberFormatException nfe) {
            showAlert("Validation Error", "Please enter valid numeric values.");
            return;
        }

        double spendingPercent = (expense / pocketMoney) * 100;
        double savingPercent = (savings / pocketMoney) * 100;
        double discretionaryPercent = ((expense - (educationMoney + transportCost)) / pocketMoney) * 100;
        double nonDiscretionaryPercent = ((educationMoney + transportCost) / pocketMoney) * 100;

        String savingBehavior;
        String future;
        if (spendingPercent > 80) {
            savingBehavior = "High consumption & low savings";
            future = "Future Consumer Mindset";
        } else if (savingPercent > 40) {
            savingBehavior = "Balanced consumption & savings";
            future = "Future Planner";
        } else {
            savingBehavior = "Moderate";
            future = "Uncertain Future";
        }

        String financialStress;
        if (borrowMoneyAmount > 0 || expense > pocketMoney * 1.10) {
            financialStress = "Future Financial Stress";
            financialStressLabel.setStyle("-fx-text-fill: #ff0055; -fx-font-weight: bold;");
        } else {
            financialStress = "Financially Secure Future";
            financialStressLabel.setStyle("-fx-text-fill: #00f5a0; -fx-font-weight: bold;");
        }

        String spendingType;
        if (discretionaryPercent > 50) {
            spendingType = "Lifestyle-Oriented Student";
        } else if (nonDiscretionaryPercent > 10) {
            spendingType = "Goal-Oriented Student";
        } else {
            spendingType = "Mixed Spending";
        }

        int score = 100;
        if (borrowMoneyAmount > 0) score -= 30;
        if (spendingPercent > 85) score -= 30;
        if (savingPercent > 35) score += 10;
        score = Math.max(0, Math.min(100, score));

        File csvFile = new File(CSV_FILE);
        boolean fileExists = csvFile.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            if (!fileExists) {
                writer.write("studentId,pocketMoneyGiven,expense,savings,educationMoney,transportCost,SavingBehavior,borrowMoneyAmount,FinancialStress,SpendingType,Future");
                writer.newLine();
            }
            DecimalFormat df = new DecimalFormat("#.##");
            String line = String.join(",",
                    studentId,
                    df.format(pocketMoney),
                    df.format(expense),
                    df.format(savings),
                    df.format(educationMoney),
                    df.format(transportCost),
                    savingBehavior,
                    df.format(borrowMoneyAmount),
                    financialStress,
                    spendingType,
                    future);
            writer.write(line);
            writer.newLine();
            writer.flush();
        }

        savingBehaviorLabel.setText(savingBehavior);
        financialStressLabel.setText(financialStress);
        spendingTypeLabel.setText(spendingType);
        futureLabel.setText(future);
        healthScoreLabel.setText(score + " / 100");
        fileLinkLabel.setText(csvFile.getAbsolutePath());

        showAlert("Success", "Data saved and predictions computed!\nSaved in: " + csvFile.getAbsolutePath());

        studentIdField.clear();
        pocketMoneyField.clear();
        expenseField.clear();
        savingsField.clear();
        educationMoneyField.clear();
        transportCostField.clear();
        borrowMoneyAmountField.clear();
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
