# Database Normalization Documentation
## Student Academic Performance and Result Analysis System

**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Department of Information Technology  
**Academic Year:** 2026-27 (ODD) | **Class:** 25IT3A&B  
**Course Code & Title:** 25ITT31 - Database Management Systems (Unit 3: Normalization)  
**Scenario Assigned:** Scenario 8  
**Author:** Jaswanth surya S | **Reg No:** 732925ITR038 | **Class:** 25IT3A  

---

## 1. Overview of Normalization
Database Normalization is the systematic technique of organizing relational database tables to:
1. Eliminate data redundancy.
2. Prevent insertion, update, and deletion anomalies.
3. Ensure data consistency through well-defined functional dependencies.

This document details the progressive decomposition of the **Student Academic Performance and Result Analysis System** schema from an **Unnormalized Form (UNF)** up to **Boyce-Codd Normal Form (BCNF)**.

---

## 2. Unnormalized Form (UNF)

In an unnormalized legacy spreadsheet or flat record format, all student attributes, departmental data, multiple course subjects, and individual marks are stored within a single flat table containing repeating groups.

$$\begin{aligned}
\text{UNF\_ACADEMIC\_RECORD} = \{ & \underline{\text{register\_no}}, \text{student\_name}, \text{section}, \text{year\_of\_study}, \text{dept\_code}, \text{dept\_name}, \text{hod\_name}, \\
& (\text{subject\_code}, \text{subject\_name}, \text{credits}, \text{max\_internal}, \text{max\_external}, \text{internal\_marks}, \text{external\_marks}, \text{total\_marks}, \text{grade}, \text{status})^* \}
\end{aligned}$$

### Anomalies Present in UNF:
- **Redundancy:** Department name, HOD, and subject descriptions are repeated for every enrolled student.
- **Insertion Anomaly:** A new subject or department cannot be added unless at least one student enrolls in it.
- **Deletion Anomaly:** Deleting the only student in a course eliminates all record of that course from the institution.
- **Update Anomaly:** Updating a subject's title or HOD requires modifying hundreds of student rows.

---

## 3. First Normal Form (1NF)

### 1NF Definition
A relation $R$ is in First Normal Form if and only if all underlying attribute domains contain **atomic (indivisible) values**, there are no repeating groups or multivalued arrays, and a formal primary key is defined.

### Transformation to 1NF
Repeating groups of subjects and marks are flattened into individual tuples. The composite primary key for the flattened relation is defined as:
$$\text{Composite Primary Key} = (\underline{\text{register\_no}}, \underline{\text{subject\_code}})$$

$$\begin{aligned}
\text{R\_1NF} = \{ & \underline{\text{register\_no}}, \underline{\text{subject\_code}}, \text{student\_name}, \text{section}, \text{year\_of\_study}, \text{email}, \text{phone}, \\
& \text{dept\_code}, \text{dept\_name}, \text{hod\_name}, \text{subject\_name}, \text{semester}, \text{credits}, \\
& \text{max\_internal}, \text{max\_external}, \text{internal\_marks}, \text{external\_marks}, \text{total\_marks}, \text{grade}, \text{grade\_point}, \text{status} \}
\end{aligned}$$

---

## 4. Second Normal Form (2NF)

### 2NF Definition
A relation $R$ is in Second Normal Form if it is in **1NF** and **every non-prime attribute is fully functionally dependent on the primary key** (i.e., there are no **partial dependencies**, where an attribute depends on a proper subset of a composite candidate key).

