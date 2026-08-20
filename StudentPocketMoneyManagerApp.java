package application;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StudentPocketMoneyManagerApp extends Application {

    private static final String CSV_FILE = System.getProperty("user.home") + File.separator + "students_data.csv";
    private static final DecimalFormat DF = new DecimalFormat("#.##");

    // Table & Data
    private TableView<StudentRecord> tableView = new TableView<>();
    private ObservableList<StudentRecord> studentList = FXCollections.observableArrayList();

    // Dashboard metrics
    private Label totalStudentsVal = new Label("0");
    private Label avgPocketMoneyVal = new Label("$0.00");
    private Label avgSavingsRateVal = new Label("0%");
    private Label highStressCountVal = new Label("0");

    // Dashboard Charts
    private PieChart expensePieChart = new PieChart();
    private BarChart<String, Number> spendingBarChart;

    // Real-time Entry labels
    private Label liveHealthScoreBadge = new Label("100 / 100 (Excellent)");
    private Label liveSavingBehaviorLabel = new Label("Awaiting input");
    private Label liveFinancialStressLabel = new Label("Awaiting input");
    private Label liveSpendingTypeLabel = new Label("Awaiting input");
    private Label liveFutureLabel = new Label("Awaiting input");
    private ProgressBar liveSpendingBar = new ProgressBar(0);
    private ProgressBar liveSavingBar = new ProgressBar(0);

    // Lookup Details pane
    private VBox detailDossierBox = new VBox(10);
    private Label detailStudentId = new Label("-");
    private Label detailPocketMoney = new Label("-");
    private Label detailExpense = new Label("-");
    private Label detailSavings = new Label("-");
    private Label detailEducation = new Label("-");
    private Label detailTransport = new Label("-");
    private Label detailBorrow = new Label("-");
    private Label detailStressBadge = new Label("-");
    private Label detailPersonaBadge = new Label("-");
    private TextArea detailAdviceArea = new TextArea();

    public static class StudentRecord {
        private final SimpleStringProperty studentId;
        private final SimpleStringProperty pocketMoney;
        private final SimpleStringProperty expense;
        private final SimpleStringProperty savings;
        private final SimpleStringProperty educationMoney;
        private final SimpleStringProperty transportCost;
        private final SimpleStringProperty savingBehavior;
        private final SimpleStringProperty borrowMoney;
        private final SimpleStringProperty financialStress;
        private final SimpleStringProperty spendingType;
        private final SimpleStringProperty future;

        public StudentRecord(String studentId, String pocketMoney, String expense, String savings,
                             String educationMoney, String transportCost, String savingBehavior,
                             String borrowMoney, String financialStress, String spendingType, String future) {
            this.studentId = new SimpleStringProperty(studentId);
            this.pocketMoney = new SimpleStringProperty(pocketMoney);
            this.expense = new SimpleStringProperty(expense);
            this.savings = new SimpleStringProperty(savings);
            this.educationMoney = new SimpleStringProperty(educationMoney);
            this.transportCost = new SimpleStringProperty(transportCost);
            this.savingBehavior = new SimpleStringProperty(savingBehavior);
            this.borrowMoney = new SimpleStringProperty(borrowMoney);
            this.financialStress = new SimpleStringProperty(financialStress);
            this.spendingType = new SimpleStringProperty(spendingType);
            this.future = new SimpleStringProperty(future);
        }

        public String getStudentId() { return studentId.get(); }
        public String getPocketMoney() { return pocketMoney.get(); }
        public String getExpense() { return expense.get(); }
        public String getSavings() { return savings.get(); }
        public String getEducationMoney() { return educationMoney.get(); }
        public String getTransportCost() { return transportCost.get(); }
        public String getSavingBehavior() { return savingBehavior.get(); }
        public String getBorrowMoney() { return borrowMoney.get(); }
        public String getFinancialStress() { return financialStress.get(); }
        public String getSpendingType() { return spendingType.get(); }
        public String getFuture() { return future.get(); }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Pocket Money Management & Financial Intelligence Platform");

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // Top Navigation Header
        HBox header = createTopHeader();
        root.setTop(header);

        // Main Tab Pane
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab dashboardTab = new Tab("📊 Executive Dashboard", createDashboardView());
        Tab entryTab = new Tab("✍️ Smart Entry & AI Predictor", createEntryView());
        Tab recordsTab = new Tab("🔍 Student Records & Lookup", createRecordsView());
        Tab advisorTab = new Tab("💡 AI Financial Advisor", createAdvisorView());

        tabPane.getTabs().addAll(dashboardTab, entryTab, recordsTab, advisorTab);
        root.setCenter(tabPane);

        // Load Initial CSV Data
        loadDataFromCsv();

        Scene scene = new Scene(root, 1100, 780);
        
        // Attach CSS
        try {
            File cssFile = new File("style.css");
            if (cssFile.exists()) {
                scene.getStylesheets().add(cssFile.toURI().toString());
            } else {
                var resource = getClass().getResource("style.css");
                if (resource != null) scene.getStylesheets().add(resource.toExternalForm());
            }
        } catch (Exception ex) {
            System.err.println("CSS loading error: " + ex.getMessage());
        }

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    private HBox createTopHeader() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setStyle("-fx-background-color: #101022; -fx-border-color: #2e2a5c; -fx-border-width: 0 0 1px 0;");

        VBox titleBox = new VBox(2);
        Label title = new Label("Student Pocket Money Management");
        title.getStyleClass().add("header-title");
        Label subtitle = new Label("Smart AI Financial Analytics & Budget Intelligence System");
        subtitle.getStyleClass().add("header-subtitle");
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button openCsvBtn = new Button("📂 Open CSV File");
        openCsvBtn.getStyleClass().add("btn-secondary");
        openCsvBtn.setOnAction(e -> openCsvInExplorer());

        Button reloadBtn = new Button("🔄 Refresh All");
        reloadBtn.getStyleClass().add("btn-primary");
        reloadBtn.setOnAction(e -> {
            loadDataFromCsv();
            showAlert("Refreshed", "Dashboard data and student records refreshed successfully.");
        });

        header.getChildren().addAll(titleBox, spacer, openCsvBtn, reloadBtn);
        return header;
    }

    // TAB 1: Dashboard View
    private ScrollPane createDashboardView() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Metric Cards Row
        HBox cardsRow = new HBox(15);
        cardsRow.setAlignment(Pos.CENTER);

        VBox card1 = createStatCard("TOTAL STUDENTS TRACKED", totalStudentsVal, "#8b5cf6");
        VBox card2 = createStatCard("AVG POCKET MONEY", avgPocketMoneyVal, "#22d3ee");
        VBox card3 = createStatCard("AVG SAVINGS RATE", avgSavingsRateVal, "#00f5a0");
        VBox card4 = createStatCard("FINANCIAL STRESS ALERTS", highStressCountVal, "#ff0055");

        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);
        HBox.setHgrow(card4, Priority.ALWAYS);
        cardsRow.getChildren().addAll(card1, card2, card3, card4);

        // Charts Row
        HBox chartsRow = new HBox(20);
        chartsRow.setPrefHeight(400);

        // Pie Chart Box
        VBox pieBox = new VBox(10);
        pieBox.getStyleClass().add("card");
        HBox.setHgrow(pieBox, Priority.ALWAYS);
        Label pieTitle = new Label("Overall Budget Allocation Breakdown");
        pieTitle.getStyleClass().add("section-title");
        expensePieChart.setTitle("Expenditure & Savings Distribution");
        expensePieChart.setLabelsVisible(true);
        pieBox.getChildren().addAll(pieTitle, expensePieChart);

        // Bar Chart Box
        VBox barBox = new VBox(10);
        barBox.getStyleClass().add("card");
        HBox.setHgrow(barBox, Priority.ALWAYS);
        Label barTitle = new Label("Student Spending Personas Distribution");
        barTitle.getStyleClass().add("section-title");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Persona Category");
        yAxis.setLabel("Number of Students");
        spendingBarChart = new BarChart<>(xAxis, yAxis);
        spendingBarChart.setTitle("Behavior Segments");
        spendingBarChart.setLegendVisible(false);
        barBox.getChildren().addAll(barTitle, spendingBarChart);

        chartsRow.getChildren().addAll(pieBox, barBox);

        content.getChildren().addAll(cardsRow, chartsRow);

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent;");
        return sp;
    }

    private VBox createStatCard(String labelText, Label valueLabel, String accentColor) {
        VBox card = new VBox(6);
        card.getStyleClass().add("card");
        card.setStyle("-fx-border-color: " + accentColor + "; -fx-border-width: 1px 1px 1px 3px;");
        
        Label title = new Label(labelText);
        title.getStyleClass().add("stat-card-title");
        
        valueLabel.getStyleClass().add("stat-card-value");
        valueLabel.setStyle("-fx-text-fill: " + accentColor + ";");

        card.getChildren().addAll(title, valueLabel);
        return card;
    }

    // TAB 2: Smart Entry & AI Predictor View
    private ScrollPane createEntryView() {
        HBox mainContainer = new HBox(20);
        mainContainer.setPadding(new Insets(20));

        // Left Form Box
        VBox formCard = new VBox(15);
        formCard.getStyleClass().add("card");
        formCard.setPrefWidth(460);

        Label formTitle = new Label("New Student Financial Profile");
        formTitle.getStyleClass().add("section-title");

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("e.g. STU1001 or Alice");
        TextField pocketMoneyField = new TextField();
        pocketMoneyField.setPromptText("Monthly allowance (e.g. 500)");
        TextField expenseField = new TextField();
        expenseField.setPromptText("Total monthly expenses (e.g. 350)");
        TextField savingsField = new TextField();
        savingsField.setPromptText("Monthly savings (e.g. 150)");
        TextField educationMoneyField = new TextField();
        educationMoneyField.setPromptText("Books, courses, supplies (e.g. 80)");
        TextField transportCostField = new TextField();
        transportCostField.setPromptText("Bus, fuel, transit (e.g. 40)");
        TextField borrowMoneyField = new TextField();
        borrowMoneyField.setPromptText("Debt or borrowed money (e.g. 0)");

        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);

        grid.add(createFieldLabel("Student ID / Name:"), 0, 0);
        grid.add(studentIdField, 1, 0);
        grid.add(createFieldLabel("Pocket Money Given ($):"), 0, 1);
        grid.add(pocketMoneyField, 1, 1);
        grid.add(createFieldLabel("Total Expense ($):"), 0, 2);
        grid.add(expenseField, 1, 2);
        grid.add(createFieldLabel("Savings ($):"), 0, 3);
        grid.add(savingsField, 1, 3);
        grid.add(createFieldLabel("Education Money ($):"), 0, 4);
        grid.add(educationMoneyField, 1, 4);
        grid.add(createFieldLabel("Transport Cost ($):"), 0, 5);
        grid.add(transportCostField, 1, 5);
        grid.add(createFieldLabel("Borrowed Money ($):"), 0, 6);
        grid.add(borrowMoneyField, 1, 6);

        // Real-time live update listeners on all fields
        Runnable recalculateLive = () -> updateLivePrediction(
                pocketMoneyField.getText(),
                expenseField.getText(),
                savingsField.getText(),
                educationMoneyField.getText(),
                transportCostField.getText(),
                borrowMoneyField.getText()
        );

        pocketMoneyField.textProperty().addListener((obs, oldV, newV) -> recalculateLive.run());
        expenseField.textProperty().addListener((obs, oldV, newV) -> recalculateLive.run());
        savingsField.textProperty().addListener((obs, oldV, newV) -> recalculateLive.run());
        educationMoneyField.textProperty().addListener((obs, oldV, newV) -> recalculateLive.run());
        transportCostField.textProperty().addListener((obs, oldV, newV) -> recalculateLive.run());
        borrowMoneyField.textProperty().addListener((obs, oldV, newV) -> recalculateLive.run());

        HBox buttonBox = new HBox(12);
        Button submitBtn = new Button("💾 Save Record & Predict");
        submitBtn.getStyleClass().add("btn-primary");
        Button clearBtn = new Button("🧹 Clear Form");
        clearBtn.getStyleClass().add("btn-secondary");
        Button sampleBtn = new Button("⚡ Fill Sample");
        sampleBtn.getStyleClass().add("btn-secondary");

        buttonBox.getChildren().addAll(submitBtn, clearBtn, sampleBtn);

        submitBtn.setOnAction(e -> {
            boolean success = handleRecordSubmission(
                    studentIdField.getText().trim(),
                    pocketMoneyField.getText().trim(),
                    expenseField.getText().trim(),
                    savingsField.getText().trim(),
                    educationMoneyField.getText().trim(),
                    transportCostField.getText().trim(),
                    borrowMoneyField.getText().trim()
            );
            if (success) {
                studentIdField.clear();
                pocketMoneyField.clear();
                expenseField.clear();
                savingsField.clear();
                educationMoneyField.clear();
                transportCostField.clear();
                borrowMoneyField.clear();
            }
        });

        clearBtn.setOnAction(e -> {
            studentIdField.clear();
            pocketMoneyField.clear();
            expenseField.clear();
            savingsField.clear();
            educationMoneyField.clear();
            transportCostField.clear();
            borrowMoneyField.clear();
        });

        sampleBtn.setOnAction(e -> {
            studentIdField.setText("STU-" + (1000 + (int)(Math.random() * 9000)));
            pocketMoneyField.setText("600");
            expenseField.setText("420");
            savingsField.setText("180");
            educationMoneyField.setText("100");
            transportCostField.setText("50");
            borrowMoneyField.setText("0");
        });

        formCard.getChildren().addAll(formTitle, grid, buttonBox);

        // Right AI Predictor Card
        VBox predictorCard = new VBox(15);
        predictorCard.getStyleClass().add("card");
        HBox.setHgrow(predictorCard, Priority.ALWAYS);

        Label predTitle = new Label("Live AI Financial Assessment & Scoring");
        predTitle.getStyleClass().add("section-title");

        VBox scoreCard = new VBox(6);
        scoreCard.setStyle("-fx-background-color: #0c0b17; -fx-padding: 15px; -fx-background-radius: 8px; -fx-border-color: #8b5cf6; -fx-border-radius: 8px;");
        Label scoreTitle = new Label("FINANCIAL HEALTH INDEX");
        scoreTitle.getStyleClass().add("stat-card-title");
        liveHealthScoreBadge.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #00f5a0;");
        scoreCard.getChildren().addAll(scoreTitle, liveHealthScoreBadge);

        GridPane predGrid = new GridPane();
        predGrid.setVgap(12);
        predGrid.setHgap(15);

        predGrid.add(createFieldLabel("Saving Behavior:"), 0, 0);
        predGrid.add(liveSavingBehaviorLabel, 1, 0);
        predGrid.add(createFieldLabel("Financial Stress Risk:"), 0, 1);
        predGrid.add(liveFinancialStressLabel, 1, 1);
        predGrid.add(createFieldLabel("Spending Persona:"), 0, 2);
        predGrid.add(liveSpendingTypeLabel, 1, 2);
        predGrid.add(createFieldLabel("Future Outlook:"), 0, 3);
        predGrid.add(liveFutureLabel, 1, 3);

        liveSavingBehaviorLabel.getStyleClass().addAll("badge", "badge-info");
        liveFinancialStressLabel.getStyleClass().addAll("badge", "badge-success");
        liveSpendingTypeLabel.getStyleClass().addAll("badge", "badge-info");
        liveFutureLabel.getStyleClass().addAll("badge", "badge-info");

        // Progress indicators
        VBox progressBox = new VBox(10);
        progressBox.setPadding(new Insets(10, 0, 0, 0));

        Label spendBarLabel = new Label("Spending vs Allowance Ratio:");
        spendBarLabel.getStyleClass().add("form-label");
        liveSpendingBar.setMaxWidth(Double.MAX_VALUE);
        liveSpendingBar.setStyle("-fx-accent: #fbbf24;");

        Label saveBarLabel = new Label("Savings Rate Ratio:");
        saveBarLabel.getStyleClass().add("form-label");
        liveSavingBar.setMaxWidth(Double.MAX_VALUE);
        liveSavingBar.setStyle("-fx-accent: #00f5a0;");

        progressBox.getChildren().addAll(spendBarLabel, liveSpendingBar, saveBarLabel, liveSavingBar);

        predictorCard.getChildren().addAll(predTitle, scoreCard, predGrid, progressBox);

        mainContainer.getChildren().addAll(formCard, predictorCard);

        ScrollPane sp = new ScrollPane(mainContainer);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent;");
        return sp;
    }

    private Label createFieldLabel(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("form-label");
        return lbl;
    }

    private void updateLivePrediction(String pmStr, String expStr, String savStr, String eduStr, String trnStr, String borStr) {
        try {
            double pm = Double.parseDouble(pmStr);
            double exp = Double.parseDouble(expStr);
            double sav = Double.parseDouble(savStr);
            double edu = Double.parseDouble(eduStr);
            double trn = Double.parseDouble(trnStr);
            double bor = Double.parseDouble(borStr);

            if (pm <= 0) return;

            double spendingPercent = (exp / pm) * 100;
            double savingPercent = (sav / pm) * 100;
            double discretionaryPercent = ((exp - (edu + trn)) / pm) * 100;
            double nonDiscretionaryPercent = ((edu + trn) / pm) * 100;

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
            if (bor > 0 || exp > pm * 1.10) {
                financialStress = "Future Financial Stress";
                liveFinancialStressLabel.setStyle("-fx-background-color: rgba(255, 0, 85, 0.2); -fx-text-fill: #ff0055; -fx-border-color: #ff0055;");
            } else {
                financialStress = "Financially Secure Future";
                liveFinancialStressLabel.setStyle("-fx-background-color: rgba(0, 245, 160, 0.2); -fx-text-fill: #00f5a0; -fx-border-color: #00f5a0;");
            }

            String spendingType;
            if (discretionaryPercent > 50) {
                spendingType = "Lifestyle-Oriented Student";
            } else if (nonDiscretionaryPercent > 10) {
                spendingType = "Goal-Oriented Student";
            } else {
                spendingType = "Mixed Spending";
            }

            // Health Score (0 - 100)
            int healthScore = 100;
            if (bor > 0) healthScore -= 30;
            if (spendingPercent > 90) healthScore -= 30;
            else if (spendingPercent > 70) healthScore -= 15;
            if (savingPercent >= 30) healthScore += 10;
            else if (savingPercent < 10) healthScore -= 20;
            healthScore = Math.max(0, Math.min(100, healthScore));

            String rating = (healthScore >= 80) ? "Excellent" : (healthScore >= 55 ? "Good" : (healthScore >= 35 ? "Warning" : "Critical"));
            String color = (healthScore >= 80) ? "#10b981" : (healthScore >= 55 ? "#38bdf8" : (healthScore >= 35 ? "#f59e0b" : "#f43f5e"));

            liveHealthScoreBadge.setText(healthScore + " / 100 (" + rating + ")");
            liveHealthScoreBadge.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

            liveSavingBehaviorLabel.setText(savingBehavior);
            liveFinancialStressLabel.setText(financialStress);
            liveSpendingTypeLabel.setText(spendingType);
            liveFutureLabel.setText(future);

            liveSpendingBar.setProgress(Math.min(1.0, spendingPercent / 100.0));
            liveSavingBar.setProgress(Math.min(1.0, savingPercent / 100.0));

        } catch (Exception ex) {
            // Keep default display on incomplete or invalid input
            liveHealthScoreBadge.setText("100 / 100 (Awaiting Input)");
            liveHealthScoreBadge.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #94a3b8;");
            liveSavingBehaviorLabel.setText("Awaiting input");
            liveFinancialStressLabel.setText("Awaiting input");
            liveSpendingTypeLabel.setText("Awaiting input");
            liveFutureLabel.setText("Awaiting input");
            liveSpendingBar.setProgress(0);
            liveSavingBar.setProgress(0);
        }
    }

    // TAB 3: Records Explorer & Lookup
    private VBox createRecordsView() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Top Filter Bar
        HBox filterBar = new HBox(12);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        Label searchLbl = new Label("Quick Search / Lookup:");
        searchLbl.getStyleClass().add("form-label");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter Student ID, persona, or keyword...");
        searchField.setPrefWidth(300);

        Button deleteBtn = new Button("🗑️ Delete Selected");
        deleteBtn.getStyleClass().add("btn-secondary");
        deleteBtn.setStyle("-fx-text-fill: #fb7185;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label countLabel = new Label("Records count: 0");
        countLabel.setStyle("-fx-text-fill: #94a3b8;");

        filterBar.getChildren().addAll(searchLbl, searchField, spacer, countLabel, deleteBtn);

        // Split Pane: Left is TableView, Right is Student Dossier Card
        SplitPane splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-color: transparent; -fx-box-border: transparent;");

        setupTableView();
        tableView.setPrefWidth(650);

        VBox dossierBox = createStudentDossierCard();
        dossierBox.setPrefWidth(350);

        splitPane.getItems().addAll(tableView, dossierBox);
        splitPane.setDividerPositions(0.65);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        // Search filtering logic
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            if (newV == null || newV.trim().isEmpty()) {
                tableView.setItems(studentList);
            } else {
                String filter = newV.trim().toLowerCase();
                ObservableList<StudentRecord> filtered = FXCollections.observableArrayList();
                for (StudentRecord r : studentList) {
                    if (r.getStudentId().toLowerCase().contains(filter) ||
                        r.getSpendingType().toLowerCase().contains(filter) ||
                        r.getFinancialStress().toLowerCase().contains(filter) ||
                        r.getSavingBehavior().toLowerCase().contains(filter)) {
                        filtered.add(r);
                    }
                }
                tableView.setItems(filtered);
            }
            countLabel.setText("Records count: " + tableView.getItems().size());
        });

        // Row Selection Listener to populate Dossier Card
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showStudentDossier(newSelection);
            }
        });

        deleteBtn.setOnAction(e -> {
            StudentRecord selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Selection Required", "Please select a student record from the table to delete.");
                return;
            }
            deleteStudentRecord(selected.getStudentId());
        });

        content.getChildren().addAll(filterBar, splitPane);
        return content;
    }

    private void setupTableView() {
        TableColumn<StudentRecord, String> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(data -> data.getValue().studentId);
        idCol.setPrefWidth(100);

        TableColumn<StudentRecord, String> pmCol = new TableColumn<>("Pocket Money");
        pmCol.setCellValueFactory(data -> data.getValue().pocketMoney);
        pmCol.setPrefWidth(90);

        TableColumn<StudentRecord, String> expCol = new TableColumn<>("Expense");
        expCol.setCellValueFactory(data -> data.getValue().expense);
        expCol.setPrefWidth(80);

        TableColumn<StudentRecord, String> savCol = new TableColumn<>("Savings");
        savCol.setCellValueFactory(data -> data.getValue().savings);
        savCol.setPrefWidth(80);

        TableColumn<StudentRecord, String> personaCol = new TableColumn<>("Spending Persona");
        personaCol.setCellValueFactory(data -> data.getValue().spendingType);
        personaCol.setPrefWidth(140);

        TableColumn<StudentRecord, String> stressCol = new TableColumn<>("Stress Status");
        stressCol.setCellValueFactory(data -> data.getValue().financialStress);
        stressCol.setPrefWidth(140);

        TableColumn<StudentRecord, String> futureCol = new TableColumn<>("Future Outlook");
        futureCol.setCellValueFactory(data -> data.getValue().future);
        futureCol.setPrefWidth(130);

        tableView.getColumns().setAll(idCol, pmCol, expCol, savCol, personaCol, stressCol, futureCol);
        tableView.setItems(studentList);
    }

    private VBox createStudentDossierCard() {
        detailDossierBox.getStyleClass().add("card");
        detailDossierBox.setPadding(new Insets(15));

        Label title = new Label("Student Financial Dossier");
        title.getStyleClass().add("section-title");

        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);

        grid.add(createFieldLabel("Student ID:"), 0, 0);
        grid.add(detailStudentId, 1, 0);
        grid.add(createFieldLabel("Allowance:"), 0, 1);
        grid.add(detailPocketMoney, 1, 1);
        grid.add(createFieldLabel("Total Expense:"), 0, 2);
        grid.add(detailExpense, 1, 2);
        grid.add(createFieldLabel("Monthly Savings:"), 0, 3);
        grid.add(detailSavings, 1, 3);
        grid.add(createFieldLabel("Education:"), 0, 4);
        grid.add(detailEducation, 1, 4);
        grid.add(createFieldLabel("Transport:"), 0, 5);
        grid.add(detailTransport, 1, 5);
        grid.add(createFieldLabel("Borrowed Debt:"), 0, 6);
        grid.add(detailBorrow, 1, 6);
        grid.add(createFieldLabel("Stress Level:"), 0, 7);
        grid.add(detailStressBadge, 1, 7);
        grid.add(createFieldLabel("Persona:"), 0, 8);
        grid.add(detailPersonaBadge, 1, 8);

        Label adviceHeader = new Label("🎯 AI Advisory & Action Plan:");
        adviceHeader.getStyleClass().add("form-label");
        detailAdviceArea.setEditable(false);
        detailAdviceArea.setWrapText(true);
        detailAdviceArea.setPrefHeight(160);
        detailAdviceArea.setText("Select a student record to inspect personalized financial advice.");

        detailDossierBox.getChildren().addAll(title, grid, adviceHeader, detailAdviceArea);
        return detailDossierBox;
    }

    private void showStudentDossier(StudentRecord r) {
        detailStudentId.setText(r.getStudentId());
        detailStudentId.setStyle("-fx-text-fill: #22d3ee; -fx-font-weight: bold;");
        detailPocketMoney.setText("$" + r.getPocketMoney());
        detailExpense.setText("$" + r.getExpense());
        detailSavings.setText("$" + r.getSavings());
        detailEducation.setText("$" + r.getEducationMoney());
        detailTransport.setText("$" + r.getTransportCost());
        detailBorrow.setText("$" + r.getBorrowMoney());
        
        detailStressBadge.setText(r.getFinancialStress());
        if (r.getFinancialStress().contains("Stress")) {
            detailStressBadge.setStyle("-fx-text-fill: #ff0055; -fx-font-weight: bold;");
        } else {
            detailStressBadge.setStyle("-fx-text-fill: #00f5a0; -fx-font-weight: bold;");
        }

        detailPersonaBadge.setText(r.getSpendingType());
        detailPersonaBadge.setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;");

        // Generate dynamic AI Financial Advice
        double pm = Double.parseDouble(r.getPocketMoney().replace("$", ""));
        double exp = Double.parseDouble(r.getExpense().replace("$", ""));
        double sav = Double.parseDouble(r.getSavings().replace("$", ""));
        double bor = Double.parseDouble(r.getBorrowMoney().replace("$", ""));

        StringBuilder advice = new StringBuilder();
        advice.append("Student ID: ").append(r.getStudentId()).append("\n\n");

        if (bor > 0) {
            advice.append("⚠️ URGENT DEBT ALERT: Student has $").append(DF.format(bor))
                  .append(" in borrowed debt. Priority #1 is clearing this debt to avoid interest.\n\n");
        }

        double savRate = (sav / pm) * 100;
        if (savRate >= 40) {
            advice.append("🌟 EXCELLENT SAVER: Saving ").append(DF.format(savRate))
                  .append("% of pocket money. Recommend setting up an emergency fund or micro-investment.\n\n");
        } else if (savRate < 15) {
            advice.append("💡 SAVINGS TIP: Savings rate is only ").append(DF.format(savRate))
                  .append("%. Target saving at least 20% ($").append(DF.format(pm * 0.20)).append(") monthly.\n\n");
        }

        if (r.getSpendingType().contains("Lifestyle")) {
            advice.append("🛍️ LIFESTYLE OPTIMIZATION: Over 50% of budget is discretionary spending. Track dining out & leisure expenses with the 50/30/20 rule.");
        } else if (r.getSpendingType().contains("Goal")) {
            advice.append("📚 GOAL ORIENTED: Strong investment in educational materials and transport. Keep it up!");
        } else {
            advice.append("📊 BALANCED: Spending habits are well distributed. Maintain steady budget tracking.");
        }

        detailAdviceArea.setText(advice.toString());
    }

    // TAB 4: AI Advisor View
    private ScrollPane createAdvisorView() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));

        Label title = new Label("Smart Financial Guidelines for Students");
        title.getStyleClass().add("header-title");

        GridPane ruleGrid = new GridPane();
        ruleGrid.setHgap(20);
        ruleGrid.setVgap(20);

        VBox rule1 = createRuleCard("1. The 50 / 30 / 20 Student Budget Rule",
                "• 50% Needs: Books, tuition materials, daily commute transport, essential meals.\n" +
                "• 30% Wants: Entertainment, outings with friends, subscriptions, gadgets.\n" +
                "• 20% Savings: Emergency fund, future semester buffers, long-term goals.",
                "#6366f1");

        VBox rule2 = createRuleCard("2. Debt Avoidance & Zero-Borrowing",
                "• Avoid borrowing pocket money from peers or micro-credit apps.\n" +
                "• If debt is incurred, allocate the first portion of next month's allowance to pay it off.\n" +
                "• Practice the 48-Hour Rule before making unplanned purchases.",
                "#f43f5e");

        VBox rule3 = createRuleCard("3. Smart Transport & Academic Optimization",
                "• Take advantage of student discount transit passes and semester passes.\n" +
                "• Purchase digital textbook editions or library loans to lower education costs.\n" +
                "• Set up an automated savings pot on day 1 of receiving pocket money.",
                "#10b981");

        VBox rule4 = createRuleCard("4. Financial Health Matrix Interpretation",
                "• High Consumption / Low Savings: High risk of running out before month-end.\n" +
                "• Balanced / Future Planner: Solid financial discipline, ready for investing.\n" +
                "• Lifestyle-Oriented: High discretionary leaks; recommended weekly audit.",
                "#38bdf8");

        ruleGrid.add(rule1, 0, 0);
        ruleGrid.add(rule2, 1, 0);
        ruleGrid.add(rule3, 0, 1);
        ruleGrid.add(rule4, 1, 1);

        content.getChildren().addAll(title, ruleGrid);

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent;");
        return sp;
    }

    private VBox createRuleCard(String heading, String body, String color) {
        VBox box = new VBox(10);
        box.getStyleClass().add("card");
        box.setStyle("-fx-border-color: " + color + "; -fx-border-width: 1px 1px 1px 3px;");
        box.setPrefWidth(480);

        Label h = new Label(heading);
        h.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 15px;");

        Label b = new Label(body);
        b.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 13px; -fx-line-spacing: 4px;");
        b.setWrapText(true);

        box.getChildren().addAll(h, b);
        return box;
    }

    // CSV & Business Logic Operations
    private boolean handleRecordSubmission(String studentId, String pmStr, String expStr, String savStr,
                                           String eduStr, String trnStr, String borStr) {
        if (studentId.isEmpty() || pmStr.isEmpty() || expStr.isEmpty() || savStr.isEmpty() ||
            eduStr.isEmpty() || trnStr.isEmpty() || borStr.isEmpty()) {
            showAlert("Validation Error", "Please fill in all 7 required financial fields.");
            return false;
        }

        double pocketMoney, expense, savings, educationMoney, transportCost, borrowMoneyAmount;
        try {
            pocketMoney = Double.parseDouble(pmStr);
            expense = Double.parseDouble(expStr);
            savings = Double.parseDouble(savStr);
            educationMoney = Double.parseDouble(eduStr);
            transportCost = Double.parseDouble(trnStr);
            borrowMoneyAmount = Double.parseDouble(borStr);
        } catch (NumberFormatException nfe) {
            showAlert("Validation Error", "Please enter valid numeric figures for financial amounts.");
            return false;
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
        } else {
            financialStress = "Financially Secure Future";
        }

        String spendingType;
        if (discretionaryPercent > 50) {
            spendingType = "Lifestyle-Oriented Student";
        } else if (nonDiscretionaryPercent > 10) {
            spendingType = "Goal-Oriented Student";
        } else {
            spendingType = "Mixed Spending";
        }

        File csvFile = new File(CSV_FILE);
        boolean fileExists = csvFile.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            if (!fileExists) {
                writer.write("studentId,pocketMoneyGiven,expense,savings,educationMoney,transportCost,SavingBehavior,borrowMoneyAmount,FinancialStress,SpendingType,Future");
                writer.newLine();
            }
            String line = String.join(",",
                    studentId,
                    DF.format(pocketMoney),
                    DF.format(expense),
                    DF.format(savings),
                    DF.format(educationMoney),
                    DF.format(transportCost),
                    savingBehavior,
                    DF.format(borrowMoneyAmount),
                    financialStress,
                    spendingType,
                    future);
            writer.write(line);
            writer.newLine();
            writer.flush();
        } catch (IOException ioe) {
            showAlert("File Error", "Could not save to CSV: " + ioe.getMessage());
            return false;
        }

        // Reload data
        loadDataFromCsv();
        showAlert("Success", "Student record successfully saved!\nPredictions & Dashboard updated.");
        return true;
    }

    private void loadDataFromCsv() {
        studentList.clear();
        File file = new File(CSV_FILE);
        if (!file.exists()) {
            // Populate sample data if file does not exist
            createDefaultSampleData();
        }

        double totalPm = 0;
        double totalSav = 0;
        double totalEdu = 0;
        double totalTrn = 0;
        double totalExp = 0;
        int stressCount = 0;
        int lifestyleCount = 0;
        int goalCount = 0;
        int mixedCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String header = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 11) {
                    StudentRecord record = new StudentRecord(
                            parts[0], parts[1], parts[2], parts[3], parts[4],
                            parts[5], parts[6], parts[7], parts[8], parts[9], parts[10]
                    );
                    studentList.add(record);

                    try {
                        double pm = Double.parseDouble(parts[1]);
                        double exp = Double.parseDouble(parts[2]);
                        double sav = Double.parseDouble(parts[3]);
                        double edu = Double.parseDouble(parts[4]);
                        double trn = Double.parseDouble(parts[5]);

                        totalPm += pm;
                        totalExp += exp;
                        totalSav += sav;
                        totalEdu += edu;
                        totalTrn += trn;

                        if (parts[8].toLowerCase().contains("stress")) stressCount++;
                        if (parts[9].contains("Lifestyle")) lifestyleCount++;
                        else if (parts[9].contains("Goal")) goalCount++;
                        else mixedCount++;
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception ex) {
            System.err.println("Error reading CSV: " + ex.getMessage());
        }

        int totalCount = studentList.size();
        totalStudentsVal.setText(String.valueOf(totalCount));
        if (totalCount > 0) {
            avgPocketMoneyVal.setText("$" + DF.format(totalPm / totalCount));
            avgSavingsRateVal.setText(DF.format((totalSav / totalPm) * 100) + "%");
        } else {
            avgPocketMoneyVal.setText("$0.00");
            avgSavingsRateVal.setText("0%");
        }
        highStressCountVal.setText(String.valueOf(stressCount));

        // Update Pie Chart
        expensePieChart.getData().clear();
        double discretionaryTotal = Math.max(0, totalExp - (totalEdu + totalTrn));
        expensePieChart.getData().add(new PieChart.Data("Discretionary ($" + DF.format(discretionaryTotal) + ")", discretionaryTotal));
        expensePieChart.getData().add(new PieChart.Data("Education ($" + DF.format(totalEdu) + ")", totalEdu));
        expensePieChart.getData().add(new PieChart.Data("Transport ($" + DF.format(totalTrn) + ")", totalTrn));
        expensePieChart.getData().add(new PieChart.Data("Savings ($" + DF.format(totalSav) + ")", totalSav));

        // Update Bar Chart
        if (spendingBarChart != null) {
            spendingBarChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Students");
            series.getData().add(new XYChart.Data<>("Lifestyle-Oriented", lifestyleCount));
            series.getData().add(new XYChart.Data<>("Goal-Oriented", goalCount));
            series.getData().add(new XYChart.Data<>("Mixed Spending", mixedCount));
            spendingBarChart.getData().add(series);
        }
    }

    private void deleteStudentRecord(String studentId) {
        List<String> remainingLines = new ArrayList<>();
        File file = new File(CSV_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String header = br.readLine();
            if (header != null) remainingLines.add(header);
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && !parts[0].equals(studentId)) {
                    remainingLines.add(line);
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to read CSV: " + e.getMessage());
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (String l : remainingLines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to rewrite CSV: " + e.getMessage());
            return;
        }

        loadDataFromCsv();
        showAlert("Record Deleted", "Student " + studentId + " was removed.");
    }

    private void createDefaultSampleData() {
        File csvFile = new File(CSV_FILE);
        if (csvFile.exists()) return;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write("studentId,pocketMoneyGiven,expense,savings,educationMoney,transportCost,SavingBehavior,borrowMoneyAmount,FinancialStress,SpendingType,Future\n");
            writer.write("STU-101,500,320,180,80,40,Moderate,0,Financially Secure Future,Mixed Spending,Future Planner\n");
            writer.write("STU-102,400,390,10,50,30,High consumption & low savings,50,Future Financial Stress,Lifestyle-Oriented Student,Future Consumer Mindset\n");
            writer.write("STU-103,600,300,300,120,60,Balanced consumption & savings,0,Financially Secure Future,Goal-Oriented Student,Future Planner\n");
            writer.write("STU-104,450,350,100,70,50,Moderate,0,Financially Secure Future,Mixed Spending,Uncertain Future\n");
            writer.write("STU-105,350,340,10,30,20,High consumption & low savings,20,Future Financial Stress,Lifestyle-Oriented Student,Future Consumer Mindset\n");
        } catch (IOException e) {
            System.err.println("Could not create sample data: " + e.getMessage());
        }
    }

    private void openCsvInExplorer() {
        File csvFile = new File(CSV_FILE);
        if (!csvFile.exists()) {
            createDefaultSampleData();
        }
        try {
            getHostServices().showDocument(csvFile.toURI().toString());
        } catch (Exception ex) {
            showAlert("Info", "CSV Path: " + csvFile.getAbsolutePath());
        }
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
