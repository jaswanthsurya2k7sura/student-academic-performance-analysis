package com.vcet.studentanalysis.ui;

import com.vcet.studentanalysis.dao.StudentDAO;
import com.vcet.studentanalysis.dao.SubjectDAO;
import com.vcet.studentanalysis.dao.impl.StudentDAOImpl;
import com.vcet.studentanalysis.dao.impl.SubjectDAOImpl;
import com.vcet.studentanalysis.exception.InvalidMarkException;
import com.vcet.studentanalysis.exception.StudentNotFoundException;
import com.vcet.studentanalysis.model.Mark;
import com.vcet.studentanalysis.model.Student;
import com.vcet.studentanalysis.model.StudentResult;
import com.vcet.studentanalysis.model.Subject;
import com.vcet.studentanalysis.service.AcademicEvaluationService;
import com.vcet.studentanalysis.service.impl.AcademicEvaluationServiceImpl;
import com.vcet.studentanalysis.util.TableFormatter;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {
    private final AcademicEvaluationService evaluationService;
    private final StudentDAO studentDAO;
    private final SubjectDAO subjectDAO;
    private final Scanner scanner;

    public ConsoleApp() {
        this.evaluationService = new AcademicEvaluationServiceImpl();
        this.studentDAO = new StudentDAOImpl();
        this.subjectDAO = new SubjectDAOImpl();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Select an option [1-10]: ");
            String choice = scanner.nextLine().trim();
            System.out.println();

            try {
                switch (choice) {
                    case "1" -> viewAllStudents();
                    case "2" -> viewStudentDetails();
                    case "3" -> registerNewStudent();
                    case "4" -> viewAllSubjects();
                    case "5" -> recordStudentMarks();
                    case "6" -> generateStudentMarksheet();
                    case "7" -> viewDepartmentRankList();
                    case "8" -> viewSubjectPassRateAnalysis();
                    case "9" -> viewDepartmentSummary();
                    case "10" -> {
                        System.out.println("Thank you for using the VCET Student Academic Performance System. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("[!] Invalid option. Please enter a number between 1 and 10.");
                }
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
            if (running) {
                System.out.println("\nPress ENTER to continue...");
                scanner.nextLine();
            }
        }
    }

    private void printBanner() {
        System.out.println("=================================================================================");
        System.out.println("        VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY, ERODE - 12");
        System.out.println("                 DEPARTMENT OF INFORMATION TECHNOLOGY");
        System.out.println("      SCENARIO 8: STUDENT ACADEMIC PERFORMANCE & RESULT ANALYSIS SYSTEM");
        System.out.println("       Developer: Jaswanth surya S (Reg No: 732925ITR038 | Class: 25IT3A)");
        System.out.println("=================================================================================");
    }

    private void printMainMenu() {
        System.out.println("\n----------------------------- MAIN MENU -----------------------------");
        System.out.println(" 1. View All Students");
        System.out.println(" 2. Search Student Profile by Register Number");
        System.out.println(" 3. Register New Student");
        System.out.println(" 4. View Curriculum Subjects");
        System.out.println(" 5. Record / Update Student Marks");
        System.out.println(" 6. Generate Individual Student Marksheet & GPA (Student-wise Report)");
        System.out.println(" 7. View Department Rank List & Distinction Students");
        System.out.println(" 8. Analyze Subject Performance & Identify Low Pass Rates");
        System.out.println(" 9. View Department-wise Summary Report");
        System.out.println(" 10. Exit Application");
        System.out.println("---------------------------------------------------------------------");
    }

    private void viewAllStudents() throws Exception {
        List<Student> students = studentDAO.getAllStudents();
        System.out.println(">>> TOTAL REGISTERED STUDENTS: " + students.size() + "\n");
        TableFormatter table = new TableFormatter("Register No", "Student Name", "Dept", "Sec", "Year", "Email");
        for (Student s : students) {
            table.addRow(s.getRegisterNo(), s.getName(), s.getDeptCode(), s.getSection(),
                    String.valueOf(s.getYearOfStudy()), s.getEmail());
        }
        table.print();
    }

    private void viewStudentDetails() throws Exception {
        System.out.print("Enter Student Register No (e.g. 732925ITR038): ");
        String regNo = scanner.nextLine().trim();
        Student s = studentDAO.getStudentByRegisterNo(regNo)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with Reg No: " + regNo));

        System.out.println("\n------------- STUDENT PROFILE -------------");
        System.out.println("Register No   : " + s.getRegisterNo());
        System.out.println("Full Name     : " + s.getName());
        System.out.println("Department    : " + s.getDeptCode());
        System.out.println("Section / Class: " + s.getSection());
        System.out.println("Year of Study : " + s.getYearOfStudy());
        System.out.println("Email Address : " + s.getEmail());
        System.out.println("Phone Number  : " + s.getPhone());
        System.out.println("Role          : " + s.getRoleDescription());
        System.out.println("------------------------------------------");
    }

    private void registerNewStudent() throws Exception {
        System.out.println(">>> REGISTER NEW STUDENT PROFILE");
        System.out.print("Enter Register No: ");
        String regNo = scanner.nextLine().trim();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Department ID (1 for IT, 2 for CSE, 3 for ECE): ");
        int deptId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter Year of Study (1-4): ");
        int year = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter Section (e.g. 25IT3A): ");
        String section = scanner.nextLine().trim();
        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Contact Phone: ");
        String phone = scanner.nextLine().trim();

        Student student = new Student(regNo, name, deptId, year, section, email, phone);
        evaluationService.registerStudent(student);
        System.out.println("[SUCCESS] Student " + name + " (" + regNo + ") registered successfully!");
    }

    private void viewAllSubjects() throws Exception {
        List<Subject> subjects = subjectDAO.getAllSubjects();
        System.out.println(">>> CURRICULUM SUBJECT DETAILS\n");
        TableFormatter table = new TableFormatter("Subject Code", "Subject Name", "Dept", "Sem", "Credits", "Max Internal", "Max External", "Max Total");
        for (Subject sub : subjects) {
            table.addRow(sub.getSubjectCode(), sub.getSubjectName(), sub.getDeptCode(),
                    String.valueOf(sub.getSemester()), String.valueOf(sub.getCredits()),
                    String.format("%.0f", sub.getMaxInternal()), String.format("%.0f", sub.getMaxExternal()),
                    String.format("%.0f", sub.getMaxTotal()));
        }
        table.print();
    }

    private void recordStudentMarks() throws Exception {
        System.out.println(">>> RECORD / UPDATE STUDENT MARKS");
        System.out.print("Enter Register No (e.g. 732925ITR038): ");
        String regNo = scanner.nextLine().trim();
        System.out.print("Enter Subject Code (e.g. 25ITT31): ");
        String subjectCode = scanner.nextLine().trim();
        System.out.print("Enter Internal Assessment Marks (out of 40): ");
        double internal = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Enter Semester Exam Marks (out of 60): ");
        double external = Double.parseDouble(scanner.nextLine().trim());

        evaluationService.enterMarks(regNo, subjectCode, internal, external);
        System.out.println("[SUCCESS] Marks recorded and performance metrics calculated successfully!");
    }

    private void generateStudentMarksheet() throws Exception {
        System.out.print("Enter Student Register No (e.g. 732925ITR038): ");
        String regNo = scanner.nextLine().trim();
        StudentResult result = evaluationService.generateStudentMarksheet(regNo);

        System.out.println("\n=========================================================================");
        System.out.println("                   SEMESTER EXAMINATION MARKSHEET");
        System.out.println("=========================================================================");
        System.out.printf(" Register No : %-20s  Student Name : %s\n", result.getRegisterNo(), result.getStudentName());
        System.out.printf(" Department  : %-20s  Class/Section: %s\n", result.getDeptCode(), result.getSection());
        System.out.println("-------------------------------------------------------------------------");

        TableFormatter table = new TableFormatter("Subject Code", "Subject Name", "Credits", "Internal (40)", "External (60)", "Total (100)", "Grade", "GP", "Status");
        for (Mark m : result.getMarkList()) {
            table.addRow(m.getSubjectCode(), m.getSubjectName(), String.valueOf(m.getCredits()),
                    String.format("%.1f", m.getInternalMarks()), String.format("%.1f", m.getExternalMarks()),
                    String.format("%.1f", m.getTotalMarks()), m.getGrade(), String.valueOf(m.getGradePoint()), m.getStatus());
        }
        table.print();

        System.out.println("-------------------------------------------------------------------------");
        System.out.printf(" Grand Total   : %.1f / 400.0          Average Marks: %.2f%%\n", result.getGrandTotal(), result.getAverageMarks());
        System.out.printf(" Passed Subjects: %d                   Failed (Arrears): %d\n", result.getSubjectsPassed(), result.getSubjectsFailed());
        System.out.printf(" Grade Point Average (GPA): %.2f       Semester Result : %s\n", result.getGpa(), result.getOverallStatus());
        System.out.println("=========================================================================");
    }

    private void viewDepartmentRankList() throws Exception {
        List<StudentResult> rankList = evaluationService.getDepartmentRankList(1);
        System.out.println(">>> DEPARTMENT MERIT / RANK LIST (All Clear Candidates)\n");
        TableFormatter table = new TableFormatter("Rank", "Register No", "Student Name", "Sec", "Total Marks", "Average %", "GPA", "Status");
        int rank = 1;
        for (StudentResult sr : rankList) {
            table.addRow(String.valueOf(rank++), sr.getRegisterNo(), sr.getStudentName(), sr.getSection(),
                    String.format("%.1f", sr.getGrandTotal()), String.format("%.2f%%", sr.getAverageMarks()),
                    String.format("%.2f", sr.getGpa()), sr.getOverallStatus());
        }
        table.print();

        List<StudentResult> arrears = evaluationService.filterStudentsWithArrears();
        if (!arrears.isEmpty()) {
            System.out.println("\n>>> CANDIDATES REQUIRING ACADEMIC REMEDIATION (ARREARS)");
            TableFormatter arrearTable = new TableFormatter("Register No", "Student Name", "Sec", "Total Marks", "Failed Subjects", "GPA");
            for (StudentResult sr : arrears) {
                arrearTable.addRow(sr.getRegisterNo(), sr.getStudentName(), sr.getSection(),
                        String.format("%.1f", sr.getGrandTotal()), String.valueOf(sr.getSubjectsFailed()),
                        String.format("%.2f", sr.getGpa()));
            }
            arrearTable.print();
        }
    }

    private void viewSubjectPassRateAnalysis() throws Exception {
        List<Map<String, Object>> list = evaluationService.getSubjectWisePassRates();
        System.out.println(">>> SUBJECT PERFORMANCE & LOW PASS RATE IDENTIFIER\n");
        TableFormatter table = new TableFormatter("Subject Code", "Subject Name", "Credits", "Appeared", "Passed", "Failed", "Pass Rate (%)", "Avg Marks", "Flag");
        for (Map<String, Object> map : list) {
            double passRate = (Double) map.get("pass_percentage");
            String flag = (passRate < 85.0) ? "LOW PASS RATE" : "NORMAL";
            table.addRow(
                    (String) map.get("subject_code"),
                    (String) map.get("subject_name"),
                    String.valueOf(map.get("credits")),
                    String.valueOf(map.get("total_appeared")),
                    String.valueOf(map.get("total_passed")),
                    String.valueOf(map.get("total_failed")),
                    String.format("%.2f%%", passRate),
                    String.format("%.2f", (Double) map.get("avg_marks")),
                    flag
            );
        }
        table.print();
        System.out.println("[NOTE] Subjects flagged as 'LOW PASS RATE' (< 85%) require faculty review and remedial classes.");
    }

    private void viewDepartmentSummary() throws Exception {
        List<Map<String, Object>> list = evaluationService.getDepartmentSummary();
        System.out.println(">>> DEPARTMENT-WISE ACADEMIC PERFORMANCE SUMMARY\n");
        TableFormatter table = new TableFormatter("Dept Code", "Department Name", "Students Count", "Evaluations", "Avg Marks", "Overall Pass %");
        for (Map<String, Object> map : list) {
            table.addRow(
                    (String) map.get("dept_code"),
                    (String) map.get("dept_name"),
                    String.valueOf(map.get("total_students")),
                    String.valueOf(map.get("total_evaluations")),
                    String.format("%.2f", (Double) map.get("dept_avg_marks")),
                    String.format("%.2f%%", (Double) map.get("overall_pass_percentage"))
            );
        }
        table.print();
    }
}
