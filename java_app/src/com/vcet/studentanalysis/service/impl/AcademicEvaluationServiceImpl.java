package com.vcet.studentanalysis.service.impl;

import com.vcet.studentanalysis.dao.MarkDAO;
import com.vcet.studentanalysis.dao.StudentDAO;
import com.vcet.studentanalysis.dao.SubjectDAO;
import com.vcet.studentanalysis.dao.impl.MarkDAOImpl;
import com.vcet.studentanalysis.dao.impl.StudentDAOImpl;
import com.vcet.studentanalysis.dao.impl.SubjectDAOImpl;
import com.vcet.studentanalysis.exception.DatabaseOperationException;
import com.vcet.studentanalysis.exception.InvalidMarkException;
import com.vcet.studentanalysis.exception.StudentNotFoundException;
import com.vcet.studentanalysis.model.Mark;
import com.vcet.studentanalysis.model.Student;
import com.vcet.studentanalysis.model.StudentResult;
import com.vcet.studentanalysis.service.AcademicEvaluationService;
import com.vcet.studentanalysis.util.InputValidator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of Academic Evaluation Service.
 * Demonstrates:
 * - Java 25ITT32 Unit 5: Stream API (filter, map, sorted, collect, reduction)
 * - Java Lambdas and functional programming
 * - Custom Exception handling
 */
public class AcademicEvaluationServiceImpl implements AcademicEvaluationService {

    private final StudentDAO studentDAO;
    private final SubjectDAO subjectDAO;
    private final MarkDAO markDAO;

    public AcademicEvaluationServiceImpl() {
        this.studentDAO = new StudentDAOImpl();
        this.subjectDAO = new SubjectDAOImpl();
        this.markDAO = new MarkDAOImpl();
    }

    public AcademicEvaluationServiceImpl(StudentDAO studentDAO, SubjectDAO subjectDAO, MarkDAO markDAO) {
        this.studentDAO = studentDAO;
        this.subjectDAO = subjectDAO;
        this.markDAO = markDAO;
    }

    @Override
    public void registerStudent(Student student) throws DatabaseOperationException {
        InputValidator.validateRegisterNo(student.getRegisterNo());
        studentDAO.addStudent(student);
    }

    @Override
    public void enterMarks(String registerNo, String subjectCode, double internalMarks, double externalMarks)
            throws InvalidMarkException, DatabaseOperationException, StudentNotFoundException {
        // 1. Validation
        InputValidator.validateMarks(internalMarks, externalMarks);

        // 2. Verify student exists
        studentDAO.getStudentByRegisterNo(registerNo)
                .orElseThrow(() -> new StudentNotFoundException("Student with Reg No '" + registerNo + "' not found."));

        // 3. Verify subject exists
        subjectDAO.getSubjectByCode(subjectCode)
                .orElseThrow(() -> new DatabaseOperationException("Subject code '" + subjectCode + "' does not exist."));

        // 4. Calculate total, grade, grade point, status
        double total = internalMarks + externalMarks;
        String grade = computeGrade(total, externalMarks);
        int gradePoint = computeGradePoint(grade);
        String status = computePassFailStatus(total, externalMarks);

        Mark mark = new Mark();
        mark.setRegisterNo(registerNo);
        mark.setSubjectCode(subjectCode);
        mark.setInternalMarks(internalMarks);
        mark.setExternalMarks(externalMarks);
        mark.setTotalMarks(total);
        mark.setGrade(grade);
        mark.setGradePoint(gradePoint);
        mark.setStatus(status);

        // 5. Persist via DAO
        markDAO.recordMarks(mark);
    }

    @Override
    public String computeGrade(double totalMarks, double externalMarks) {
        // Passing rule: Min 45% in semester exam (27/60) and 50% overall
        if (externalMarks < 27.0 || totalMarks < 50.0) {
            return "RA";
        }
        if (totalMarks >= 90.0) return "O";
        if (totalMarks >= 80.0) return "A+";
        if (totalMarks >= 70.0) return "A";
        if (totalMarks >= 60.0) return "B+";
        return "B";
    }

    @Override
    public int computeGradePoint(String grade) {
        return switch (grade) {
            case "O" -> 10;
            case "A+" -> 9;
            case "A" -> 8;
            case "B+" -> 7;
            case "B" -> 6;
            default -> 0;
        };
    }

    @Override
    public String computePassFailStatus(double totalMarks, double externalMarks) {
        return (externalMarks >= 27.0 && totalMarks >= 50.0) ? "PASS" : "FAIL";
    }

    @Override
    public StudentResult generateStudentMarksheet(String registerNo)
            throws StudentNotFoundException, DatabaseOperationException {
        Student student = studentDAO.getStudentByRegisterNo(registerNo)
                .orElseThrow(() -> new StudentNotFoundException("Student with Reg No '" + registerNo + "' not found."));

        StudentResult summary = markDAO.getStudentSemesterSummary(registerNo);
        if (summary == null) {
            // New student with no marks yet
            summary = new StudentResult(student.getRegisterNo(), student.getName(), student.getSection(), student.getDeptCode());
            summary.setOverallStatus("PENDING");
        }
        return summary;
    }

    @Override
    public List<StudentResult> getDepartmentRankList(int deptId) throws DatabaseOperationException {
        List<StudentResult> allSummaries = markDAO.getAllStudentSummaries();

        // Using Java Streams API to filter, sort and collect
        return allSummaries.stream()
                .filter(s -> "PASS".equalsIgnoreCase(s.getOverallStatus()))
                .sorted(Comparator.comparingDouble(StudentResult::getGpa).reversed()
                        .thenComparing(Comparator.comparingDouble(StudentResult::getGrandTotal).reversed()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getSubjectWisePassRates() throws DatabaseOperationException {
        return markDAO.getSubjectPerformanceSummary();
    }

    @Override
    public List<Map<String, Object>> getDepartmentSummary() throws DatabaseOperationException {
        return markDAO.getDepartmentSummary();
    }

    @Override
    public double calculateOverallClassAverage() throws DatabaseOperationException {
        List<StudentResult> list = markDAO.getAllStudentSummaries();
        return list.stream()
                .mapToDouble(StudentResult::getAverageMarks)
                .average()
                .orElse(0.0);
    }

    @Override
    public List<StudentResult> filterStudentsWithArrears() throws DatabaseOperationException {
        return markDAO.getAllStudentSummaries().stream()
                .filter(s -> s.getSubjectsFailed() > 0)
                .sorted(Comparator.comparingInt(StudentResult::getSubjectsFailed).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResult> getTopDistinctionStudents(int limit) throws DatabaseOperationException {
        return markDAO.getAllStudentSummaries().stream()
                .filter(s -> s.getGpa() >= 8.5)
                .sorted(Comparator.comparingDouble(StudentResult::getGpa).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
