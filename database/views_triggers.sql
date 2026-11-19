-- ====================================================================
-- Views and Triggers: Student Academic Performance and Result Analysis System
-- Engine: SQLite / ANSI SQL Compatible
-- ====================================================================

-- 1. View: Detailed Student Subject Marks
DROP VIEW IF EXISTS v_student_marks_detail;
CREATE VIEW v_student_marks_detail AS
SELECT 
    s.register_no,
    s.name AS student_name,
    s.section,
    d.dept_code,
    d.dept_name,
    sub.subject_code,
    sub.subject_name,
    sub.credits,
    m.internal_marks,
    m.external_marks,
    m.total_marks,
    m.grade,
    m.grade_point,
    m.status
FROM marks m
JOIN students s ON m.register_no = s.register_no
JOIN subjects sub ON m.subject_code = sub.subject_code
JOIN departments d ON s.dept_id = d.dept_id;

-- 2. View: Student-Wise Semester Result & GPA Summary
DROP VIEW IF EXISTS v_student_semester_summary;
CREATE VIEW v_student_semester_summary AS
SELECT 
    s.register_no,
    s.name AS student_name,
    s.section,
    d.dept_code,
    COUNT(m.subject_code) AS total_subjects,
    SUM(CASE WHEN m.status = 'PASS' THEN 1 ELSE 0 END) AS subjects_passed,
    SUM(CASE WHEN m.status = 'FAIL' THEN 1 ELSE 0 END) AS subjects_failed,
    ROUND(SUM(m.total_marks), 2) AS grand_total,
    ROUND(AVG(m.total_marks), 2) AS average_marks,
    ROUND(CAST(SUM(m.grade_point * sub.credits) AS REAL) / SUM(sub.credits), 2) AS gpa,
    CASE 
        WHEN SUM(CASE WHEN m.status = 'FAIL' THEN 1 ELSE 0 END) = 0 THEN 'PASS'
        ELSE 'FAIL'
    END AS overall_result
FROM students s
JOIN departments d ON s.dept_id = d.dept_id
LEFT JOIN marks m ON s.register_no = m.register_no
LEFT JOIN subjects sub ON m.subject_code = sub.subject_code
GROUP BY s.register_no, s.name, s.section, d.dept_code;

-- 3. View: Subject-Wise Performance Analysis (Low pass rate identifier)
DROP VIEW IF EXISTS v_subject_performance;
CREATE VIEW v_subject_performance AS
SELECT 
    sub.subject_code,
    sub.subject_name,
    sub.credits,
    d.dept_code,
    COUNT(m.mark_id) AS total_appeared,
    SUM(CASE WHEN m.status = 'PASS' THEN 1 ELSE 0 END) AS total_passed,
    SUM(CASE WHEN m.status = 'FAIL' THEN 1 ELSE 0 END) AS total_failed,
    ROUND((CAST(SUM(CASE WHEN m.status = 'PASS' THEN 1 ELSE 0 END) AS REAL) / COUNT(m.mark_id)) * 100, 2) AS pass_percentage,
    ROUND(AVG(m.total_marks), 2) AS avg_marks,
    MIN(m.total_marks) AS min_marks,
    MAX(m.total_marks) AS max_marks
FROM subjects sub
JOIN departments d ON sub.dept_id = d.dept_id
LEFT JOIN marks m ON sub.subject_code = m.subject_code
GROUP BY sub.subject_code, sub.subject_name, sub.credits, d.dept_code;

-- 4. View: Department-Wise Academic Performance Summary
DROP VIEW IF EXISTS v_department_summary;
CREATE VIEW v_department_summary AS
SELECT 
    d.dept_code,
    d.dept_name,
    COUNT(DISTINCT s.register_no) AS total_students,
    COUNT(m.mark_id) AS total_evaluations,
    ROUND(AVG(m.total_marks), 2) AS dept_avg_marks,
    ROUND((CAST(SUM(CASE WHEN m.status = 'PASS' THEN 1 ELSE 0 END) AS REAL) / COUNT(m.mark_id)) * 100, 2) AS overall_pass_percentage
FROM departments d
JOIN students s ON d.dept_id = s.dept_id
LEFT JOIN marks m ON s.register_no = m.register_no
GROUP BY d.dept_code, d.dept_name;

-- 5. SQLite Trigger for Automated Grade & Status Calculation on INSERT
DROP TRIGGER IF EXISTS trg_calculate_marks_insert;
CREATE TRIGGER trg_calculate_marks_insert
AFTER INSERT ON marks
BEGIN
    UPDATE marks
    SET 
        total_marks = NEW.internal_marks + NEW.external_marks,
        grade = CASE 
            WHEN (NEW.internal_marks + NEW.external_marks) >= 90 AND NEW.external_marks >= 27 THEN 'O'
            WHEN (NEW.internal_marks + NEW.external_marks) >= 80 AND NEW.external_marks >= 27 THEN 'A+'
            WHEN (NEW.internal_marks + NEW.external_marks) >= 70 AND NEW.external_marks >= 27 THEN 'A'
            WHEN (NEW.internal_marks + NEW.external_marks) >= 60 AND NEW.external_marks >= 27 THEN 'B+'
            WHEN (NEW.internal_marks + NEW.external_marks) >= 50 AND NEW.external_marks >= 27 THEN 'B'
            ELSE 'RA'
        END,
        grade_point = CASE 
            WHEN (NEW.internal_marks + NEW.external_marks) >= 90 AND NEW.external_marks >= 27 THEN 10
            WHEN (NEW.internal_marks + NEW.external_marks) >= 80 AND NEW.external_marks >= 27 THEN 9
            WHEN (NEW.internal_marks + NEW.external_marks) >= 70 AND NEW.external_marks >= 27 THEN 8
            WHEN (NEW.internal_marks + NEW.external_marks) >= 60 AND NEW.external_marks >= 27 THEN 7
            WHEN (NEW.internal_marks + NEW.external_marks) >= 50 AND NEW.external_marks >= 27 THEN 6
            ELSE 0
        END,
        status = CASE 
            WHEN (NEW.internal_marks + NEW.external_marks) >= 50 AND NEW.external_marks >= 27 THEN 'PASS'
            ELSE 'FAIL'
        END
    WHERE mark_id = NEW.mark_id;
END;
