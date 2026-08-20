# Student Pocket Money Management & Financial Intelligence Platform

[![Live Demo](https://img.shields.io/badge/Live_Demo-GitHub_Pages-8b5cf6?style=for-the-badge&logo=github)](https://soumya4504.github.io/Student-Pocket-Money-Management/)

> 🌐 **Live Web App**: [https://soumya4504.github.io/Student-Pocket-Money-Management/](https://soumya4504.github.io/Student-Pocket-Money-Management/)

An advanced student pocket money management and financial analytics system designed with predictive insights, budget tracking, and interactive visualizations.

---

## 🌟 Key Features

1. **📊 Executive Dashboard**:
   - High-level KPIs: Total Students, Average Pocket Money, Average Savings Rate (%), High-Risk Alert Counts.
   - Interactive **Doughnut/Pie Chart**: Spending & Savings allocation (Discretionary, Education, Transport, Savings).
   - Interactive **Bar Chart**: Persona distribution across student profiles.

2. **✍️ Smart Financial Entry & AI Health Predictor**:
   - Real-time dynamic health score calculation (**0–100 index**) and status badges.
   - Automatic classification:
     - **Saving Behavior**: *High consumption & low savings*, *Balanced consumption & savings*, or *Moderate*.
     - **Financial Stress Risk**: *Financially Secure Future* vs. *Future Financial Stress*.
     - **Spending Persona**: *Lifestyle-Oriented*, *Goal-Oriented*, or *Mixed Spending*.
     - **Future Outlook**: *Future Planner*, *Future Consumer Mindset*, or *Uncertain Future*.
   - Live progress bars for Expense and Savings ratios.

3. **🔍 Records Explorer & Student Lookup**:
   - Instant search and filtering by Student ID, Persona, or Financial Stress.
   - Detailed **Student Financial Dossier Card** with customized AI financial recommendations and budget prescriptions.
   - Actionable record management (Delete, Reload, Sample fill).

4. **💡 AI Budgeting Advisor**:
   - Comprehensive guidelines: **50/30/20 Student Framework**, **Zero-Borrowing Debt Shield**, and **Cost Optimization Tips**.

5. **📂 Data Persistence & Portability**:
   - Compatible with `students_data.csv` in `user.home`.
   - CSV Export & Import support.

---

## 🚀 How to Run the Project

### Option A: Interactive Web Dashboard (Instant & Zero-Config)
Simply double-click or open [`web/index.html`](file:///c:/Users/panda/OneDrive/Desktop/Student%20Pocket%20Money%20management/web/index.html) in any web browser (Chrome, Edge, Firefox):

- **Direct Open**: Right click on `web/index.html` → *Open with Google Chrome / Microsoft Edge*.
- **Or via Local Web Server**:
  ```bash
  python -m http.server 8085 --directory web
  ```
  Then open: `http://localhost:8085`

---

### Option B: JavaFX Desktop Application
If running via IDE (Eclipse, IntelliJ IDEA, VS Code) or command line:

#### 1. Running in Eclipse / IntelliJ IDEA
- Import this folder into Eclipse or IntelliJ.
- Ensure the **JavaFX SDK** library is added to your module path / build path.
- Add VM Options if using Java 11+:
  ```text
  --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
  ```
- Run `StudentPocketMoneyManagerApp.java` (Unified Dashboard) or `StudentPocketMoneyManagerSave.java` / `StudentDataLookup.java`.

#### 2. Running via Command Line with JavaFX SDK
```bash
javac --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml -d . StudentPocketMoneyManagerApp.java
java --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml application.StudentPocketMoneyManagerApp
```

---

## 📁 Project Structure

```text
Student Pocket Money management/
├── README.md                           # Documentation & Run Guide
├── style.css                           # JavaFX Modern Dark Theme Stylesheet
├── StudentPocketMoneyManagerApp.java   # Main Unified JavaFX Platform
├── StudentPocketMoneyManagerSave.java  # Modernized JavaFX Data Entry
├── StudentDataLookup.java              # Modernized JavaFX Lookup Tool
└── web/                                # Standalone Web Application
    ├── index.html                      # Modern Responsive UI
    ├── style.css                       # Glassmorphic Design System
    └── app.js                          # AI Analytics Engine & Chart.js Visualizations
```
