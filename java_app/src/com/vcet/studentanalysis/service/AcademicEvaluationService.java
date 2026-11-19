package com.vcet.studentanalysis.service;

import com.vcet.studentanalysis.exception.DatabaseOperationException;
import com.vcet.studentanalysis.exception.InvalidMarkException;
import com.vcet.studentanalysis.exception.StudentNotFoundException;
import com.vcet.studentanalysis.model.Mark;
import com.vcet.studentanalysis.model.Student;
import com.vcet.studentanalysis.model.StudentResult;

import java.util.List;
import java.util.Map;

public interface AcademicEvaluationService {
    void registerStudent(Student student) throws DatabaseOperationException;
    void enterMarks(String registerNo, String subjectCode, double internalMarks, double externalMarks)
            throws InvalidMarkException, DatabaseOperationException, StudentNotFoundException;

    String computeGrade(double totalMarks, double externalMarks);
    int computeGradePoint(String grade);
    String computePassFailStatus(double totalMarks, double externalMarks);

    StudentResult generateStudentMarksheet(String registerNo)
            throws StudentNotFoundException, DatabaseOperationException;

    List<StudentResult> getDepartmentRankList(int deptId) throws DatabaseOperationException;
    List<Map<String, Object>> getSubjectWisePassRates() throws DatabaseOperationException;
    List<Map<String, Object>> getDepartmentSummary() throws DatabaseOperationException;

    // Advanced Streams and Functional Analytics
    double calculateOverallClassAverage() throws DatabaseOperationException;
    List<StudentResult> filterStudentsWithArrears() throws DatabaseOperationException;
    List<StudentResult> getTopDistinctionStudents(int limit) throws DatabaseOperationException;
}
