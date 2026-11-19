-- ====================================================================
-- Database Schema: Student Academic Performance and Result Analysis System
-- Institution: Velalar College of Engineering and Technology, Erode - 12
-- Department: Information Technology
-- Class: 25IT3A&B | Scenario: 8
-- Student: Jaswanth surya S | Reg No: 732925ITR038
-- Engine: SQLite / ANSI SQL Compatible
-- ====================================================================

PRAGMA foreign_keys = ON;

-- 1. Departments Table
DROP TABLE IF EXISTS marks;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS departments;

CREATE TABLE departments (
    dept_id INTEGER PRIMARY KEY AUTOINCREMENT,
    dept_code VARCHAR(10) NOT NULL UNIQUE,
    dept_name VARCHAR(100) NOT NULL,
    hod_name VARCHAR(100) NOT NULL
);

-- 2. Students Table
CREATE TABLE students (
    register_no VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dept_id INTEGER NOT NULL,
    year_of_study INTEGER NOT NULL CHECK (year_of_study BETWEEN 1 AND 4),
    section VARCHAR(5) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 3. Subjects Table
CREATE TABLE subjects (
    subject_code VARCHAR(15) PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL,
    dept_id INTEGER NOT NULL,
    semester INTEGER NOT NULL CHECK (semester BETWEEN 1 AND 8),
    credits INTEGER NOT NULL CHECK (credits > 0),
    max_internal REAL NOT NULL DEFAULT 40.0,
    max_external REAL NOT NULL DEFAULT 60.0,
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 4. Marks / Assessments Table
CREATE TABLE marks (
    mark_id INTEGER PRIMARY KEY AUTOINCREMENT,
    register_no VARCHAR(20) NOT NULL,
    subject_code VARCHAR(15) NOT NULL,
    internal_marks REAL NOT NULL CHECK (internal_marks >= 0 AND internal_marks <= 40),
    external_marks REAL NOT NULL CHECK (external_marks >= 0 AND external_marks <= 60),
    total_marks REAL NOT NULL CHECK (total_marks >= 0 AND total_marks <= 100),
    grade VARCHAR(5) NOT NULL,
    grade_point INTEGER NOT NULL CHECK (grade_point BETWEEN 0 AND 10),
    status VARCHAR(10) NOT NULL CHECK (status IN ('PASS', 'FAIL')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (register_no) REFERENCES students(register_no) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (subject_code) REFERENCES subjects(subject_code) ON DELETE RESTRICT ON UPDATE CASCADE,
    UNIQUE(register_no, subject_code)
);

-- Indexes for Query Optimization
CREATE INDEX idx_students_dept ON students(dept_id);
CREATE INDEX idx_subjects_dept ON subjects(dept_id);
CREATE INDEX idx_marks_student ON marks(register_no);
CREATE INDEX idx_marks_subject ON marks(subject_code);
CREATE INDEX idx_marks_status ON marks(status);
