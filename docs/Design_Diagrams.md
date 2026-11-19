# System Design Diagrams
## Student Academic Performance and Result Analysis System

**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Department of Information Technology  
**Academic Year:** 2026-27 (ODD) | **Class:** 25IT3A&B  
**Course Code & Title:** 25ITT33 - Software Engineering & 25ITT31 - DBMS  
**Scenario Assigned:** Scenario 8  
**Author:** Jaswanth surya S | **Reg No:** 732925ITR038 | **Class:** 25IT3A  

---

## 1. Overall System Architecture

The system utilizes an integrated **3-Tier Layered MVC Architecture** spanning Java Enterprise OOP, Relational DBMS, and Python Data Science Pipeline.

```mermaid
flowchart TD
    subgraph Presentation_Layer ["Presentation & UI Layer"]
        CLI["Java Interactive Console CLI (ConsoleApp)"]
        Jupyter["Jupyter Analytics Notebook (.ipynb)"]
        Plots["Exported PNG Visualizations (7 Charts)"]
    end

    subgraph Service_Layer ["Business Logic & Service Layer"]
        AES["AcademicEvaluationService"]
        GradingEngine["Grading & GPA Engine (10-Point Scale)"]
        RuleValidator["Input & Assessment Validator"]
        StreamsFilter["Java 21 Streams & Lambda Filters"]
    end

    subgraph Data_Access_Layer ["Data Access Layer (DAO)"]
        StudentDAO["StudentDAO / StudentDAOImpl"]
        SubjectDAO["SubjectDAO / SubjectDAOImpl"]
        MarkDAO["MarkDAO / MarkDAOImpl"]
        JDBC["JDBC Connection Manager (Singleton)"]
    end

    subgraph Persistence_Layer ["Database Persistence Tier (DBMS)"]
        DB[(SQLite / MySQL Database)]
        Tables["Tables: students, subjects, departments, marks"]
        Views["Views: v_student_summary, v_subject_perf, v_dept_summary"]
        Triggers["Triggers: Automated Mark & Grade Calculation"]
    end

    subgraph Python_Analytics_Layer ["Python Data Science Tier (25ITC31)"]
        Extractor["Database Extractor (db_extractor.py)"]
        Preprocessor["Data Cleaning & Wrangling (Pandas/NumPy)"]
        Visualizer["Statistical Visualization Engine (Matplotlib/Seaborn)"]
    end

    CLI --> AES
    AES --> GradingEngine
    AES --> RuleValidator
    AES --> StreamsFilter
    AES --> StudentDAO
    AES --> SubjectDAO
    AES --> MarkDAO
    StudentDAO --> JDBC
    SubjectDAO --> JDBC
    MarkDAO --> JDBC
    JDBC --> DB
    DB --- Tables
    DB --- Views
    DB --- Triggers

    DB --> Extractor
    Extractor --> Preprocessor
    Preprocessor --> Visualizer
    Visualizer --> Plots
    Extractor --> Jupyter
```

---

## 2. Use Case Diagram

```mermaid
flowchart LR
    Faculty["Faculty / Course Instructor"]
    HOD["Head of Department / Academic Coordinator"]
    StudentActor["Student / Examination Cell"]

    subgraph UC_Boundary ["Student Academic Performance & Result Analysis System"]
        UC1(["Enter / Update Student Marks"])
        UC2(["Validate Marks Limits (40 Int / 60 Ext)"])
        UC3(["Compute Grades & GPA (10-point Scale)"])
        UC4(["Generate Student Marksheet"])
        UC5(["Identify Subjects with Low Pass Rate (< 85%)"])
        UC6(["Generate Department Academic Summary"])
        UC7(["View Class Rank & Distinction List"])
        UC8(["Execute Python EDA & Statistical Visualizations"])
    end

    Faculty --> UC1
    Faculty --> UC4
    UC1 -.->|includes| UC2
    UC1 -.->|includes| UC3

    HOD --> UC5
    HOD --> UC6
    HOD --> UC7
    HOD --> UC8

    StudentActor --> UC4
```

---

## 3. Class Diagram (UML)