### Functional Dependencies Analysis on 1NF:
1. $\{\text{register\_no}, \text{subject\_code}\} \rightarrow \{\text{internal\_marks}, \text{external\_marks}, \text{total\_marks}, \text{grade}, \text{grade\_point}, \text{status}\}$ *(Full Functional Dependency)*
2. $\text{register\_no} \rightarrow \{\text{student\_name}, \text{section}, \text{year\_of\_study}, \text{email}, \text{phone}, \text{dept\_code}, \text{dept\_name}, \text{hod\_name}\}$ *(Partial Dependency - depends only on register_no)*
3. $\text{subject\_code} \rightarrow \{\text{subject\_name}, \text{semester}, \text{credits}, \text{max\_internal}, \text{max\_external}, \text{dept\_code}, \text{dept\_name}, \text{hod\_name}\}$ *(Partial Dependency - depends only on subject_code)*

### Decomposition into 2NF:
To remove partial dependencies, we decompose $\text{R\_1NF}$ into three distinct relations:

1. **STUDENT_2NF**
   $$\{\underline{\text{register\_no}}, \text{student\_name}, \text{section}, \text{year\_of\_study}, \text{email}, \text{phone}, \text{dept\_code}, \text{dept\_name}, \text{hod\_name}\}$$
   *Primary Key:* `register_no`

2. **SUBJECT_2NF**
   $$\{\underline{\text{subject\_code}}, \text{subject\_name}, \text{semester}, \text{credits}, \text{max\_internal}, \text{max\_external}, \text{dept\_code}\}$$
   *Primary Key:* `subject_code`

3. **MARKS_2NF**
   $$\{\underline{\text{register\_no}}, \underline{\text{subject\_code}}, \text{internal\_marks}, \text{external\_marks}, \text{total\_marks}, \text{grade}, \text{grade\_point}, \text{status}\}$$
   *Primary Key:* `(register_no, subject_code)`

---

## 5. Third Normal Form (3NF)

### 3NF Definition
A relation $R$ is in Third Normal Form if it is in **2NF** and **no non-prime attribute is transitively dependent on the primary key**.  
Formally, for every non-trivial functional dependency $X \rightarrow Y$:
- $X$ is a super key, OR
- $Y$ is a prime attribute (part of a candidate key).

### Transitive Dependencies Identified in 2NF:
In `STUDENT_2NF`:
$$\text{register\_no} \rightarrow \text{dept\_code} \quad \text{and} \quad \text{dept\_code} \rightarrow \{\text{dept\_name}, \text{hod\_name}\}$$
Here, `dept_name` and `hod_name` transitively depend on `register_no` through the non-key attribute `dept_code`.

### Decomposition into 3NF:
We extract department attributes into an independent relation `DEPARTMENTS`:

1. **DEPARTMENTS (3NF):**
   $$\{\underline{\text{dept\_id}}, \text{dept\_code}, \text{dept\_name}, \text{hod\_name}\}$$
   *Primary Key:* `dept_id`, *Alternate Key:* `dept_code`

2. **STUDENTS (3NF):**
   $$\{\underline{\text{register\_no}}, \text{name}, \text{dept\_id}, \text{year\_of\_study}, \text{section}, \text{email}, \text{phone}\}$$
   *Primary Key:* `register_no`, *Foreign Key:* `dept_id REFERENCES DEPARTMENTS(dept_id)`

3. **SUBJECTS (3NF):**
   $$\{\underline{\text{subject\_code}}, \text{subject\_name}, \text{dept\_id}, \text{semester}, \text{credits}, \text{max\_internal}, \text{max\_external}\}$$
   *Primary Key:* `subject_code`, *Foreign Key:* `dept_id REFERENCES DEPARTMENTS(dept_id)`

4. **MARKS (3NF):**
   $$\{\underline{\text{mark\_id}}, \text{register\_no}, \text{subject\_code}, \text{internal\_marks}, \text{external\_marks}, \text{total\_marks}, \text{grade}, \text{grade\_point}, \text{status}\}$$
   *Primary Key:* `mark_id`, *Alternate Key:* `(register_no, subject_code)`

---

## 6. Boyce-Codd Normal Form (BCNF)

### BCNF Definition
A relation $R$ is in Boyce-Codd Normal Form if and only if **for every non-trivial functional dependency $X \rightarrow Y$, $X$ is a super key**.

