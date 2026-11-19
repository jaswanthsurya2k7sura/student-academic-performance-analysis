package com.vcet.studentanalysis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Result DTO containing consolidated performance metrics for a student.
 */
public class StudentResult {
    private String registerNo;
    private String studentName;
    private String section;
    private String deptCode;
    private List<Mark> markList;
    private double grandTotal;
    private double averageMarks;
    private double gpa;
    private int subjectsPassed;
    private int subjectsFailed;
    private String overallStatus;

    public StudentResult() {
        this.markList = new ArrayList<>();
    }

    public StudentResult(String registerNo, String studentName, String section, String deptCode) {
        this.registerNo = registerNo;
        this.studentName = studentName;
        this.section = section;
        this.deptCode = deptCode;
        this.markList = new ArrayList<>();
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public List<Mark> getMarkList() {
        return markList;
    }

    public void setMarkList(List<Mark> markList) {
        this.markList = markList;
    }

    public void addMark(Mark mark) {
        this.markList.add(mark);
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public double getAverageMarks() {
        return averageMarks;
    }

    public void setAverageMarks(double averageMarks) {
        this.averageMarks = averageMarks;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getSubjectsPassed() {
        return subjectsPassed;
    }

    public void setSubjectsPassed(int subjectsPassed) {
        this.subjectsPassed = subjectsPassed;
    }

    public int getSubjectsFailed() {
        return subjectsFailed;
    }

    public void setSubjectsFailed(int subjectsFailed) {
        this.subjectsFailed = subjectsFailed;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }
}
