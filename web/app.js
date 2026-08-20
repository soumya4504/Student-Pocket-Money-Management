// Student Pocket Money Manager - Financial Analytics & Prediction Engine

// Default Sample Records
const INITIAL_STUDENT_DATA = [
  {
    studentId: "STU-1001",
    pocketMoney: 500,
    expense: 320,
    savings: 180,
    educationMoney: 80,
    transportCost: 40,
    savingBehavior: "Moderate",
    borrowMoney: 0,
    financialStress: "Financially Secure Future",
    spendingType: "Mixed Spending",
    future: "Future Planner"
  },
  {
    studentId: "STU-1002",
    pocketMoney: 400,
    expense: 390,
    savings: 10,
    educationMoney: 50,
    transportCost: 30,
    savingBehavior: "High consumption & low savings",
    borrowMoney: 50,
    financialStress: "Future Financial Stress",
    spendingType: "Lifestyle-Oriented Student",
    future: "Future Consumer Mindset"
  },
  {
    studentId: "STU-1003",
    pocketMoney: 600,
    expense: 300,
    savings: 300,
    educationMoney: 120,
    transportCost: 60,
    savingBehavior: "Balanced consumption & savings",
    borrowMoney: 0,
    financialStress: "Financially Secure Future",
    spendingType: "Goal-Oriented Student",
    future: "Future Planner"
  },
  {
    studentId: "STU-1004",
    pocketMoney: 450,
    expense: 350,
    savings: 100,
    educationMoney: 70,
    transportCost: 50,
    savingBehavior: "Moderate",
    borrowMoney: 0,
    financialStress: "Financially Secure Future",
    spendingType: "Mixed Spending",
    future: "Uncertain Future"
  },
  {
    studentId: "STU-1005",
    pocketMoney: 350,
    expense: 340,
    savings: 10,
    educationMoney: 30,
    transportCost: 20,
    savingBehavior: "High consumption & low savings",
    borrowMoney: 25,
    financialStress: "Future Financial Stress",
    spendingType: "Lifestyle-Oriented Student",
    future: "Future Consumer Mindset"
  }
];

class PocketMoneyApp {
  constructor() {
    this.students = this.loadData();
    this.pieChart = null;
    this.barChart = null;
    this.selectedStudentId = null;

    this.initElements();
    this.initTabs();
    this.initLiveCalculator();
    this.initForm();
    this.initSearch();
    this.initExportImport();
    this.renderAll();
  }

  loadData() {
    const saved = localStorage.getItem("students_pocket_money_data");
    if (saved) {
      try {
        return JSON.parse(saved);
      } catch (e) {
        console.error("Failed to parse localStorage data", e);
      }
    }
    return [...INITIAL_STUDENT_DATA];
  }

  saveData() {
    localStorage.setItem("students_pocket_money_data", JSON.stringify(this.students));
  }

  initElements() {
    // KPI elements
    this.kpiTotalStudents = document.getElementById("kpiTotalStudents");
    this.kpiAvgPocketMoney = document.getElementById("kpiAvgPocketMoney");
    this.kpiAvgSavingsRate = document.getElementById("kpiAvgSavingsRate");
    this.kpiStressCount = document.getElementById("kpiStressCount");

    // Table elements
    this.tableBody = document.getElementById("studentsTableBody");
    this.tableRecordCount = document.getElementById("tableRecordCount");
    this.dossierBody = document.getElementById("dossierBody");

    // Form inputs
    this.form = document.getElementById("studentEntryForm");
    this.idInput = document.getElementById("studentId");
    this.pmInput = document.getElementById("pocketMoney");
    this.expInput = document.getElementById("expense");
    this.savInput = document.getElementById("savings");
    this.eduInput = document.getElementById("educationMoney");
    this.trnInput = document.getElementById("transportCost");
    this.borInput = document.getElementById("borrowMoney");

    // Live predictor indicators
    this.liveScore = document.getElementById("liveHealthScore");
    this.liveRating = document.getElementById("liveHealthRating");
    this.liveSavingBehavior = document.getElementById("liveSavingBehavior");
    this.liveFinancialStress = document.getElementById("liveFinancialStress");
    this.liveSpendingType = document.getElementById("liveSpendingType");
    this.liveFuture = document.getElementById("liveFuture");
    this.liveExpenseRatio = document.getElementById("liveExpenseRatio");
    this.liveSavingsRatio = document.getElementById("liveSavingsRatio");
    this.liveExpenseBar = document.getElementById("liveExpenseBar");
    this.liveSavingsBar = document.getElementById("liveSavingsBar");
  }