```mermaid
classDiagram
    class Person {
        <<abstract>>
        -String id
        -String name
        -String email
        -String phone
        +getId() String
        +getName() String
        +getEmail() String
        +getPhone() String
        +getRoleDescription()* String
    }

    class Student {
        -int deptId
        -String deptCode
        -int yearOfStudy
        -String section
        +getRegisterNo() String
        +getDeptId() int
        +getYearOfStudy() int
        +getSection() String
        +getRoleDescription() String
    }

    class Department {
        -int deptId
        -String deptCode
        -String deptName
        -String hodName
        +getDeptId() int
        +getDeptCode() String
        +getDeptName() String
        +getHodName() String
    }

    class Subject {
        -String subjectCode
        -String subjectName
        -int deptId
        -int semester
        -int credits
        -double maxInternal
        -double maxExternal
        +getSubjectCode() String
        +getSubjectName() String
        +getCredits() int
        +getMaxTotal() double
    }

    class Mark {
        -int markId
        -String registerNo
        -String subjectCode
        -double internalMarks
        -double externalMarks
        -double totalMarks
        -String grade
        -int gradePoint
        -String status
        +getTotalMarks() double
        +getGrade() String
        +getGradePoint() int
        +getStatus() String
    }

    class StudentResult {
        -String registerNo
        -String studentName
        -String section
        -String deptCode
        -List~Mark~ markList
        -double grandTotal
        -double averageMarks
        -double gpa
        -int subjectsPassed
        -int subjectsFailed
        -String overallStatus
        +addMark(Mark mark) void
        +getGpa() double
        +getOverallStatus() String
    }

    class AcademicEvaluationService {
        <<interface>>
        +registerStudent(Student student) void
        +enterMarks(String regNo, String subCode, double intM, double extM) void
        +computeGrade(double total, double ext) String
        +computeGradePoint(String grade) int
        +generateStudentMarksheet(String regNo) StudentResult
        +getDepartmentRankList(int deptId) List~StudentResult~
        +getSubjectWisePassRates() List~Map~
    }

    class AcademicEvaluationServiceImpl {
        -StudentDAO studentDAO
        -SubjectDAO subjectDAO
        -MarkDAO markDAO
        +computeGrade(double total, double ext) String
        +computeGradePoint(String grade) int
        +calculateOverallClassAverage() double
        +filterStudentsWithArrears() List~StudentResult~
    }

    Person <|-- Student
    Student "1" *-- "1..*" Mark
    Subject "1" *-- "0..*" Mark
    Department "1" o-- "1..*" Student
    Department "1" o-- "1..*" Subject
    StudentResult "1" o-- "1..*" Mark
    AcademicEvaluationService <|.. AcademicEvaluationServiceImpl
```

---

## 4. Sequence Diagram: Mark Entry and Result Computation Flow

```mermaid
sequenceDiagram
    autonumber
    actor Faculty as Course Faculty
    participant UI as ConsoleApp (UI)
    participant Service as AcademicEvaluationService
    participant Validator as InputValidator
    participant MarkDAO as MarkDAOImpl
    participant DB as SQLite/MySQL Database

    Faculty->>UI: Enter marks (RegNo, SubjectCode, Internal, External)
    UI->>Service: enterMarks(regNo, subCode, internal, external)
    Service->>Validator: validateMarks(internal, external)
    Validator-->>Service: Valid (<=40, <=60)
    Service->>Service: computeGrade(total, external)
    Service->>Service: computeGradePoint(grade)
    Service->>Service: computePassFailStatus(total, external)
    Service->>MarkDAO: recordMarks(Mark mark)
    MarkDAO->>DB: INSERT / UPDATE INTO marks
    DB-->>MarkDAO: Success (Row updated)
    MarkDAO-->>Service: Acknowledged
    Service-->>UI: Marks Recorded Successfully
    UI-->>Faculty: Display Formatted Confirmation
```

---

## 5. Entity-Relationship (ER) Diagram

```mermaid
erDiagram
    DEPARTMENT ||--o{ STUDENT : "enrolls"
    DEPARTMENT ||--o{ SUBJECT : "offers"
    STUDENT ||--o{ MARK : "secures"
    SUBJECT ||--o{ MARK : "evaluated_in"

    DEPARTMENT {
        int dept_id PK
        string dept_code UK
        string dept_name
        string hod_name
    }

    STUDENT {
        string register_no PK
        string name
        int dept_id FK
        int year_of_study
        string section
        string email UK
        string phone
    }

    SUBJECT {
        string subject_code PK
        string subject_name
        int dept_id FK
        int semester
        int credits
        double max_internal
        double max_external
    }

    MARK {
        int mark_id PK
        string register_no FK
        string subject_code FK
        double internal_marks
        double external_marks
        double total_marks
        string grade
        int grade_point
        string status
        timestamp created_at
    }
```

---

## 6. Relational Model / Schema Representation

```text
DEPARTMENTS (
    dept_id (PK),
    dept_code (UNIQUE),
    dept_name,
    hod_name
)

STUDENTS (
    register_no (PK),
    name,
    dept_id (FK -> DEPARTMENTS.dept_id),
    year_of_study,
    section,
    email (UNIQUE),
    phone
)

SUBJECTS (
    subject_code (PK),
    subject_name,
    dept_id (FK -> DEPARTMENTS.dept_id),
    semester,
    credits,
    max_internal,
    max_external
)

MARKS (
    mark_id (PK),
    register_no (FK -> STUDENTS.register_no),
    subject_code (FK -> SUBJECTS.subject_code),
    internal_marks,
    external_marks,
    total_marks,
    grade,
    grade_point,
    status,
    created_at,
    UNIQUE(register_no, subject_code)
)
```

