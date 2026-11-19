package com.vcet.studentanalysis.model;

/**
 * Mark Entity class holding internal, external and computed evaluation metrics.
 */
public class Mark {
    private int markId;
    private String registerNo;
    private String subjectCode;
    private String subjectName;
    private int credits;
    private double internalMarks;
    private double externalMarks;
    private double totalMarks;
    private String grade;
    private int gradePoint;
    private String status;

    public Mark() {}

    public Mark(String registerNo, String subjectCode, double internalMarks, double externalMarks) {
        this.registerNo = registerNo;
        this.subjectCode = subjectCode;
        this.internalMarks = internalMarks;
        this.externalMarks = externalMarks;
        this.totalMarks = internalMarks + externalMarks;
    }

    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public double getInternalMarks() {
        return internalMarks;
    }

    public void setInternalMarks(double internalMarks) {
        this.internalMarks = internalMarks;
    }

    public double getExternalMarks() {
        return externalMarks;
    }

    public void setExternalMarks(double externalMarks) {
        this.externalMarks = externalMarks;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(int gradePoint) {
        this.gradePoint = gradePoint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("[%s] Int: %.1f, Ext: %.1f, Total: %.1f, Grade: %s, Status: %s",
                subjectCode, internalMarks, externalMarks, totalMarks, grade, status);
    }
}