  initTabs() {
    const tabBtns = document.querySelectorAll(".tab-btn");
    const tabContents = document.querySelectorAll(".tab-content");

    tabBtns.forEach(btn => {
      btn.addEventListener("click", () => {
        tabBtns.forEach(b => b.classList.remove("active"));
        tabContents.forEach(c => c.classList.remove("active"));

        btn.classList.add("active");
        const target = document.getElementById(btn.dataset.tab);
        if (target) {
          target.classList.add("active");
          if (btn.dataset.tab === "dashboard-tab") {
            this.updateCharts();
          }
        }
      });
    });
  }

  calculatePredictions(pm, exp, sav, edu, trn, bor) {
    if (!pm || pm <= 0) {
      return {
        spendingPercent: 0,
        savingPercent: 0,
        savingBehavior: "Awaiting Input",
        financialStress: "Awaiting Input",
        spendingType: "Awaiting Input",
        future: "Awaiting Input",
        healthScore: 100,
        rating: "Awaiting Input",
        ratingColor: "var(--text-muted)"
      };
    }

    const spendingPercent = (exp / pm) * 100;
    const savingPercent = (sav / pm) * 100;
    const discretionaryPercent = ((exp - (edu + trn)) / pm) * 100;
    const nonDiscretionaryPercent = ((edu + trn) / pm) * 100;

    let savingBehavior = "Moderate";
    let future = "Uncertain Future";
    if (spendingPercent > 80) {
      savingBehavior = "High consumption & low savings";
      future = "Future Consumer Mindset";
    } else if (savingPercent > 40) {
      savingBehavior = "Balanced consumption & savings";
      future = "Future Planner";
    }

    let financialStress = "Financially Secure Future";
    if (bor > 0 || exp > pm * 1.1) {
      financialStress = "Future Financial Stress";
    }

    let spendingType = "Mixed Spending";
    if (discretionaryPercent > 50) {
      spendingType = "Lifestyle-Oriented Student";
    } else if (nonDiscretionaryPercent > 10) {
      spendingType = "Goal-Oriented Student";
    }

    let healthScore = 100;
    if (bor > 0) healthScore -= 30;
    if (spendingPercent > 90) healthScore -= 30;
    else if (spendingPercent > 70) healthScore -= 15;
    if (savingPercent >= 30) healthScore += 10;
    else if (savingPercent < 10) healthScore -= 20;
    healthScore = Math.max(0, Math.min(100, Math.round(healthScore)));

    let rating = "Excellent";
    let ratingColor = "var(--emerald)";
    if (healthScore < 40) {
      rating = "Critical Risk";
      ratingColor = "var(--rose)";
    } else if (healthScore < 60) {
      rating = "Warning Alert";
      ratingColor = "var(--amber)";
    } else if (healthScore < 80) {
      rating = "Good Health";
      ratingColor = "var(--cyan)";
    }

    return {
      spendingPercent,
      savingPercent,
      discretionaryPercent,
      nonDiscretionaryPercent,
      savingBehavior,
      financialStress,
      spendingType,
      future,
      healthScore,
      rating,
      ratingColor
    };
  }

