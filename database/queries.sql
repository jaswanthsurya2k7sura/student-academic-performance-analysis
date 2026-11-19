-- ====================================================================
-- SQL Analytical Queries: Student Academic Performance and Result Analysis System
-- Institution: Velalar College of Engineering and Technology, Erode - 12
-- Department: Information Technology
-- Class: 25IT3A&B | Scenario: 8
-- Student: Jaswanth surya S | Reg No: 732925ITR038
-- Demonstrating DBMS Syllabus (Unit 2 & 3: SQL, Joins, Aggregations, Subqueries)
-- ====================================================================

-- 1. Student-Wise Detailed Marksheet Report
-- Displays complete mark details with grade and GPA for an individual student (Jaswanth surya S)
SELECT 
    s.register_no,
    s.name AS student_name,
    s.section,
    d.dept_code,
    sub.subject_code,
    sub.subject_name,
    sub.credits,
    m.internal_marks,
    m.external_marks,
    m.total_marks,
    m.grade,
    m.grade_point,
    m.status
FROM students s
JOIN departments d ON s.dept_id = d.dept_id
JOIN marks m ON s.register_no = m.register_no
JOIN subjects sub ON m.subject_code = sub.subject_code
WHERE s.register_no = '732925ITR038'
ORDER BY sub.subject_code;

-- 2. Semester GPA Calculation for All Students
SELECT 
    s.register_no,
    s.name AS student_name,
    s.section,
    ROUND(SUM(m.grade_point * sub.credits) * 1.0 / SUM(sub.credits), 2) AS gpa,
    SUM(m.total_marks) AS grand_total,
    COUNT(m.subject_code) AS subjects_registered,
    SUM(CASE WHEN m.status = 'PASS' THEN 1 ELSE 0 END) AS subjects_passed,
    SUM(CASE WHEN m.status = 'FAIL' THEN 1 ELSE 0 END) AS arrears,
    CASE 
        WHEN SUM(CASE WHEN m.status = 'FAIL' THEN 1 ELSE 0 END) = 0 THEN 'ALL CLEAR' 
        ELSE 'ARREAR' 
    END AS semester_status
FROM students s
JOIN marks m ON s.register_no = m.register_no
JOIN subjects sub ON m.subject_code = sub.subject_code
GROUP BY s.register_no, s.name, s.section
ORDER BY gpa DESC, grand_total DESC;

-- 3. Identify Subjects with Low Pass Rates (Pass Rate < 85%)
-- Functional Requirement #4 & #5
SELECT 
    sub.subject_code,
    sub.subject_name,
    COUNT(m.mark_id) AS total_appeared,
    SUM(CASE WHEN m.status = 'PASS' THEN 1 ELSE 0 END) AS total_passed,
    SUM(CASE WHEN m.status = 'FAIL' THEN 1 ELSE 0 END) AS total_failed,
    ROUND((SUM(CASE WHEN m.status = 'PASS' THEN 1.0 ELSE 0.0 END) / COUNT(m.mark_id)) * 100, 2) AS pass_percentage,
    ROUND(AVG(m.total_marks), 2) AS average_score
FROM subjects sub
JOIN marks m ON sub.subject_code = m.subject_code
GROUP BY sub.subject_code, sub.subject_name
HAVING pass_percentage < 85.0
ORDER BY pass_percentage ASC;

-- 4. Department-Wise Academic Performance Comparison
SELECT 
    d.dept_code,
    d.dept_name,
    COUNT(DISTINCT s.register_no) AS total_students,
    COUNT(m.mark_id) AS total_papers_evaluated,
    ROUND(AVG(m.total_marks), 2) AS dept_average_marks,
    ROUND((SUM(CASE WHEN m.status = 'PASS' THEN 1.0 ELSE 0.0 END) / COUNT(m.mark_id)) * 100, 2) AS dept_pass_percentage
FROM departments d
JOIN students s ON d.dept_id = s.dept_id
JOIN marks m ON s.register_no = m.register_no
GROUP BY d.dept_id, d.dept_code, d.dept_name;

-- 5. Nested Subquery: Find Students Scoring Above Department Average in Java Programming
SELECT 
    s.register_no,
    s.name,
    s.section,
    m.total_marks,
    m.grade
FROM students s
JOIN marks m ON s.register_no = m.register_no
WHERE m.subject_code = '25ITT32'
  AND m.total_marks > (
      SELECT AVG(total_marks) 
      FROM marks 
      WHERE subject_code = '25ITT32'
  )
ORDER BY m.total_marks DESC;

-- 6. Section-Wise Comparative Analysis (25IT3A vs 25IT3B)
SELECT 
    s.section,
    COUNT(DISTINCT s.register_no) AS students_count,
    ROUND(AVG(m.total_marks), 2) AS section_avg_marks,
    ROUND((SUM(CASE WHEN m.status = 'PASS' THEN 1.0 ELSE 0.0 END) / COUNT(m.mark_id)) * 100, 2) AS section_pass_rate
FROM students s
JOIN marks m ON s.register_no = m.register_no
WHERE s.dept_id = 1
GROUP BY s.section;

-- 7. Grade Distribution Across the Department
SELECT 
    m.grade,
    COUNT(*) AS total_awarded,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM marks), 2) AS percentage_share
FROM marks m
GROUP BY m.grade
ORDER BY 
    CASE m.grade
        WHEN 'O' THEN 1
        WHEN 'A+' THEN 2
        WHEN 'A' THEN 3
        WHEN 'B+' THEN 4
        WHEN 'B' THEN 5
        WHEN 'RA' THEN 6
        ELSE 7
    END;
