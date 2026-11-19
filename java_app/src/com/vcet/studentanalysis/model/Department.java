package com.vcet.studentanalysis.model;

public class Department {
    private int deptId;
    private String deptCode;
    private String deptName;
    private String hodName;

    public Department() {}

    public Department(int deptId, String deptCode, String deptName, String hodName) {
        this.deptId = deptId;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.hodName = hodName;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getHodName() {
        return hodName;
    }

    public void setHodName(String hodName) {
        this.hodName = hodName;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (HOD: %s)", deptCode, deptName, hodName);
    }
}