  initLiveCalculator() {
    const inputs = [this.pmInput, this.expInput, this.savInput, this.eduInput, this.trnInput, this.borInput];
    const updateLive = () => {
      const pm = parseFloat(this.pmInput.value) || 0;
      const exp = parseFloat(this.expInput.value) || 0;
      const sav = parseFloat(this.savInput.value) || 0;
      const edu = parseFloat(this.eduInput.value) || 0;
      const trn = parseFloat(this.trnInput.value) || 0;
      const bor = parseFloat(this.borInput.value) || 0;

      const p = this.calculatePredictions(pm, exp, sav, edu, trn, bor);

      this.liveScore.textContent = p.healthScore;
      this.liveScore.style.color = p.ratingColor;
      this.liveRating.textContent = p.rating;
      this.liveRating.style.color = p.ratingColor;

      this.liveSavingBehavior.textContent = p.savingBehavior;
      this.liveFinancialStress.textContent = p.financialStress;
      this.liveSpendingType.textContent = p.spendingType;
      this.liveFuture.textContent = p.future;

      if (p.financialStress.includes("Stress")) {
        this.liveFinancialStress.className = "pill-badge pill-danger";
      } else {
        this.liveFinancialStress.className = "pill-badge pill-success";
      }

      this.liveExpenseRatio.textContent = Math.round(p.spendingPercent) + "%";
      this.liveSavingsRatio.textContent = Math.round(p.savingPercent) + "%";

      this.liveExpenseBar.style.width = Math.min(100, Math.round(p.spendingPercent)) + "%";
      this.liveSavingsBar.style.width = Math.min(100, Math.round(p.savingPercent)) + "%";
    };

    inputs.forEach(input => input.addEventListener("input", updateLive));
  }

  initForm() {
    this.form.addEventListener("submit", e => {
      e.preventDefault();
      const studentId = this.idInput.value.trim();
      const pm = parseFloat(this.pmInput.value) || 0;
      const exp = parseFloat(this.expInput.value) || 0;
      const sav = parseFloat(this.savInput.value) || 0;
      const edu = parseFloat(this.eduInput.value) || 0;
      const trn = parseFloat(this.trnInput.value) || 0;
      const bor = parseFloat(this.borInput.value) || 0;

      if (!studentId || pm <= 0) {
        alert("Please enter a valid Student ID and Pocket Money amount.");
        return;
      }

      const p = this.calculatePredictions(pm, exp, sav, edu, trn, bor);

      const newRecord = {
        studentId,
        pocketMoney: pm,
        expense: exp,
        savings: sav,
        educationMoney: edu,
        transportCost: trn,
        savingBehavior: p.savingBehavior,
        borrowMoney: bor,
        financialStress: p.financialStress,
        spendingType: p.spendingType,
        future: p.future
      };

      // Check if student ID already exists, update or add
      const existingIdx = this.students.findIndex(s => s.studentId.toLowerCase() === studentId.toLowerCase());
      if (existingIdx !== -1) {
        this.students[existingIdx] = newRecord;
      } else {
        this.students.unshift(newRecord);
      }

      this.saveData();
      this.renderAll();
      this.form.reset();

      // Show success alert
      alert(`Student profile for "${studentId}" saved successfully!`);
    });

    document.getElementById("clearFormBtn").addEventListener("click", () => {
      this.form.reset();
      const evt = new Event("input");
      this.pmInput.dispatchEvent(evt);
    });

    document.getElementById("autoFillBtn").addEventListener("click", () => {
      this.idInput.value = "STU-" + Math.floor(1000 + Math.random() * 9000);
      this.pmInput.value = 550;
      this.expInput.value = 380;
      this.savInput.value = 170;
      this.eduInput.value = 90;
      this.trnInput.value = 45;
      this.borInput.value = 0;

      const evt = new Event("input");
      this.pmInput.dispatchEvent(evt);
    });
  }

  initSearch() {
    const searchInput = document.getElementById("recordSearchInput");
    searchInput.addEventListener("input", () => {
      const query = searchInput.value.trim().toLowerCase();
      this.renderTable(query);
    });
  }

