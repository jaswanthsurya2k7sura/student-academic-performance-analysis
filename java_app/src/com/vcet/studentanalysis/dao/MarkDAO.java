package com.vcet.studentanalysis.dao;

import com.vcet.studentanalysis.exception.DatabaseOperationException;
import com.vcet.studentanalysis.model.Mark;
import com.vcet.studentanalysis.model.StudentResult;

import java.util.List;
import java.util.Map;

public interface MarkDAO {
    void recordMarks(Mark mark) throws DatabaseOperationException;
    List<Mark> getMarksByStudent(String registerNo) throws DatabaseOperationException;
    List<Mark> getMarksBySubject(String subjectCode) throws DatabaseOperationException;
    StudentResult getStudentSemesterSummary(String registerNo) throws DatabaseOperationException;
    List<StudentResult> getAllStudentSummaries() throws DatabaseOperationException;
    List<Map<String, Object>> getSubjectPerformanceSummary() throws DatabaseOperationException;
    List<Map<String, Object>> getDepartmentSummary() throws DatabaseOperationException;
}
