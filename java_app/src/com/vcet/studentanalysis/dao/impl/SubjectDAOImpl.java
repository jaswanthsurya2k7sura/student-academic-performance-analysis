package com.vcet.studentanalysis.dao.impl;

import com.vcet.studentanalysis.dao.SubjectDAO;
import com.vcet.studentanalysis.exception.DatabaseOperationException;
import com.vcet.studentanalysis.model.Subject;
import com.vcet.studentanalysis.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubjectDAOImpl implements SubjectDAO {

    @Override
    public void addSubject(Subject subject) throws DatabaseOperationException {
        String sql = "INSERT INTO subjects (subject_code, subject_name, dept_id, semester, credits, max_internal, max_external) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subject.getSubjectCode());
            ps.setString(2, subject.getSubjectName());
            ps.setInt(3, subject.getDeptId());
            ps.setInt(4, subject.getSemester());
            ps.setInt(5, subject.getCredits());
            ps.setDouble(6, subject.getMaxInternal());
            ps.setDouble(7, subject.getMaxExternal());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error adding subject: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Subject> getSubjectByCode(String subjectCode) throws DatabaseOperationException {
        String sql = "SELECT s.*, d.dept_code FROM subjects s JOIN departments d ON s.dept_id = d.dept_id WHERE s.subject_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Subject s = new Subject(
                        rs.getString("subject_code"),
                        rs.getString("subject_name"),
                        rs.getInt("dept_id"),
                        rs.getInt("semester"),
                        rs.getInt("credits"),
                        rs.getDouble("max_internal"),
                        rs.getDouble("max_external")
                    );
                    s.setDeptCode(rs.getString("dept_code"));
                    return Optional.of(s);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error querying subject by code: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Subject> getAllSubjects() throws DatabaseOperationException {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_code FROM subjects s JOIN departments d ON s.dept_id = d.dept_id ORDER BY s.subject_code";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Subject s = new Subject(
                    rs.getString("subject_code"),
                    rs.getString("subject_name"),
                    rs.getInt("dept_id"),
                    rs.getInt("semester"),
                    rs.getInt("credits"),
                    rs.getDouble("max_internal"),
                    rs.getDouble("max_external")
                );
                s.setDeptCode(rs.getString("dept_code"));
                list.add(s);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error querying all subjects: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Subject> getSubjectsByDepartment(int deptId) throws DatabaseOperationException {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_code FROM subjects s JOIN departments d ON s.dept_id = d.dept_id WHERE s.dept_id = ? ORDER BY s.subject_code";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject s = new Subject(
                        rs.getString("subject_code"),
                        rs.getString("subject_name"),
                        rs.getInt("dept_id"),
                        rs.getInt("semester"),
                        rs.getInt("credits"),
                        rs.getDouble("max_internal"),
                        rs.getDouble("max_external")
                    );
                    s.setDeptCode(rs.getString("dept_code"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error querying subjects by department: " + e.getMessage(), e);
        }
        return list;
    }
}
