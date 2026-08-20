# 💰 Student Pocket Money Management - AI Financial Intelligence

[![Live Demo](https://img.shields.io/badge/Live_Demo-GitHub_Pages-8b5cf6?style=for-the-badge&logo=github)](https://soumya4504.github.io/Student-Pocket-Money-Management/)
[![Java](https://img.shields.io/badge/Java-JavaFX_GUI-00f5a0?style=for-the-badge&logo=openjdk&logoColor=black)](https://github.com/soumya4504/Student-Pocket-Money-Management)
[![Chart.js](https://img.shields.io/badge/Visualizations-Chart.js-22d3ee?style=for-the-badge&logo=chartdotjs&logoColor=black)](https://github.com/soumya4504/Student-Pocket-Money-Management)
[![License](https://img.shields.io/badge/License-MIT-d946ef?style=for-the-badge)](https://github.com/soumya4504/Student-Pocket-Money-Management)

> 🌐 **Live Web Application**: [https://soumya4504.github.io/Student-Pocket-Money-Management/](https://soumya4504.github.io/Student-Pocket-Money-Management/)

An advanced, modern **Student Pocket Money Management & Financial Analytics Platform** designed to analyze allowance, categorize expenses, assess financial stress risks, and predict student spending behaviors with interactive visual charts and real-time AI scoring.

---

## 🌟 Key Modules & Features

### 1. 📊 Executive Dashboard
- **Real-Time KPIs**: Total Students Tracked, Average Monthly Allowance ($), Average Savings Rate (%), High-Risk Debt Alerts.
- **Interactive Visualizations**:
  - 🍩 **Budget Allocation Doughnut Chart**: Categorizes discretionary leisure, education supplies, daily transit, and savings pool.
  - 📊 **Spending Persona Distribution Bar Chart**: Visualizes behavior segments across all students.

### 2. ✍️ Smart Financial Entry & Live AI Predictor
- **Real-time Live Scoring Engine**: Calculates student **Financial Health Score (0–100 index)** dynamically as numbers are typed.
- **Predictive Behavior Matrix**:
  - **Saving Behavior**: *High consumption & low savings*, *Balanced consumption & savings*, or *Moderate*.
  - **Financial Stress Risk**: *Financially Secure Future* vs. *Future Financial Stress*.
  - **Spending Persona**: *Lifestyle-Oriented*, *Goal-Oriented*, or *Mixed Spending*.
  - **Future Forecast**: *Future Planner*, *Future Consumer Mindset*, or *Uncertain Future*.
- **Live Ratio Bars**: Instant feedback on spending and savings capacity.

### 3. 🔍 Student Records & Dossier Lookup
- **Instant Search & Filter**: Real-time filtering by Student ID, Persona, or Stress level.
- **Student Financial Dossier**: Click any student row to inspect detailed metrics and tailored **AI Financial Prescriptions** (debt repayment priority, 50/30/20 budget optimization).
- **Data Operations**: Add, delete, auto-fill samples, and persistent storage.

### 4. 💡 AI Budgeting Advisor
- **50/30/20 Student Rule**: 50% Needs, 30% Wants, 20% Savings framework.
- **Zero-Borrowing Debt Shield**: Practical debt prevention and 48-hour impulse purchase delay guidelines.
- **Cost Optimization Tips**: Academic supplies and transit discount strategies.

### 5. 📂 Dual-Platform Architecture
- **Interactive Web App**: Zero-install browser platform running on GitHub Pages.
- **JavaFX Desktop App**: Desktop Java GUI with native CSV file persistence (`students_data.csv`).
- **CSV Data Portability**: Export and import support across web and desktop platforms.

---

## 🎨 Theme & Design System

The application features a **Cyberpunk Neon Violet & Electric Cyan** aesthetic:
- **Obsidian Dark Surface**: `#080811` / `#16162d`
- **Neon Violet & Magenta**: `#8b5cf6` → `#d946ef`
- **Electric Cyan Accents**: `#22d3ee`
- **Neon Mint Health Highlights**: `#00f5a0`
- **Debt & Stress Alerts**: `#ff0055`

---

## 🚀 How to Run

### 🌐 Option 1: Live Cloud Version (Instant)
Access the live deployed web dashboard directly:
👉 **[https://soumya4504.github.io/Student-Pocket-Money-Management/](https://soumya4504.github.io/Student-Pocket-Money-Management/)**

---

### 💻 Option 2: Local Web Dashboard
1. Clone the repository:
   ```bash
   git clone https://github.com/soumya4504/Student-Pocket-Money-Management.git
   cd Student-Pocket-Money-Management
   ```
2. Double-click `Run-App.bat` or open `web/index.html` in any browser.
3. Or start a local server:
   ```bash
   python -m http.server 8085 --directory web
   ```
   Then visit `http://localhost:8085`

---

### ☕ Option 3: JavaFX Desktop Application

#### Running with JavaFX SDK
```bash
# Compile
javac --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -d . StudentPocketMoneyManagerApp.java

# Run
java --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml application.StudentPocketMoneyManagerApp
```

#### Running in Eclipse / IntelliJ IDEA
1. Open the project folder in Eclipse or IntelliJ IDEA.
2. Add the **JavaFX SDK** user library to your module build path.
3. Run `StudentPocketMoneyManagerApp.java`.

---

## 📁 Project Directory Structure

```text
Student-Pocket-Money-Management/
├── .github/workflows/
│   └── deploy.yml                      # Automated GitHub Pages CI/CD Pipeline
├── web/                                # Standalone Web Application
│   ├── index.html                      # Semantic Dashboard HTML5
│   ├── style.css                       # Cyberpunk Glassmorphic CSS3
│   └── app.js                          # Analytics Engine, Chart.js & CSV Store
├── StudentPocketMoneyManagerApp.java   # Main Unified JavaFX Platform
├── StudentPocketMoneyManagerSave.java  # Modernized JavaFX Data Entry Module
├── StudentDataLookup.java              # Modernized JavaFX Student Dossier Tool
├── style.css                           # JavaFX CSS Theme Stylesheet
├── Run-App.bat                         # Quick Launcher Script
├── .gitignore                          # Git configuration
└── README.md                           # Documentation
```

---

## 👥 Author
- **Soumya** ([@soumya4504](https://github.com/soumya4504))
- **Repository**: [https://github.com/soumya4504/Student-Pocket-Money-Management](https://github.com/soumya4504/Student-Pocket-Money-Management)