### Evaluation of Each Decomposed Relation:
1. **DEPARTMENTS:**
   - $FD_1: \text{dept\_id} \rightarrow \{\text{dept\_code}, \text{dept\_name}, \text{hod\_name}\}$ (`dept_id` is candidate key) $\implies$ **Satisfies BCNF**.
   - $FD_2: \text{dept\_code} \rightarrow \{\text{dept\_id}, \text{dept\_name}, \text{hod\_name}\}$ (`dept_code` is candidate key) $\implies$ **Satisfies BCNF**.

2. **STUDENTS:**
   - $FD_1: \text{register\_no} \rightarrow \{\text{name}, \text{dept\_id}, \text{year\_of\_study}, \text{section}, \text{email}, \text{phone}\}$ (`register_no` is candidate key) $\implies$ **Satisfies BCNF**.
   - $FD_2: \text{email} \rightarrow \{\text{register\_no}, \text{name}, \text{dept\_id}, \dots\}$ (`email` is candidate key) $\implies$ **Satisfies BCNF**.

3. **SUBJECTS:**
   - $FD_1: \text{subject\_code} \rightarrow \{\text{subject\_name}, \text{dept\_id}, \text{semester}, \text{credits}, \text{max\_internal}, \text{max\_external}\}$ (`subject_code` is candidate key) $\implies$ **Satisfies BCNF**.

4. **MARKS:**
   - $FD_1: \text{mark\_id} \rightarrow \{\text{register\_no}, \text{subject\_code}, \text{internal\_marks}, \text{external\_marks}, \dots\}$ (`mark_id` is candidate key) $\implies$ **Satisfies BCNF**.
   - $FD_2: \{\text{register\_no}, \text{subject\_code}\} \rightarrow \{\text{mark\_id}, \text{internal\_marks}, \text{external\_marks}, \dots\}$ (`(register_no, subject_code)` is candidate key) $\implies$ **Satisfies BCNF**.

---

## 7. Verification: Decomposition Properties

### 7.1 Lossless Join Property Proof
A decomposition of $R$ into $(R_1, R_2)$ is guaranteed to be **lossless** if:
$$R_1 \cap R_2 \rightarrow R_1 \quad \text{or} \quad R_1 \cap R_2 \rightarrow R_2$$

- For `STUDENTS` $\bowtie$ `DEPARTMENTS`:
  $$\text{STUDENTS} \cap \text{DEPARTMENTS} = \{\text{dept\_id}\}$$
  Since `dept_id` is the Primary Key of `DEPARTMENTS`, $\text{dept\_id} \rightarrow \text{DEPARTMENTS}$.  
  $\implies$ **The decomposition is guaranteed lossless.**

- For `MARKS` $\bowtie$ `STUDENTS`:
  $$\text{MARKS} \cap \text{STUDENTS} = \{\text{register\_no}\}$$
  Since `register_no` is the Primary Key of `STUDENTS`, $\text{register\_no} \rightarrow \text{STUDENTS}$.  
  $\implies$ **The decomposition is guaranteed lossless.**

### 7.2 Dependency Preservation
All identified functional dependencies are preserved within their respective decomposed relations without requiring multi-table cross joins to enforce constraints.

---

## 8. Summary of Final Normalized Relations

| Relation Name | Primary Key | Foreign Key(s) | Normal Form Achieved |
| :--- | :--- | :--- | :--- |
| **`departments`** | `dept_id` | *None* | **BCNF** |
| **`students`** | `register_no` | `dept_id` $\rightarrow$ `departments(dept_id)` | **BCNF** |
| **`subjects`** | `subject_code` | `dept_id` $\rightarrow$ `departments(dept_id)` | **BCNF** |
| **`marks`** | `mark_id` | `register_no` $\rightarrow$ `students`, `subject_code` $\rightarrow$ `subjects` | **BCNF** |
