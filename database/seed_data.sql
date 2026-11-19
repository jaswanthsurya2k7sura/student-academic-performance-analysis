-- ====================================================================
-- Seed Data: Student Academic Performance and Result Analysis System
-- Institution: Velalar College of Engineering and Technology, Erode - 12
-- Department: Information Technology
-- Class: 25IT3A&B | Scenario: 8
-- Student: Jaswanth surya S | Reg No: 732925ITR038
-- ====================================================================

-- 1. Insert Departments
INSERT INTO departments (dept_id, dept_code, dept_name, hod_name) VALUES
(1, 'IT', 'Information Technology', 'Dr. S. Murugesan'),
(2, 'CSE', 'Computer Science and Engineering', 'Dr. K. R. Valluvan'),
(3, 'ECE', 'Electronics and Communication Engineering', 'Dr. P. Sakthivel');

-- 2. Insert Subjects (Curriculum for II Year / III Semester)
INSERT INTO subjects (subject_code, subject_name, dept_id, semester, credits, max_internal, max_external) VALUES
('25ITT31', 'Database Management Systems', 1, 3, 3, 40.0, 60.0),
('25ITT32', 'Java Programming', 1, 3, 3, 40.0, 60.0),
('25ITT33', 'Software Engineering', 1, 3, 3, 40.0, 60.0),
('25ITC31', 'Foundations of Data Science', 1, 3, 4, 40.0, 60.0),
('25CS31', 'Operating Systems', 2, 3, 3, 40.0, 60.0),
('25EC31', 'Digital Principles & System Design', 3, 3, 3, 40.0, 60.0);

-- 3. Insert Students (Highlighting 732925ITR038 Jaswanth surya S, plus 008, 068, 098 and cohort)
INSERT INTO students (register_no, name, dept_id, year_of_study, section, email, phone) VALUES
('732925ITR038', 'Jaswanth surya S', 1, 2, '25IT3A', 'jaswanthsurya@velalarengg.ac.in', '9876543210'),
('732925ITR008', 'Ananya R', 1, 2, '25IT3A', 'ananya.r@velalarengg.ac.in', '9876543201'),
('732925ITR068', 'Kavin Kumar M', 1, 2, '25IT3B', 'kavin.m@velalarengg.ac.in', '9876543202'),
('732925ITR098', 'Sanjay P', 1, 2, '25IT3B', 'sanjay.p@velalarengg.ac.in', '9876543203'),
('732925ITR001', 'Aadhavan V', 1, 2, '25IT3A', 'aadhavan.v@velalarengg.ac.in', '9876543204'),
('732925ITR012', 'Bavithra S', 1, 2, '25IT3A', 'bavithra.s@velalarengg.ac.in', '9876543205'),
('732925ITR024', 'Dinesh Karthik R', 1, 2, '25IT3A', 'dinesh.r@velalarengg.ac.in', '9876543206'),
('732925ITR035', 'Harini K', 1, 2, '25IT3A', 'harini.k@velalarengg.ac.in', '9876543207'),
('732925ITR042', 'Karthik N', 1, 2, '25IT3A', 'karthik.n@velalarengg.ac.in', '9876543208'),
('732925ITR050', 'Lavanya M', 1, 2, '25IT3A', 'lavanya.m@velalarengg.ac.in', '9876543209'),
('732925ITR055', 'Manoj Kumar T', 1, 2, '25IT3B', 'manoj.t@velalarengg.ac.in', '9876543211'),
('732925ITR062', 'Nivetha B', 1, 2, '25IT3B', 'nivetha.b@velalarengg.ac.in', '9876543212'),
('732925ITR075', 'Praveen S', 1, 2, '25IT3B', 'praveen.s@velalarengg.ac.in', '9876543213'),
('732925ITR082', 'Rithika G', 1, 2, '25IT3B', 'rithika.g@velalarengg.ac.in', '9876543214'),
('732925ITR090', 'Sakthi Vel V', 1, 2, '25IT3B', 'sakthivel.v@velalarengg.ac.in', '9876543215'),
('732925ITR102', 'Vigneshwaran K', 1, 2, '25IT3B', 'vignesh.k@velalarengg.ac.in', '9876543216'),
('732925CSR015', 'Chandru E', 2, 2, '25CS3A', 'chandru.e@velalarengg.ac.in', '9876543217'),
('732925CSR045', 'Madhumitha K', 2, 2, '25CS3A', 'madhumitha.k@velalarengg.ac.in', '9876543218'),
('732925ECR020', 'Gowtham R', 3, 2, '25EC3A', 'gowtham.r@velalarengg.ac.in', '9876543219'),
('732925ECR052', 'Subhashini T', 3, 2, '25EC3A', 'subhashini.t@velalarengg.ac.in', '9876543220');