  initExportImport() {
    // Export to CSV
    document.getElementById("exportCsvBtn").addEventListener("click", () => {
      if (this.students.length === 0) {
        alert("No records to export.");
        return;
      }

      let csv = "studentId,pocketMoneyGiven,expense,savings,educationMoney,transportCost,SavingBehavior,borrowMoneyAmount,FinancialStress,SpendingType,Future\n";
      this.students.forEach(s => {
        csv += `${s.studentId},${s.pocketMoney},${s.expense},${s.savings},${s.educationMoney},${s.transportCost},"${s.savingBehavior}",${s.borrowMoney},"${s.financialStress}","${s.spendingType}","${s.future}"\n`;
      });

      const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
      const url = URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "students_data.csv";
      a.click();
      URL.revokeObjectURL(url);
    });

    // Import from CSV
    document.getElementById("importCsvInput").addEventListener("change", e => {
      const file = e.target.files[0];
      if (!file) return;

      const reader = new FileReader();
      reader.onload = evt => {
        const lines = evt.target.result.split(/\r?\n/).filter(line => line.trim().length > 0);
        if (lines.length <= 1) {
          alert("CSV file is empty or missing headers.");
          return;
        }

        const newRecords = [];
        for (let i = 1; i < lines.length; i++) {
          const parts = lines[i].split(",").map(p => p.replace(/^"|"$/g, "").trim());
          if (parts.length >= 11) {
            newRecords.push({
              studentId: parts[0],
              pocketMoney: parseFloat(parts[1]) || 0,
              expense: parseFloat(parts[2]) || 0,
              savings: parseFloat(parts[3]) || 0,
              educationMoney: parseFloat(parts[4]) || 0,
              transportCost: parseFloat(parts[5]) || 0,
              savingBehavior: parts[6],
              borrowMoney: parseFloat(parts[7]) || 0,
              financialStress: parts[8],
              spendingType: parts[9],
              future: parts[10]
            });
          }
        }

        if (newRecords.length > 0) {
          this.students = newRecords;
          this.saveData();
          this.renderAll();
          alert(`Successfully imported ${newRecords.length} records!`);
        } else {
          alert("Could not parse records from CSV. Please verify column structure.");
        }
      };
      reader.readAsText(file);
      e.target.value = "";
    });

    // Sample data button
    document.getElementById("sampleDataBtn").addEventListener("click", () => {
      this.students = [...INITIAL_STUDENT_DATA];
      this.saveData();
      this.renderAll();
      alert("Sample student data reloaded!");
    });
  }

  renderAll() {
    this.renderKPIs();
    this.renderTable();
    this.updateCharts();
  }

  renderKPIs() {
    const total = this.students.length;
    this.kpiTotalStudents.textContent = total;

    if (total === 0) {
      this.kpiAvgPocketMoney.textContent = "$0.00";
      this.kpiAvgSavingsRate.textContent = "0%";
      this.kpiStressCount.textContent = "0";
      return;
    }

    let totalPm = 0;
    let totalSav = 0;
    let stressCount = 0;

    this.students.forEach(s => {
      totalPm += s.pocketMoney;
      totalSav += s.savings;
      if (s.financialStress.toLowerCase().includes("stress") || s.borrowMoney > 0) {
        stressCount++;
      }
    });

    const avgPm = totalPm / total;
    const avgSavRate = totalPm > 0 ? (totalSav / totalPm) * 100 : 0;

    this.kpiAvgPocketMoney.textContent = "$" + avgPm.toFixed(2);
    this.kpiAvgSavingsRate.textContent = Math.round(avgSavRate) + "%";
    this.kpiStressCount.textContent = stressCount;
  }