---

## 7. Data Flow Diagrams (DFD)

### 7.1 DFD Level 0 (Context Level Diagram)
The Level 0 context diagram demonstrates external entities interacting with the centralized academic system boundary.

```mermaid
flowchart LR
    Faculty["Faculty / Staff"]
    System[("0.0\nStudent Academic Performance &\nResult Analysis System")]
    Admin["HOD / Exam Cell"]
    StudentActor["Student"]

    Faculty -->|"Student Marks & Curriculum Data"| System
    System -->|"Validation Feedback & Marks Confirmation"| Faculty

    Admin -->|"Report Generation Requests & Filters"| System
    System -->|"Subject Pass Rates, Rank Lists & Department Summaries"| Admin

    StudentActor -->|"Register Number Query"| System
    System -->|"Semester Marksheet & GPA"| StudentActor
```

### 7.2 DFD Level 1 (Detailed Subsystem Flow)
Level 1 decomposes the system into functional processes: Student Management, Subject Mapping, Mark Entry & Validation, Result & GPA Engine, and Python Data Science Analytics.

```mermaid
flowchart TD
    Faculty["Faculty / Course Instructor"]
    Admin["HOD / Exam Cell"]
    
    subgraph DFD_Level_1 ["Level 1 Functional Decomposition"]
        P1["1.0 Student & Subject Management"]
        P2["2.0 Mark Recording & Input Validation"]
        P3["3.0 Result & GPA Computation Engine"]
        P4["4.0 Academic Reporting & Filtering"]
        P5["5.0 Python EDA & Visual Analytics"]
    end

    D1[("D1: STUDENTS")]
    D2[("D2: SUBJECTS")]
    D3[("D3: MARKS")]
    D4[("D4: DEPARTMENTS")]

    Faculty -->|"Student details & Subject credits"| P1
    P1 -->|"Store student bio-data"| D1
    P1 -->|"Store course curriculum"| D2
    
    Faculty -->|"Raw CIA (40) & ESE (60) marks"| P2
    D1 -.->|"Verify RegNo"| P2
    D2 -.->|"Verify SubjectCode"| P2
    P2 -->|"Validated marks"| P3
    P3 -->|"Calculate Total, Grade, GP, Status"| D3
    
    Admin -->|"Generate Reports Request"| P4
    D1 -.-> P4
    D2 -.-> P4
    D3 -.-> P4
    D4 -.-> P4
    P4 -->|"Student Marksheet, Toppers, Low Pass Alerts"| Admin

    D3 -.->|"Extract records for EDA"| P5
    P5 -->|"Statistical plots, KDE, Boxplots, Heatmaps"| Admin
```

---

## 8. Activity Diagram: Mark Entry and Result Processing Workflow

```mermaid
stateDiagram-v2
    [*] --> SelectStudentAndSubject
    SelectStudentAndSubject --> EnterScores: Faculty inputs Internal (40) & External (60)
    EnterScores --> ValidateScores: Check Range [0-40] & [0-60]
    
    state ValidateScores <<choice>>
    ValidateScores --> ShowValidationError: Invalid marks (< 0 or > max)
    ShowValidationError --> EnterScores: Re-enter valid values
    
    ValidateScores --> CalculateTotal: Valid marks
    CalculateTotal --> CheckPassingConditions: Compute Total = Int + Ext
    
    state CheckPassingConditions <<choice>>
    CheckPassingConditions --> AssignFailGrade: Ext < 27.0 OR Total < 50.0
    CheckPassingConditions --> EvaluateLetterGrade: Ext >= 27.0 AND Total >= 50.0
    
    AssignFailGrade --> SetRA: Grade = 'RA', GradePoint = 0, Status = 'FAIL'
    
    state EvaluateLetterGrade <<choice>>
    EvaluateLetterGrade --> GradeO: Total >= 90 (Grade 'O', GP 10)
    EvaluateLetterGrade --> GradeAplus: Total >= 80 (Grade 'A+', GP 9)
    EvaluateLetterGrade --> GradeA: Total >= 70 (Grade 'A', GP 8)
    EvaluateLetterGrade --> GradeBplus: Total >= 60 (Grade 'B+', GP 7)
    EvaluateLetterGrade --> GradeB: Total >= 50 (Grade 'B', GP 6)

    GradeO --> PersistDatabase
    GradeAplus --> PersistDatabase
    GradeA --> PersistDatabase
    GradeBplus --> PersistDatabase
    GradeB --> PersistDatabase
    SetRA --> PersistDatabase

    PersistDatabase --> UpdateSemesterGPA: Re-calculate GPA = Sum(GP*Credits)/Sum(Credits)
    UpdateSemesterGPA --> CheckLowPassRate: Aggregate Subject Pass Rate
    CheckLowPassRate --> [*]: Display Formatted Result Confirmation
```