-- 4. Insert Marks (Calculated totals, grades, and status)
-- 732925ITR038: Jaswanth surya S (Top Academic Performer)
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR038', '25ITT31', 38.0, 56.0, 94.0, 'O', 10, 'PASS'),
('732925ITR038', '25ITT32', 39.0, 57.0, 96.0, 'O', 10, 'PASS'),
('732925ITR038', '25ITT33', 37.0, 55.0, 92.0, 'O', 10, 'PASS'),
('732925ITR038', '25ITC31', 38.5, 56.5, 95.0, 'O', 10, 'PASS');

-- 732925ITR008: Ananya R
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR008', '25ITT31', 35.0, 51.0, 86.0, 'A+', 9, 'PASS'),
('732925ITR008', '25ITT32', 36.0, 52.0, 88.0, 'A+', 9, 'PASS'),
('732925ITR008', '25ITT33', 34.0, 48.0, 82.0, 'A+', 9, 'PASS'),
('732925ITR008', '25ITC31', 35.0, 50.0, 85.0, 'A+', 9, 'PASS');

-- 732925ITR068: Kavin Kumar M
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR068', '25ITT31', 32.0, 45.0, 77.0, 'A', 8, 'PASS'),
('732925ITR068', '25ITT32', 30.0, 42.0, 72.0, 'A', 8, 'PASS'),
('732925ITR068', '25ITT33', 28.0, 38.0, 66.0, 'B+', 7, 'PASS'),
('732925ITR068', '25ITC31', 31.0, 44.0, 75.0, 'A', 8, 'PASS');

-- 732925ITR098: Sanjay P
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR098', '25ITT31', 28.0, 36.0, 64.0, 'B+', 7, 'PASS'),
('732925ITR098', '25ITT32', 26.0, 34.0, 60.0, 'B+', 7, 'PASS'),
('732925ITR098', '25ITT33', 24.0, 30.0, 54.0, 'B', 6, 'PASS'),
('732925ITR098', '25ITC31', 25.0, 32.0, 57.0, 'B', 6, 'PASS');

-- 732925ITR001: Aadhavan V
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR001', '25ITT31', 30.0, 44.0, 74.0, 'A', 8, 'PASS'),
('732925ITR001', '25ITT32', 29.0, 41.0, 70.0, 'A', 8, 'PASS'),
('732925ITR001', '25ITT33', 32.0, 46.0, 78.0, 'A', 8, 'PASS'),
('732925ITR001', '25ITC31', 30.0, 43.0, 73.0, 'A', 8, 'PASS');

-- 732925ITR012: Bavithra S
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR012', '25ITT31', 36.0, 54.0, 90.0, 'O', 10, 'PASS'),
('732925ITR012', '25ITT32', 37.0, 53.0, 90.0, 'O', 10, 'PASS'),
('732925ITR012', '25ITT33', 35.0, 52.0, 87.0, 'A+', 9, 'PASS'),
('732925ITR012', '25ITC31', 36.0, 55.0, 91.0, 'O', 10, 'PASS');

-- 732925ITR024: Dinesh Karthik R (Subject with Low Pass Rate demo)
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR024', '25ITT31', 24.0, 32.0, 56.0, 'B', 6, 'PASS'),
('732925ITR024', '25ITT32', 22.0, 24.0, 46.0, 'RA', 0, 'FAIL'),
('732925ITR024', '25ITT33', 25.0, 35.0, 60.0, 'B+', 7, 'PASS'),
('732925ITR024', '25ITC31', 20.0, 22.0, 42.0, 'RA', 0, 'FAIL');

-- 732925ITR035: Harini K
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR035', '25ITT31', 34.0, 49.0, 83.0, 'A+', 9, 'PASS'),
('732925ITR035', '25ITT32', 33.0, 47.0, 80.0, 'A+', 9, 'PASS'),
('732925ITR035', '25ITT33', 35.0, 50.0, 85.0, 'A+', 9, 'PASS'),
('732925ITR035', '25ITC31', 34.0, 48.0, 82.0, 'A+', 9, 'PASS');