  renderTable(query = "") {
    this.tableBody.innerHTML = "";
    const filtered = this.students.filter(s => {
      if (!query) return true;
      return (
        s.studentId.toLowerCase().includes(query) ||
        s.spendingType.toLowerCase().includes(query) ||
        s.financialStress.toLowerCase().includes(query) ||
        s.savingBehavior.toLowerCase().includes(query)
      );
    });

    this.tableRecordCount.textContent = `Showing ${filtered.length} of ${this.students.length} records`;

    if (filtered.length === 0) {
      this.tableBody.innerHTML = `<tr><td colspan="7" style="text-align:center; color: var(--text-dim); padding: 2rem;">No matching student records found.</td></tr>`;
      return;
    }

    filtered.forEach(s => {
      const tr = document.createElement("tr");
      if (this.selectedStudentId === s.studentId) {
        tr.classList.add("selected");
      }

      const stressClass = s.financialStress.includes("Stress") ? "pill-danger" : "pill-success";

      tr.innerHTML = `
        <td><strong style="color:var(--cyan);">${s.studentId}</strong></td>
        <td>$${s.pocketMoney}</td>
        <td>$${s.expense}</td>
        <td>$${s.savings}</td>
        <td><span class="pill-badge pill-info">${s.spendingType}</span></td>
        <td><span class="pill-badge ${stressClass}">${s.financialStress}</span></td>
        <td>
          <button class="btn btn-sm btn-danger delete-btn" data-id="${s.studentId}">🗑️</button>
        </td>
      `;

      tr.addEventListener("click", e => {
        if (e.target.classList.contains("delete-btn")) return;
        document.querySelectorAll("#studentsTableBody tr").forEach(r => r.classList.remove("selected"));
        tr.classList.add("selected");
        this.selectedStudentId = s.studentId;
        this.showDossier(s);
      });

      const delBtn = tr.querySelector(".delete-btn");
      delBtn.addEventListener("click", e => {
        e.stopPropagation();
        if (confirm(`Are you sure you want to delete record for ${s.studentId}?`)) {
          this.students = this.students.filter(item => item.studentId !== s.studentId);
          if (this.selectedStudentId === s.studentId) {
            this.selectedStudentId = null;
            this.dossierBody.innerHTML = `<div class="dossier-placeholder">👈 Click on any student row in the table to inspect detailed financial analytics.</div>`;
          }
          this.saveData();
          this.renderAll();
        }
      });

      this.tableBody.appendChild(tr);
    });
  }

  showDossier(s) {
    const savRate = s.pocketMoney > 0 ? Math.round((s.savings / s.pocketMoney) * 100) : 0;
    const expRate = s.pocketMoney > 0 ? Math.round((s.expense / s.pocketMoney) * 100) : 0;
    const discretionary = Math.max(0, s.expense - (s.educationMoney + s.transportCost));

    let adviceHtml = "";
    if (s.borrowMoney > 0) {
      adviceHtml += `<p style="color:var(--rose); font-weight:700;">⚠️ URGENT DEBT ALERT: Student has $${s.borrowMoney} in borrowed debt. Pay off immediately before non-essential purchases.</p>`;
    }
    if (savRate >= 40) {
      adviceHtml += `<p>🌟 <strong>Star Saver:</strong> Saving ${savRate}% of pocket money! Recommended to start a micro-investment pot.</p>`;
    } else if (savRate < 15) {
      adviceHtml += `<p>💡 <strong>Savings Boost:</strong> Current savings is ${savRate}%. Aim for at least 20% ($${Math.round(s.pocketMoney * 0.2)}) per month.</p>`;
    }

    if (s.spendingType.includes("Lifestyle")) {
      adviceHtml += `<p>🛍️ <strong>Lifestyle Leak:</strong> High discretionary spending ($${discretionary}). Apply the 48-Hour impulse purchase rule.</p>`;
    } else if (s.spendingType.includes("Goal")) {
      adviceHtml += `<p>📚 <strong>Education First:</strong> Productive investment in books ($${s.educationMoney}) and transport ($${s.transportCost}).</p>`;
    }

    this.dossierBody.innerHTML = `
      <div class="dossier-grid">
        <div class="dossier-item">
          <div class="dossier-label">STUDENT ID</div>
          <div class="dossier-val" style="color:var(--cyan);">${s.studentId}</div>
        </div>
        <div class="dossier-item">
          <div class="dossier-label">MONTHLY ALLOWANCE</div>
          <div class="dossier-val">$${s.pocketMoney}</div>
        </div>
        <div class="dossier-item">
          <div class="dossier-label">TOTAL EXPENSE</div>
          <div class="dossier-val">$${s.expense} (${expRate}%)</div>
        </div>
        <div class="dossier-item">
          <div class="dossier-label">MONTHLY SAVINGS</div>
          <div class="dossier-val" style="color:var(--emerald);">$${s.savings} (${savRate}%)</div>
        </div>
        <div class="dossier-item">
          <div class="dossier-label">EDUCATION SUPPLIES</div>
          <div class="dossier-val">$${s.educationMoney}</div>
        </div>
        <div class="dossier-item">
          <div class="dossier-label">TRANSPORT TRANSIT</div>
          <div class="dossier-val">$${s.transportCost}</div>
        </div>
        <div class="dossier-item">
          <div class="dossier-label">BORROWED DEBT</div>
          <div class="dossier-val" style="color:${s.borrowMoney > 0 ? 'var(--rose)' : 'var(--emerald)'};">$${s.borrowMoney}</div>
        </div>
        <div class="dossier-item">
          <div class="dossier-label">FUTURE FORECAST</div>
          <div class="dossier-val" style="color:var(--purple); font-size:0.8rem;">${s.future}</div>
        </div>
      </div>

      <div class="dossier-advice">
        <h4>🎯 Personalized AI Financial Prescription</h4>
        ${adviceHtml}
      </div>
    `;
  }

