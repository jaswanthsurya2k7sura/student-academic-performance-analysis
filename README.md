# Student Academic Performance and Result Analysis System
### Scenario 8 | Activity-Based Project Assessment (Academic Year 2026-27 ODD)

[![Java 21](https://img.shields.io/badge/Java-21%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Python 3.14](https://img.shields.io/badge/Python-3.14-3776AB?style=for-the-badge&logo=python&logoColor=white)](https://www.python.org/)
[![SQLite](https://img.shields.io/badge/SQLite-3-003B57?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.sqlite.org/)
[![MySQL Compatible](https://img.shields.io/badge/MySQL-8.0%2B-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Pandas](https://img.shields.io/badge/Pandas-Data%20Analysis-150458?style=for-the-badge&logo=pandas&logoColor=white)](https://pandas.pydata.org/)
[![Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)]()

---

## 🏛️ Academic Institutional Details

| Attribute | Details |
| :--- | :--- |
| **Institution** | **Velalar College of Engineering and Technology (Autonomous), Erode - 12** |
| **Department** | **Department of Information Technology** |
| **Class / Section** | **25IT3A** (Class: 25IT3A&B) |
| **Student Name** | **Jaswanth surya S** |
| **Register Number** | **732925ITR038** |
| **Assigned Scenario** | **Scenario 8: Student Academic Performance and Result Analysis System** |
| **Candidate Roll Nos** | 8, **38**, 68, 98 |
| **Academic Year** | 2026 - 27 (ODD Semester) |

---

## 📖 Problem Statement & Objectives

Educational institutions handle extensive volumes of academic data: student bio-data, course catalogs, continuous internal assessment (CIA) marks, and end-semester examination results. Maintaining and evaluating these records manually causes several issues:
- Labor-intensive result and Grade Point Average (GPA) calculations.
- Difficulty in pinpointing bottleneck courses with low pass rates.
- Inability to generate multi-dimensional department and section-level analytics in real time.

The **Student Academic Performance and Result Analysis System** is an end-to-end multi-tier solution developed to manage student marks, calculate results according to autonomous regulations, generate formatted academic reports, and visualize performance trends.

---

## 📚 Curriculum Alignment & Course Integration

This project integrates four core computer science and engineering disciplines:

| Course Code | Course Name | Integrated System Deliverables |
| :--- | :--- | :--- |
| **25ITT33** | **Software Engineering** | IEEE Std 830 SRS Document, Architectural Design, Use Case & Class Diagrams, Test Plan with Black-box/White-box Test Cases. |
| **25ITT31** | **Database Management Systems** | Normalized Relational Schema (1NF $\to$ BCNF), ER Diagram, DDL/DML SQL scripts, Views, Triggers, and Analytical Queries. |
| **25ITT32** | **Java Programming** | Object-Oriented Architecture, Custom Exception Handling, Singleton JDBC Connection, Collections, Java 21 Stream API & Lambdas, Console CLI. |
| **25ITC31** | **Foundations of Data Science** | Python database retrieval, Data Cleaning & Preprocessing (missing values, outlier IQR, feature engineering), and EDA Visualizations (Matplotlib & Seaborn). |

---

## 📁 Repository Directory Structure

```text
d:/project/
├── database/                                # Database Persistence Tier
│   ├── schema.sql                           # SQLite ANSI DDL (Tables, Indexes, Constraints)
│   ├── schema_mysql.sql                     # MySQL 8.0+ DDL (InnoDB, Foreign Keys, Engine)
│   ├── views_triggers.sql                   # SQL Views & Triggers for automated grading
│   ├── seed_data.sql                        # Comprehensive cohort dataset (20 students, 68 evaluations)
│   ├── queries.sql                          # Analytical reporting queries (Joins, Aggregations, Having)
│   ├── init_db.py                           # Python database initialization and verification script
│   └── student_performance.db               # Live compiled SQLite database
│
├── docs/                                    # Academic Engineering & Design Documentation
│   ├── SRS.md                               # Software Requirements Specification (IEEE 830 format)
│   ├── Design_Diagrams.md                   # System Architecture, Use Case, Class, ER & Sequence Diagrams
│   ├── Normalization.md                     # Formal Normalization Analysis (1NF, 2NF, 3NF, BCNF Proofs)
│   └── Testing_Documentation.md             # Verification, Validation, White-box & Black-box Test Cases
│
├── java_app/                                # Java Application Tier (25ITT32)
│   ├── lib/                                 # Runtime Dependencies (sqlite-jdbc, slf4j-api, slf4j-simple)
│   ├── bin/                                 # Compiled Java bytecode (.class files)
│   ├── sample_io/
│   │   └── Sample_Inputs_Outputs.md         # Verifiable CLI interaction logs and sample outputs
│   └── src/com/vcet/studentanalysis/
│       ├── Main.java                        # Application Entry Point
│       ├── model/                           # Entity Models (Person, Student, Department, Subject, Mark)
│       ├── dao/                             # Data Access Object Interfaces & JDBC Implementations
│       ├── service/                         # Academic Evaluation Service & Streams GPA Engine
│       ├── exception/                       # Custom Exceptions (StudentNotFound, InvalidMark, DatabaseOp)
│       ├── util/                            # DBConnection (Singleton), TableFormatter, InputValidator
│       └── ui/                              # Interactive Console CLI (ConsoleApp)
│
├── python_analysis/                         # Data Science Tier (25ITC31)
│   ├── data/                                # Cleaned CSV exports (preprocessed datasets)
│   ├── db_extractor.py                      # SQL database extraction to Pandas DataFrames
│   ├── data_preprocessing.py                # Data Cleaning, IQR outlier detection, feature engineering
│   ├── visualizer.py                        # Statistical charts generation (Matplotlib & Seaborn)
│   ├── run_analysis.py                      # End-to-end pipeline automation runner
│   └── Student_Performance_Analysis.ipynb   # Interactive Jupyter Notebook for EDA
│
├── visualizations/                          # Exported Publication-Quality Charts (PNG 300 DPI)
│   ├── 01_subject_pass_percentage.png       # Subject pass rates with 85% threshold
│   ├── 02_grade_distribution.png            # Department letter grade distribution (Donut chart)
│   ├── 03_marks_distribution_kde.png        # Total marks histogram & Kernel Density Estimation
│   ├── 04_subject_marks_boxplot.png         # Subject score dispersion & quartiles (Boxplot)
│   ├── 05_correlation_heatmap.png           # Pearson correlation matrix across assessments
│   ├── 06_section_performance_comparison.png# Section 25IT3A vs 25IT3B comparative analysis
│   └── 07_top_rankers_gpa.png               # Merit Leaderboard highlighting Jaswanth surya S
│
└── README.md                                # Project Documentation & Execution Guide
```

---

## ⚙️ Institutional Evaluation & Grading Scheme

In accordance with autonomous college regulations:
- **Maximum Internal Assessment (CIA):** 40.0 Marks
- **Maximum End Semester Examination (ESE):** 60.0 Marks
- **Total Course Marks:** 100.0 Marks
- **Passing Rules:**
  - Minimum **45%** in Semester Exam ($\ge 27.0 / 60$)
  - Minimum **50%** in Overall Total Marks ($\ge 50.0 / 100$)
- **Letter Grades and Grade Points:**
  - **O (Outstanding):** $90 - 100$ | Grade Point: **10**
  - **A+ (Excellent):** $80 - 89$ | Grade Point: **9**
  - **A (Very Good):** $70 - 79$ | Grade Point: **8**
  - **B+ (Good):** $60 - 69$ | Grade Point: **7**
  - **B (Average):** $50 - 59$ | Grade Point: **6**
  - **RA (Re-Appear / Fail):** $< 50$ or External $< 27$ | Grade Point: **0**
- **Grade Point Average (GPA) Formulation:**
  $$\text{GPA} = \frac{\sum_{i=1}^{n} (\text{Grade Point}_i \times \text{Credits}_i)}{\sum_{i=1}^{n} \text{Credits}_i}$$

---

## 🚀 Execution & Setup Instructions

### 1. Prerequisites
- **Java Development Kit:** OpenJDK 21 LTS (or Java 17+)
- **Python:** Python 3.10+ (with `pandas`, `numpy`, `matplotlib`, `seaborn`)

---

### 2. Database Initialization
To create and populate the database from scratch:
```powershell
python d:\project\database\init_db.py
```
*Output:*
```text
Successfully executed: schema.sql
Successfully executed: views_triggers.sql
Successfully executed: seed_data.sql
Database initialization complete and verified!
```

---

### 3. Running the Presentation-Ready Web Application
Start the unified full-stack academic web portal:
```powershell
python d:\project\web_app\app.py
```
Open your browser and navigate to:
```text
http://127.0.0.1:5000
```
This launches the complete presentation portal featuring all 10 interactive modules (Dashboard, Students, Subjects, Marks, Results, Python Data Science, DBMS SQL Console, Java 21 Engine Runner, and Subject Coverage Faculty Review).

---

### 4. Compiling and Running the Java Application
Open PowerShell / Command Prompt:

#### Compile Java Code:
```powershell
cd d:\project\java_app
javac -cp "lib/*" -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

#### Run Interactive Console:
```powershell
cd d:\project\java_app
java -cp "bin;lib/*" com.vcet.studentanalysis.Main
```

#### Run Java Engine Bridge Directly:
```powershell
java -cp "bin;lib/*" com.vcet.studentanalysis.JavaEngineRunner --action=streams-analytics
```

---

### 5. Running the Python Data Science & Visualization Pipeline
Execute the end-to-end data processing and charting script:
```powershell
python d:\project\python_analysis\run_analysis.py
```
All generated charts are automatically exported to `visualizations/`.

---

## 📊 Visualizations Gallery

| Chart Title | File Name | Academic Insight |
| :--- | :--- | :--- |
| **Subject Pass Rate & Low Pass Alerts** | `01_subject_pass_percentage.png` | Identifies subjects below institutional target ($85\%$) for academic counseling. |
| **Grade Distribution** | `02_grade_distribution.png` | Percentage breakdown of letter grades across all evaluations. |
| **Marks Distribution & KDE** | `03_marks_distribution_kde.png` | Normal distribution fit, skewness, and class mean score ($73.0$). |
| **Subject Score Dispersion** | `04_subject_marks_boxplot.png` | Quartile spreads, medians, and variance across semester courses. |
| **Assessment Correlation Heatmap** | `05_correlation_heatmap.png` | Positive correlation between continuous assessment and final exam score. |
| **Section Comparative Analysis** | `06_section_performance_comparison.png` | Performance comparison between Section 25IT3A and Section 25IT3B. |
| **Merit Leaderboard** | `07_top_rankers_gpa.png` | Top departmental rankers highlighting **Jaswanth surya S (GPA: 10.0)**. |

---

## 🧪 Testing Verification Summary

All test cases specified in the test plan have been executed:
- **Unit Test Cases (Boundary Value & Equivalence Partitioning):** 100% Passed.
- **Integration Test Cases (JDBC Persistence & SQL Triggers):** 100% Passed.
- **System Validation Test Cases (Reports & Charts):** 100% Passed.

Detailed documentation is available at [`docs/Testing_Documentation.md`](file:///d:/project/docs/Testing_Documentation.md).

---

## 👨‍🎓 Developer Information
- **Name:** Jaswanth surya S
- **Register Number:** 732925ITR038
- **Class / Section:** 25IT3A
- **Department:** Information Technology
- **Institution:** Velalar College of Engineering and Technology, Erode - 12
