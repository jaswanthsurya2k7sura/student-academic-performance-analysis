package com.vcet.studentanalysis.model;

/**
 * Subject Entity class representing curriculum course details.
 */
public class Subject {
    private String subjectCode;
    private String subjectName;
    private int deptId;
    private String deptCode;
    private int semester;
    private int credits;
    private double maxInternal;
    private double maxExternal;

    public Subject() {
        this.maxInternal = 40.0;
        this.maxExternal = 60.0;
    }

    public Subject(String subjectCode, String subjectName, int deptId, int semester, int credits, double maxInternal, double maxExternal) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.deptId = deptId;
        this.semester = semester;
        this.credits = credits;
        this.maxInternal = maxInternal;
        this.maxExternal = maxExternal;
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

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public double getMaxInternal() {
        return maxInternal;
    }

    public void setMaxInternal(double maxInternal) {
        this.maxInternal = maxInternal;
    }

    public double getMaxExternal() {
        return maxExternal;
    }

    public void setMaxExternal(double maxExternal) {
        this.maxExternal = maxExternal;
    }

    public double getMaxTotal() {
        return maxInternal + maxExternal;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (Credits: %d, Max: %.0f+%.0f)",
                subjectCode, subjectName, credits, maxInternal, maxExternal);
    }
}
