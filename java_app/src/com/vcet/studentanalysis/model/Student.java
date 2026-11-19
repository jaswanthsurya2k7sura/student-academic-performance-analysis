package com.vcet.studentanalysis.model;

/**
 * Student Entity class inheriting from Person.
 * Implements functional requirements for student profile management.
 */
public class Student extends Person {
    private int deptId;
    private String deptCode;
    private int yearOfStudy;
    private String section;

    public Student() {
        super();
    }

    public Student(String registerNo, String name, int deptId, int yearOfStudy, String section, String email, String phone) {
        super(registerNo, name, email, phone);
        this.deptId = deptId;
        this.yearOfStudy = yearOfStudy;
        this.section = section;
    }

    public String getRegisterNo() {
        return getId();
    }

    public void setRegisterNo(String registerNo) {
        setId(registerNo);
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

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public String getRoleDescription() {
        return "Undergraduate Engineering Student (Class: " + section + ", Year: " + yearOfStudy + ")";
    }

    @Override
    public String toString() {
        return String.format("Student[RegNo=%s, Name=%s, Section=%s, Year=%d]",
                getRegisterNo(), getName(), section, yearOfStudy);
    }
}
