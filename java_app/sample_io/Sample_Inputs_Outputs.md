# Sample Inputs and Outputs
## Student Academic Performance and Result Analysis System

**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Department of Information Technology  
**Scenario:** 8 | **Academic Year:** 2026-27 (ODD)  
**Student:** Jaswanth surya S | **Reg No:** 732925ITR038 | **Class:** 25IT3A  

---

### Sample Execution 1: Student Marksheet Generation (Option 6)
**Input:**
```text
Select an option [1-10]: 6
Enter Student Register No (e.g. 732925ITR038): 732925ITR038
```

**Output:**
```text
=========================================================================
                   SEMESTER EXAMINATION MARKSHEET
=========================================================================
 Register No : 732925ITR038          Student Name : Jaswanth surya S
 Department  : IT                    Class/Section: 25IT3A
-------------------------------------------------------------------------
+--------------+-----------------------------+---------+---------------+---------------+-------------+-------+----+--------+
| Subject Code | Subject Name                | Credits | Internal (40) | External (60) | Total (100) | Grade | GP | Status |
+--------------+-----------------------------+---------+---------------+---------------+-------------+-------+----+--------+
| 25ITC31      | Foundations of Data Science | 4       | 38.5          | 56.5          | 95.0        | O     | 10 | PASS   |
| 25ITT31      | Database Management Systems | 3       | 38.0          | 56.0          | 94.0        | O     | 10 | PASS   |
| 25ITT32      | Java Programming            | 3       | 39.0          | 57.0          | 96.0        | O     | 10 | PASS   |
| 25ITT33      | Software Engineering        | 3       | 37.0          | 55.0          | 92.0        | O     | 10 | PASS   |
+--------------+-----------------------------+---------+---------------+---------------+-------------+-------+----+--------+
-------------------------------------------------------------------------
 Grand Total   : 377.0 / 400.0          Average Marks: 94.25%
 Passed Subjects: 4                   Failed (Arrears): 0
 Grade Point Average (GPA): 10.00       Semester Result : PASS
=========================================================================
```

---

### Sample Execution 2: Subject Performance & Low Pass Rate Identification (Option 8)
**Input:**
```text
Select an option [1-10]: 8
```

**Output:**
```text
>>> SUBJECT PERFORMANCE & LOW PASS RATE IDENTIFIER

+--------------+------------------------------------+---------+----------+--------+--------+---------------+-----------+--------+
| Subject Code | Subject Name                       | Credits | Appeared | Passed | Failed | Pass Rate (%) | Avg Marks | Flag   |
+--------------+------------------------------------+---------+----------+--------+--------+---------------+-----------+--------+
| 25ITC31      | Foundations of Data Science        | 4       | 16       | 14     | 2      | 87.50%        | 73.19     | NORMAL |
| 25ITT31      | Database Management Systems        | 3       | 16       | 14     | 2      | 87.50%        | 73.19     | NORMAL |
| 25ITT32      | Java Programming                   | 3       | 16       | 15     | 1      | 93.75%        | 71.94     | NORMAL |
| 25CS31       | Operating Systems                  | 3       | 2        | 2      | 0      | 100.00%       | 79.00     | NORMAL |
| 25EC31       | Digital Principles & System Design | 3       | 2        | 2      | 0      | 100.00%       | 72.00     | NORMAL |
| 25ITT33      | Software Engineering               | 3       | 16       | 16     | 0      | 100.00%       | 73.06     | NORMAL |
+--------------+------------------------------------+---------+----------+--------+--------+---------------+-----------+--------+
[NOTE] Subjects flagged as 'LOW PASS RATE' (< 85%) require faculty review and remedial classes.
```

---

### Sample Execution 3: Department Merit / Rank List (Option 7)
**Input:**
```text
Select an option [1-10]: 7
```

**Output:**
```text
>>> DEPARTMENT MERIT / RANK LIST (All Clear Candidates)

+------+---------------+------------------+--------+-------------+-----------+-------+--------+
| Rank | Register No   | Student Name     | Sec    | Total Marks | Average % | GPA   | Status |
+------+---------------+------------------+--------+-------------+-----------+-------+--------+
| 1    | 732925ITR038  | Jaswanth surya S | 25IT3A | 377.0       | 94.25%    | 10.00 | PASS   |
| 2    | 732925ITR082  | Rithika G        | 25IT3B | 367.0       | 91.75%    | 9.77  | PASS   |
| 3    | 732925ITR012  | Bavithra S       | 25IT3A | 358.0       | 89.50%    | 9.54  | PASS   |
| 4    | 732925ITR050  | Lavanya M        | 25IT3A | 358.0       | 89.50%    | 9.54  | PASS   |
| 5    | 732925ITR008  | Ananya R         | 25IT3A | 341.0       | 85.25%    | 9.00  | PASS   |
| 6    | 732925ITR035  | Harini K         | 25IT3A | 330.0       | 82.50%    | 9.00  | PASS   |
| 7    | 732925ITR062  | Nivetha B        | 25IT3B | 335.0       | 83.75%    | 9.00  | PASS   |
| 8    | 732925ITR001  | Aadhavan V       | 25IT3A | 295.0       | 73.75%    | 8.00  | PASS   |
| 9    | 732925ITR068  | Kavin Kumar M    | 25IT3B | 290.0       | 72.50%    | 7.77  | PASS   |
| 10   | 732925ITR075  | Praveen S        | 25IT3B | 266.0       | 66.50%    | 7.23  | PASS   |
| 11   | 732925ITR042  | Karthik N        | 25IT3A | 259.0       | 64.75%    | 7.00  | PASS   |
| 12   | 732925ITR098  | Sanjay P         | 25IT3B | 235.0       | 58.75%    | 6.46  | PASS   |
| 13   | 732925ITR090  | Sakthi Vel V     | 25IT3B | 222.0       | 55.50%    | 6.23  | PASS   |
+------+---------------+------------------+--------+-------------+-----------+-------+--------+

>>> CANDIDATES REQUIRING ACADEMIC REMEDIATION (ARREARS)
+---------------+-------------------+--------+-------------+-----------------+------+
| Register No   | Student Name      | Sec    | Total Marks | Failed Subjects | GPA  |
+---------------+-------------------+--------+-------------+-----------------+------+
| 732925ITR024  | Dinesh Karthik R  | 25IT3A | 200.0       | 2               | 3.23 |
| 732925ITR055  | Manoj Kumar T     | 25IT3B | 196.0       | 2               | 2.77 |
| 732925ITR102  | Vigneshwaran K    | 25IT3B | 229.0       | 1               | 5.08 |
+---------------+-------------------+--------+-------------+-----------------+------+
```

---

### Sample Execution 4: Department-Wise Academic Summary (Option 9)
**Input:**
```text
Select an option [1-10]: 9
```

**Output:**
```text
>>> DEPARTMENT-WISE ACADEMIC PERFORMANCE SUMMARY

+-----------+---------------------------------------+----------------+-------------+-----------+----------------+
| Dept Code | Department Name                       | Students Count | Evaluations | Avg Marks | Overall Pass % |
+-----------+---------------------------------------+----------------+-------------+-----------+----------------+
| CSE       | Computer Science and Engineering      | 2              | 2           | 79.00     | 100.00%        |
| ECE       | Electronics and Communication Engg    | 2              | 2           | 72.00     | 100.00%        |
| IT        | Information Technology                | 16             | 64          | 72.84     | 92.19%         |
+-----------+---------------------------------------+----------------+-------------+-----------+----------------+
```
