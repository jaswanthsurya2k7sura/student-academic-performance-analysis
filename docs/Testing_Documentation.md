# Software Testing Documentation
## Student Academic Performance and Result Analysis System

**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Department of Information Technology  
**Academic Year:** 2026-27 (ODD) | **Class:** 25IT3A&B  
**Course Code & Title:** 25ITT33 - Software Engineering (Unit 4: Testing)  
**Scenario Assigned:** Scenario 8  
**Author:** Jaswanth surya S | **Reg No:** 732925ITR038 | **Class:** 25IT3A  

---

## 1. Testing Strategy & Methodology

The testing process verifies and validates that the **Student Academic Performance and Result Analysis System** functions according to institutional rules, adheres to IEEE standards, and reliably prevents invalid operations.

### Testing Levels Executed:
1. **Unit Testing:** Testing isolated methods (e.g., mark validation, letter grade computation, GPA mathematical formulation).
2. **Integration Testing:** Testing interactions between Java Service layer, DAO layer, and SQLite/MySQL JDBC database transactions.
3. **System Testing:** End-to-end evaluation of user workflows including student marksheet generation, low pass rate alerts, and Python data visualization pipelines.

---

## 2. White-Box Testing (Logic & Branch Coverage)

### 2.1 Cyclomatic Complexity Analysis for Grading Function
The grading algorithm in `AcademicEvaluationServiceImpl.computeGrade()` evaluates marks against college criteria:

```text
public String computeGrade(double totalMarks, double externalMarks) {
    if (externalMarks < 27.0 || totalMarks < 50.0) return "RA"; // Decision 1
    if (totalMarks >= 90.0) return "O";                         // Decision 2
    if (totalMarks >= 80.0) return "A+";                        // Decision 3
    if (totalMarks >= 70.0) return "A";                         // Decision 4
    if (totalMarks >= 60.0) return "B+";                        // Decision 5
    return "B";                                                 // Default
}
```

- **Predicate Nodes ($P$):** 5 decision points.
- **Cyclomatic Complexity ($V(G)$):** $V(G) = P + 1 = 5 + 1 = 6$.
- **Independent Execution Paths:**
  - **Path 1:** `externalMarks < 27.0` $\implies$ Returns `RA` (Fail).
  - **Path 2:** `externalMarks >= 27.0` and `totalMarks >= 90.0` $\implies$ Returns `O`.
  - **Path 3:** `externalMarks >= 27.0` and $80.0 \le \text{totalMarks} < 90.0 \implies$ Returns `A+`.
  - **Path 4:** `externalMarks >= 27.0` and $70.0 \le \text{totalMarks} < 80.0 \implies$ Returns `A`.
  - **Path 5:** `externalMarks >= 27.0` and $60.0 \le \text{totalMarks} < 70.0 \implies$ Returns `B+`.
  - **Path 6:** `externalMarks >= 27.0` and $50.0 \le \text{totalMarks} < 60.0 \implies$ Returns `B`.

---

## 3. Black-Box Testing (Equivalence Partitioning & Boundary Value Analysis)

### 3.1 Input Partitioning
- **Internal Assessment (Max 40):**
  - *Valid Equivalence Class:* $[0.0, 40.0]$
  - *Invalid Equivalence Class:* $\text{Internal} < 0.0$ or $\text{Internal} > 40.0$
- **External Examination (Max 60):**
  - *Valid Equivalence Class:* $[0.0, 60.0]$
  - *Invalid Equivalence Class:* $\text{External} < 0.0$ or $\text{External} > 60.0$

---

## 4. Comprehensive Test Suite & Execution Results

| Test ID | Test Scenario | Test Input Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-01** | Student Profile Retrieval | Reg No: `732925ITR038` | Displays profile for "Jaswanth surya S", Section: 25IT3A | Profile loaded accurately with all departmental attributes | **PASS** |
| **TC-02** | Valid Marks Entry & Calculation | Internal: 38.0, External: 56.0 (Total: 94.0) | Total: 94.0, Grade: 'O', Grade Point: 10, Status: 'PASS' | Calculated Total: 94.0, Grade: 'O', GP: 10, Status: 'PASS' | **PASS** |
| **TC-03** | Boundary Value: Max Marks | Internal: 40.0, External: 60.0 (Total: 100.0) | Total: 100.0, Grade: 'O', Grade Point: 10, Status: 'PASS' | Accepted, Grade 'O' assigned | **PASS** |
| **TC-04** | Boundary Value: Zero Marks | Internal: 0.0, External: 0.0 (Total: 0.0) | Total: 0.0, Grade: 'RA', Grade Point: 0, Status: 'FAIL' | Total: 0.0, Grade: 'RA', Status: 'FAIL' | **PASS** |
| **TC-05** | Invalid Mark Rejection (Internal > 40) | Internal: 45.0, External: 50.0 | Throws `InvalidMarkException` ("Internal marks must be between 0 and 40") | `InvalidMarkException` thrown; record rejected | **PASS** |
| **TC-06** | Invalid Mark Rejection (Negative Marks) | Internal: -5.0, External: 40.0 | Throws `InvalidMarkException` | `InvalidMarkException` thrown; record rejected | **PASS** |
| **TC-07** | Passing Condition: External Border Check | Internal: 35.0, External: 26.0 (Total: 61.0) | Grade: 'RA', Status: 'FAIL' (Failed external min 27) | Grade: 'RA', Status: 'FAIL' | **PASS** |
| **TC-08** | Passing Condition: Overall Total Border Check | Internal: 20.0, External: 27.0 (Total: 47.0) | Grade: 'RA', Status: 'FAIL' (Total < 50) | Grade: 'RA', Status: 'FAIL' | **PASS** |
| **TC-09** | GPA Calculation Formula Verification | Courses with grades: [O(4 cr), O(3 cr), O(3 cr), O(3 cr)] | $\text{GPA} = (40 + 30 + 30 + 30) / 13 = 10.00$ | Calculated GPA: 10.00 | **PASS** |
| **TC-10** | Student Marksheet Generation | Reg No: `732925ITR038` | Returns 4 course subjects, Grand Total: 377.0, GPA: 10.00, Status: PASS | Formatted ASCII marksheet rendered with 100% precision | **PASS** |
| **TC-11** | Non-Existent Student Search | Reg No: `999999UNKNOWN` | Throws `StudentNotFoundException` | Handled gracefully with error prompt | **PASS** |
| **TC-12** | Low Pass Rate Identification | Query `v_subject_performance` | Flags courses with pass rate $< 85\%$ as 'LOW PASS RATE' | Subjects filtered correctly based on institutional threshold | **PASS** |
| **TC-13** | Database Integrity (Duplicate Mark) | Insert duplicate `(732925ITR038, 25ITT31)` | Triggers UPSERT or unique constraint check | Handled via UPSERT; marks updated cleanly | **PASS** |
| **TC-14** | Python Database Data Retrieval | Execute `db_extractor.py` | 4 DataFrames extracted with exact row counts | 68 mark records extracted to Pandas | **PASS** |
| **TC-15** | Python Visualization Export | Execute `visualizer.py` | 7 PNG chart files created in `visualizations/` folder | 7 high-resolution charts generated successfully | **PASS** |

---

## 5. Summary of Test Results
- **Total Test Cases Executed:** 15
- **Passed:** 15 (100%)
- **Failed:** 0 (0%)
- **System Stability:** Verified and ready for production academic deployment.