  updateCharts() {
    if (typeof Chart === "undefined") return;

    let totalDiscretionary = 0;
    let totalEducation = 0;
    let totalTransport = 0;
    let totalSavings = 0;

    let lifestyleCount = 0;
    let goalCount = 0;
    let mixedCount = 0;

    this.students.forEach(s => {
      const disc = Math.max(0, s.expense - (s.educationMoney + s.transportCost));
      totalDiscretionary += disc;
      totalEducation += s.educationMoney;
      totalTransport += s.transportCost;
      totalSavings += s.savings;

      if (s.spendingType.includes("Lifestyle")) lifestyleCount++;
      else if (s.spendingType.includes("Goal")) goalCount++;
      else mixedCount++;
    });

    // 1. Pie Chart
    const pieCtx = document.getElementById("expensePieChart");
    if (pieCtx) {
      if (this.pieChart) {
        this.pieChart.destroy();
      }
      this.pieChart = new Chart(pieCtx, {
        type: "doughnut",
        data: {
          labels: ["Discretionary / Leisure", "Education & Supplies", "Daily Transport", "Savings Pool"],
          datasets: [{
            data: [totalDiscretionary, totalEducation, totalTransport, totalSavings],
            backgroundColor: ["#d946ef", "#8b5cf6", "#22d3ee", "#00f5a0"],
            borderColor: "#16162d",
            borderWidth: 3
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: "bottom",
              labels: { color: "#a5b4fc", font: { family: "Plus Jakarta Sans", size: 11, weight: "bold" } }
            }
          }
        }
      });
    }

    // 2. Bar Chart
    const barCtx = document.getElementById("personaBarChart");
    if (barCtx) {
      if (this.barChart) {
        this.barChart.destroy();
      }
      this.barChart = new Chart(barCtx, {
        type: "bar",
        data: {
          labels: ["Lifestyle-Oriented", "Goal-Oriented", "Mixed Spending"],
          datasets: [{
            label: "Students Count",
            data: [lifestyleCount, goalCount, mixedCount],
            backgroundColor: ["#ff0055", "#00f5a0", "#8b5cf6"],
            borderRadius: 8
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { display: false }
          },
          scales: {
            x: {
              ticks: { color: "#a5b4fc", font: { family: "Plus Jakarta Sans", weight: "bold" } },
              grid: { color: "rgba(139, 92, 246, 0.1)" }
            },
            y: {
              beginAtZero: true,
              ticks: { precision: 0, color: "#a5b4fc", font: { family: "Plus Jakarta Sans" } },
              grid: { color: "rgba(139, 92, 246, 0.1)" }
            }
          }
        }
      });
    }
  }
}

// Initialize on DOM load
document.addEventListener("DOMContentLoaded", () => {
  window.app = new PocketMoneyApp();
});
