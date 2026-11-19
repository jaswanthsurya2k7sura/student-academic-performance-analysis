-- ====================================================================
-- MySQL Database Schema: Student Academic Performance and Result Analysis System
-- Institution: Velalar College of Engineering and Technology, Erode - 12
-- Department: Information Technology
-- Class: 25IT3A&B | Scenario: 8
-- Student: Jaswanth surya S | Reg No: 732925ITR038
-- Engine: MySQL 8.0+ / MariaDB
-- ====================================================================

CREATE DATABASE IF NOT EXISTS student_performance_db;
USE student_performance_db;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS marks;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS departments;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Departments Table
CREATE TABLE departments (
    dept_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_code VARCHAR(10) NOT NULL UNIQUE,
    dept_name VARCHAR(100) NOT NULL,
    hod_name VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Students Table
CREATE TABLE students (
    register_no VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dept_id INT NOT NULL,
    year_of_study INT NOT NULL CHECK (year_of_study BETWEEN 1 AND 4),
    section VARCHAR(5) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    CONSTRAINT fk_students_dept FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Subjects Table
CREATE TABLE subjects (
    subject_code VARCHAR(15) PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL,
    dept_id INT NOT NULL,
    semester INT NOT NULL CHECK (semester BETWEEN 1 AND 8),
    credits INT NOT NULL CHECK (credits > 0),
    max_internal DECIMAL(5,2) NOT NULL DEFAULT 40.00,
    max_external DECIMAL(5,2) NOT NULL DEFAULT 60.00,
    CONSTRAINT fk_subjects_dept FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Marks / Assessments Table
CREATE TABLE marks (
    mark_id INT AUTO_INCREMENT PRIMARY KEY,
    register_no VARCHAR(20) NOT NULL,
    subject_code VARCHAR(15) NOT NULL,
    internal_marks DECIMAL(5,2) NOT NULL CHECK (internal_marks BETWEEN 0 AND 40),
    external_marks DECIMAL(5,2) NOT NULL CHECK (external_marks BETWEEN 0 AND 60),
    total_marks DECIMAL(5,2) NOT NULL CHECK (total_marks BETWEEN 0 AND 100),
    grade VARCHAR(5) NOT NULL,
    grade_point INT NOT NULL CHECK (grade_point BETWEEN 0 AND 10),
    status ENUM('PASS', 'FAIL') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_marks_student FOREIGN KEY (register_no) REFERENCES students(register_no)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_marks_subject FOREIGN KEY (subject_code) REFERENCES subjects(subject_code)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uq_student_subject UNIQUE(register_no, subject_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Indexes
CREATE INDEX idx_students_dept ON students(dept_id);
CREATE INDEX idx_subjects_dept ON subjects(dept_id);
CREATE INDEX idx_marks_student ON marks(register_no);
CREATE INDEX idx_marks_subject ON marks(subject_code);
CREATE INDEX idx_marks_status ON marks(status);
