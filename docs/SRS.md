# Software Requirements Specification (SRS)
## Student Academic Performance and Result Analysis System

**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Department of Information Technology  
**Academic Year:** 2026-27 (ODD) | **Class:** 25IT3A&B  
**Course Code & Title:** 25ITT33 - Software Engineering  
**Scenario Assigned:** Scenario 8  
**Author:** Jaswanth surya S  
**Register Number:** 732925ITR038 | **Class / Section:** 25IT3A  

---

## 1. Introduction

### 1.1 Purpose
This Software Requirements Specification (SRS) document defines the functional and non-functional requirements for the **Student Academic Performance and Result Analysis System (Scenario 8)**. The document conforms to the IEEE Std 830-1998 standard and serves as the baseline for system architecture, database design, Java OOP implementation, and Python data analytics.

### 1.2 Scope of the System
The software system is developed to automate the management and statistical evaluation of student academic records at Velalar College of Engineering and Technology. The system replaces manual calculation of results by:
- Storing student biographical and departmental information.
- Maintaining curriculum subject information and credit distributions.
- Recording continuous internal assessments (CIA) and semester end examinations.
- Calculating total marks, letter grades, grade points (10-point scale), and pass/fail status in accordance with autonomous college regulations.
- Generating student-level marksheets, subject-level low pass rate alerts (< 85%), and department-level summaries.
- Performing data preprocessing and producing visualization charts using Python.

### 1.3 Definitions, Acronyms, and Abbreviations
| Term | Expansion / Definition |
| :--- | :--- |
| **SRS** | Software Requirements Specification |
| **DBMS** | Database Management System |
| **CIA** | Continuous Internal Assessment (Max 40 marks) |
| **ESE** | End Semester Examination (Max 60 marks) |
| **GPA** | Grade Point Average |
| **EDA** | Exploratory Data Analysis |
| **RA** | Re-Appear (Failed subject status) |
| **DAO** | Data Access Object Pattern |
| **1NF / 2NF / 3NF / BCNF** | Normalization Forms (First, Second, Third, Boyce-Codd) |

### 1.4 References
1. IEEE Recommended Practice for Software Requirements Specifications (IEEE Std 830-1998).
2. VCET Curriculum & Autonomous Examination Regulations (2026-27).
3. Course Syllabus for 25ITT33 Software Engineering, 25ITT31 DBMS, 25ITT32 Java Programming, and 25ITC31 Foundations of Data Science.

---

## 2. Overall Description

### 2.1 Product Perspective
The system operates as an integrated multi-tier software system:
1. **Data Persistence Tier (DBMS):** Relational database (SQLite / MySQL) storing normalized student, subject, and marks tables.
2. **Business & Application Tier (Java):** High-performance, object-oriented console application using JDBC, DAO pattern, and Java Streams API.
3. **Data Science Tier (Python):** Python automated ETL pipeline utilizing Pandas, NumPy, Matplotlib, and Seaborn for descriptive statistics, outlier detection, and data visualization.

### 2.2 User Characteristics
- **Department Head (HOD) & Academic Coordinators:** Monitor department-wide pass percentages, review subject pass rates, and flag low-performing courses.
- **Faculty / Course Instructors:** Enter internal and semester exam scores, review class performance, and monitor remedial students.
- **Examination Cell Officers:** Generate authentic student marksheets and merit rank lists.

### 2.3 General Constraints
1. The system must run on standard Java 17+ / 21 LTS runtime and Python 3.10+ environments.
2. Relational integrity must be enforced via foreign keys with cascade rules.
3. Internal marks must not exceed 40.0; External marks must not exceed 60.0; Total marks must be within [0, 100].
4. An external mark of at least 27/60 (45%) and a combined total of at least 50/100 (50%) is mandatory to qualify for a 'PASS'.

---

## 3. Specific Requirements

### 3.1 External Interface Requirements
- **User Interface:** Interactive, menu-driven CLI formatted with clear tabular ASCII tables.
- **Database Interface:** Java Database Connectivity (JDBC) standard driver communicating via SQL queries, PreparedStatements, and database views.
- **Python Connector Interface:** Python `sqlite3` and `pandas.read_sql_query` drivers extracting live records into DataFrames.

### 3.2 Functional Requirements

#### FR-1: Student Information Management
- **FR-1.1:** The system shall capture and maintain student details including Register Number (Primary Key), Full Name, Department ID, Year of Study, Section, Email, and Phone Number.
- **FR-1.2:** The system shall allow searching and viewing individual student records by register number.

#### FR-2: Subject Curriculum Management
- **FR-2.1:** The system shall maintain course subjects with Subject Code (Primary Key), Subject Name, Department ID, Semester, Credits, Maximum Internal Marks (40), and Maximum External Marks (60).

