package com.vcet.studentanalysis;

import com.vcet.studentanalysis.dao.StudentDAO;
import com.vcet.studentanalysis.dao.SubjectDAO;
import com.vcet.studentanalysis.dao.impl.StudentDAOImpl;
import com.vcet.studentanalysis.dao.impl.SubjectDAOImpl;
import com.vcet.studentanalysis.model.Mark;
import com.vcet.studentanalysis.model.Student;
import com.vcet.studentanalysis.model.StudentResult;
import com.vcet.studentanalysis.service.AcademicEvaluationService;
import com.vcet.studentanalysis.service.impl.AcademicEvaluationServiceImpl;
import com.vcet.studentanalysis.util.TableFormatter;

import java.util.List;
import java.util.Map;

/**
 * JavaEngineRunner: Command-line execution bridge and faculty demonstration runner.
 * Demonstrates:
 * - OOP: Classes, Encapsulation, Inheritance, Polymorphism
 * - Java 21 Streams API & Lambdas
 * - JDBC Data Access via DAO pattern
 * - Formatted Tabular Console Output
 */
public class JavaEngineRunner {

    public static void main(String[] args) {
        String action = "test";
        String regNo = "732925ITR038";

        for (String arg : args) {
            if (arg.startsWith("--action=")) {
                action = arg.substring("--action=".length()).trim();
            } else if (arg.startsWith("--reg=")) {
                regNo = arg.substring("--reg=".length()).trim();
            }
        }

        AcademicEvaluationService service = new AcademicEvaluationServiceImpl();
        StudentDAO studentDAO = new StudentDAOImpl();
        SubjectDAO subjectDAO = new SubjectDAOImpl();

        try {
            switch (action.toLowerCase()) {
                case "test" -> runGeneralTest(service, studentDAO, subjectDAO);
                case "marksheet" -> runMarksheet(service, regNo);
                case "ranklist" -> runRankList(service);
                case "lowpass" -> runLowPassRate(service);
                case "deptsummary" -> runDepartmentSummary(service);
                case "streams-analytics" -> runStreamsAnalytics(service);
                default -> {
                    System.out.println("[!] Unknown action: " + action);
                    printUsage();
                }
            }
        } catch (Exception e) {
            System.err.println("[JAVA ERROR] Execution failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runGeneralTest(AcademicEvaluationService service, StudentDAO studentDAO, SubjectDAO subjectDAO) throws Exception {
        System.out.println("=========================================================================");
        System.out.println("   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY - JAVA 21 ENGINE RUNNER");
        System.out.println("   Demonstrating: OOP, Inheritance, Interfaces, DAO & Streams API");
        System.out.println("=========================================================================");

        List<Student> students = studentDAO.getAllStudents();
        System.out.println("[✓] JDBC Connection Verified. Total Students in DB: " + students.size());

        // Polymorphism demonstration
        if (!students.isEmpty()) {
            Student s = students.get(0);
            System.out.println("[✓] Polymorphism & Inheritance Demo:");
            System.out.println("    Entity Class: " + s.getClass().getName());
            System.out.println("    Superclass  : " + s.getClass().getSuperclass().getName());
            System.out.println("    Role Output : " + s.getRoleDescription());
        }

        double classAvg = service.calculateOverallClassAverage();
        System.out.printf("[✓] Streams API Average Calculation: %.2f%%\n", classAvg);
        System.out.println("=========================================================================");
    }

    private static void runMarksheet(AcademicEvaluationService service, String regNo) throws Exception {
        StudentResult result = service.generateStudentMarksheet(regNo);
        System.out.println("=========================================================================");
        System.out.println("            JAVA ENGINE: SEMESTER EXAMINATION MARKSHEET");
        System.out.println("=========================================================================");
        System.out.printf(" Reg No: %-18s Name: %s\n", result.getRegisterNo(), result.getStudentName());
        System.out.printf(" Dept  : %-18s Class/Sec: %s\n", result.getDeptCode(), result.getSection());
        System.out.println("-------------------------------------------------------------------------");

        TableFormatter table = new TableFormatter("Subject Code", "Subject Name", "Credits", "Int (40)", "Ext (60)", "Total", "Grade", "GP", "Status");
        for (Mark m : result.getMarkList()) {
            table.addRow(m.getSubjectCode(), m.getSubjectName(), String.valueOf(m.getCredits()),
                    String.format("%.1f", m.getInternalMarks()), String.format("%.1f", m.getExternalMarks()),
                    String.format("%.1f", m.getTotalMarks()), m.getGrade(), String.valueOf(m.getGradePoint()), m.getStatus());
        }
        table.print();

        System.out.printf(" Grand Total: %.1f / 400.0   Average: %.2f%%\n", result.getGrandTotal(), result.getAverageMarks());
        System.out.printf(" Passed: %d                  Failed: %d\n", result.getSubjectsPassed(), result.getSubjectsFailed());
        System.out.printf(" GPA: %.2f                   Overall Status: %s\n", result.getGpa(), result.getOverallStatus());
        System.out.println("=========================================================================");
    }

    private static void runRankList(AcademicEvaluationService service) throws Exception {
        List<StudentResult> rankList = service.getDepartmentRankList(1);
        System.out.println(">>> JAVA 21 STREAMS API MERIT RANK LIST (Sorted by GPA & Total Marks)\n");
        TableFormatter table = new TableFormatter("Rank", "Register No", "Student Name", "Sec", "Total Marks", "Average %", "GPA", "Status");
        int rank = 1;
        for (StudentResult sr : rankList) {
            table.addRow(String.valueOf(rank++), sr.getRegisterNo(), sr.getStudentName(), sr.getSection(),
                    String.format("%.1f", sr.getGrandTotal()), String.format("%.2f%%", sr.getAverageMarks()),
                    String.format("%.2f", sr.getGpa()), sr.getOverallStatus());
        }
        table.print();
    }

    private static void runLowPassRate(AcademicEvaluationService service) throws Exception {
        List<Map<String, Object>> list = service.getSubjectWisePassRates();
        System.out.println(">>> SUBJECT PERFORMANCE & LOW PASS RATE IDENTIFIER (< 85%)\n");
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
    }

    private static void runDepartmentSummary(AcademicEvaluationService service) throws Exception {
        List<Map<String, Object>> list = service.getDepartmentSummary();
        System.out.println(">>> DEPARTMENT-WISE PERFORMANCE SUMMARY REPORT\n");
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

    private static void runStreamsAnalytics(AcademicEvaluationService service) throws Exception {
        System.out.println("=========================================================================");
        System.out.println("       JAVA 21 STREAMS API & LAMBDAS ANALYTICS DEMONSTRATION");
        System.out.println("=========================================================================");

        double overallAvg = service.calculateOverallClassAverage();
        System.out.printf("1. Overall Class Average Marks (Stream mapToDouble & average): %.2f%%\n", overallAvg);

        List<StudentResult> distinction = service.getTopDistinctionStudents(5);
        System.out.println("\n2. Top Distinction Students (filter(gpa >= 8.5).sorted().limit(5)):");
        for (StudentResult s : distinction) {
            System.out.printf("   - %s (%s) | GPA: %.2f | Total: %.1f\n", s.getStudentName(), s.getRegisterNo(), s.getGpa(), s.getGrandTotal());
        }

        List<StudentResult> arrears = service.filterStudentsWithArrears();
        System.out.println("\n3. Students Requiring Academic Remediation (filter(arrears > 0)):");
        for (StudentResult s : arrears) {
            System.out.printf("   - %s (%s) | Failed Subjects: %d | GPA: %.2f\n", s.getStudentName(), s.getRegisterNo(), s.getSubjectsFailed(), s.getGpa());
        }
        System.out.println("=========================================================================");
    }

    private static void printUsage() {
        System.out.println("Usage: java -cp \"bin;lib/*\" com.vcet.studentanalysis.JavaEngineRunner --action=<action> [--reg=<regNo>]");
        System.out.println("Available actions: test, marksheet, ranklist, lowpass, deptsummary, streams-analytics");
    }
}
