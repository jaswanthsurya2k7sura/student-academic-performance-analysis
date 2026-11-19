package com.vcet.studentanalysis.dao;

import com.vcet.studentanalysis.exception.DatabaseOperationException;
import com.vcet.studentanalysis.model.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectDAO {
    void addSubject(Subject subject) throws DatabaseOperationException;
    Optional<Subject> getSubjectByCode(String subjectCode) throws DatabaseOperationException;
    List<Subject> getAllSubjects() throws DatabaseOperationException;
    List<Subject> getSubjectsByDepartment(int deptId) throws DatabaseOperationException;
}