#### FR-3: Assessment and Marks Recording
- **FR-3.1:** The system shall record Continuous Internal Assessment marks (0.0 to 40.0) and Semester Exam marks (0.0 to 60.0) for each student-subject combination.
- **FR-3.2:** Input validation shall immediately reject out-of-range marks or unregistered student/subject identifiers.

#### FR-4: Result Computation and Grading Rules
- **FR-4.1:** The system shall calculate `total_marks = internal_marks + external_marks`.
- **FR-4.2:** The system shall assign letter grades and grade points according to the institutional 10-point scale:
  - Total $\ge 90$ and External $\ge 27 \implies$ **O** (Grade Point: 10)
  - Total $\ge 80$ and External $\ge 27 \implies$ **A+** (Grade Point: 9)
  - Total $\ge 70$ and External $\ge 27 \implies$ **A** (Grade Point: 8)
  - Total $\ge 60$ and External $\ge 27 \implies$ **B+** (Grade Point: 7)
  - Total $\ge 50$ and External $\ge 27 \implies$ **B** (Grade Point: 6)
  - Total $< 50$ or External $< 27 \implies$ **RA** (Grade Point: 0, Fail)
- **FR-4.3:** The system shall compute semester GPA using credit-weighted average:
  $$\text{GPA} = \frac{\sum (\text{Grade Point} \times \text{Credits})}{\sum \text{Credits}}$$

#### FR-5: Academic Reporting
- **FR-5.1 Student-wise Report:** Produce consolidated semester marksheet detailing individual subject scores, credits, letter grades, grand total, and overall GPA.
- **FR-5.2 Subject-wise Report:** Compute total appeared, passed, failed, pass percentage, average score, and highlight subjects with pass rate $< 85\%$.
- **FR-5.3 Department-wise Report:** Aggregate total student enrollments, paper evaluations, overall department average, and pass rate.

#### FR-6: Python Data Retrieval & Visualizations
- **FR-6.1:** Connect to SQLite database, extract marks and summary tables into Pandas DataFrames, and export clean CSV datasets.
- **FR-6.2:** Perform missing value detection, duplicate checks, and outlier detection (IQR).
- **FR-6.3:** Generate 7 publication-quality visual charts:
  1. Subject-Wise Pass Percentage (with 85% threshold line)
  2. Grade Distribution (Donut Chart)
  3. Marks Distribution (Histogram & KDE curve)
  4. Subject Score Dispersion (Seaborn Box Plot)
  5. Correlation Heatmap (Assessment metrics correlation)
  6. Section-wise Comparative Analysis (25IT3A vs 25IT3B)
  7. Merit Leaderboard (Top rankers by GPA)

---

## 4. Non-Functional Requirements

### 4.1 Performance Requirements
- Mark calculations, GPA computations, and report generation shall execute in under 1 second for cohorts up to 10,000 student records.
- Database queries utilize indexes on foreign keys (`dept_id`, `register_no`, `subject_code`, `status`) to ensure $O(\log n)$ search time.

### 4.2 Security & Integrity
- Foreign key cascading deletes ensure no orphaned marks exist when a student is removed.
- Check constraints at database level enforce valid score boundaries.

### 4.3 Maintainability & Modularity
- Implementation strictly follows the MVC and DAO architectural patterns, separating business logic from UI and data access layers.

---

## 5. Traceability Matrix

| Requirement ID | Description | Database Component | Java Class / Method | Python Component |
| :--- | :--- | :--- | :--- | :--- |
| **FR-1** | Student Management | `students` table | `StudentDAOImpl` | `db_extractor.py` |
| **FR-2** | Subject Management | `subjects` table | `SubjectDAOImpl` | `db_extractor.py` |
| **FR-3** | Marks Entry | `marks` table | `MarkDAOImpl.recordMarks()` | `data_preprocessing.py` |
| **FR-4** | Result & GPA Calculation | `trg_calculate_marks_insert` | `AcademicEvaluationServiceImpl.computeGrade()` | `data_preprocessing.py` |
| **FR-5.1** | Student Marksheet | `v_student_semester_summary` | `ConsoleApp.generateStudentMarksheet()` | `Student_Performance_Analysis.ipynb` |
| **FR-5.2** | Low Pass Rate Identifier | `v_subject_performance` | `ConsoleApp.viewSubjectPassRateAnalysis()` | `01_subject_pass_percentage.png` |
| **FR-5.3** | Department Summary | `v_department_summary` | `ConsoleApp.viewDepartmentSummary()` | `db_extractor.py` |
| **FR-6** | Visualizations | All views | N/A | `visualizer.py` (7 Charts) |