-- 732925ITR042: Karthik N
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR042', '25ITT31', 26.0, 38.0, 64.0, 'B+', 7, 'PASS'),
('732925ITR042', '25ITT32', 25.0, 36.0, 61.0, 'B+', 7, 'PASS'),
('732925ITR042', '25ITT33', 27.0, 39.0, 66.0, 'B+', 7, 'PASS'),
('732925ITR042', '25ITC31', 28.0, 40.0, 68.0, 'B+', 7, 'PASS');

-- 732925ITR050: Lavanya M
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR050', '25ITT31', 37.0, 53.0, 90.0, 'O', 10, 'PASS'),
('732925ITR050', '25ITT32', 36.0, 51.0, 87.0, 'A+', 9, 'PASS'),
('732925ITR050', '25ITT33', 38.0, 54.0, 92.0, 'O', 10, 'PASS'),
('732925ITR050', '25ITC31', 37.0, 52.0, 89.0, 'A+', 9, 'PASS');

-- 732925ITR055: Manoj Kumar T
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR055', '25ITT31', 20.0, 25.0, 45.0, 'RA', 0, 'FAIL'),
('732925ITR055', '25ITT32', 24.0, 30.0, 54.0, 'B', 6, 'PASS'),
('732925ITR055', '25ITT33', 22.0, 28.0, 50.0, 'B', 6, 'PASS'),
('732925ITR055', '25ITC31', 21.0, 26.0, 47.0, 'RA', 0, 'FAIL');

-- 732925ITR062: Nivetha B
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR062', '25ITT31', 35.0, 50.0, 85.0, 'A+', 9, 'PASS'),
('732925ITR062', '25ITT32', 33.0, 48.0, 81.0, 'A+', 9, 'PASS'),
('732925ITR062', '25ITT33', 34.0, 49.0, 83.0, 'A+', 9, 'PASS'),
('732925ITR062', '25ITC31', 35.0, 51.0, 86.0, 'A+', 9, 'PASS');

-- 732925ITR075: Praveen S
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR075', '25ITT31', 27.0, 39.0, 66.0, 'B+', 7, 'PASS'),
('732925ITR075', '25ITT32', 26.0, 35.0, 61.0, 'B+', 7, 'PASS'),
('732925ITR075', '25ITT33', 28.0, 40.0, 68.0, 'B+', 7, 'PASS'),
('732925ITR075', '25ITC31', 29.0, 42.0, 71.0, 'A', 8, 'PASS');

-- 732925ITR082: Rithika G
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR082', '25ITT31', 38.0, 55.0, 93.0, 'O', 10, 'PASS'),
('732925ITR082', '25ITT32', 36.0, 52.0, 88.0, 'A+', 9, 'PASS'),
('732925ITR082', '25ITT33', 37.0, 54.0, 91.0, 'O', 10, 'PASS'),
('732925ITR082', '25ITC31', 39.0, 56.0, 95.0, 'O', 10, 'PASS');

-- 732925ITR090: Sakthi Vel V
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR090', '25ITT31', 25.0, 32.0, 57.0, 'B', 6, 'PASS'),
('732925ITR090', '25ITT32', 23.0, 28.0, 51.0, 'B', 6, 'PASS'),
('732925ITR090', '25ITT33', 26.0, 34.0, 60.0, 'B+', 7, 'PASS'),
('732925ITR090', '25ITC31', 24.0, 30.0, 54.0, 'B', 6, 'PASS');

-- 732925ITR102: Vigneshwaran K
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ITR102', '25ITT31', 22.0, 25.0, 47.0, 'RA', 0, 'FAIL'),
('732925ITR102', '25ITT32', 28.0, 38.0, 66.0, 'B+', 7, 'PASS'),
('732925ITR102', '25ITT33', 24.0, 31.0, 55.0, 'B', 6, 'PASS'),
('732925ITR102', '25ITC31', 26.0, 35.0, 61.0, 'B+', 7, 'PASS');

-- CSE Students
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925CSR015', '25CS31', 35.0, 50.0, 85.0, 'A+', 9, 'PASS'),
('732925CSR045', '25CS31', 31.0, 42.0, 73.0, 'A', 8, 'PASS');

-- ECE Students
INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) VALUES
('732925ECR020', '25EC31', 33.0, 45.0, 78.0, 'A', 8, 'PASS'),
('732925ECR052', '25EC31', 28.0, 38.0, 66.0, 'B+', 7, 'PASS');
