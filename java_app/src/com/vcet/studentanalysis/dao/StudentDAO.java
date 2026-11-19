package com.vcet.studentanalysis.dao;

import com.vcet.studentanalysis.exception.DatabaseOperationException;
import com.vcet.studentanalysis.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDAO {
    void addStudent(Student student) throws DatabaseOperationException;
    Optional<Student> getStudentByRegisterNo(String registerNo) throws DatabaseOperationException;
    List<Student> getAllStudents() throws DatabaseOperationException;
    List<Student> getStudentsByDepartment(int deptId) throws DatabaseOperationException;
    boolean deleteStudent(String registerNo) throws DatabaseOperationException;
}
