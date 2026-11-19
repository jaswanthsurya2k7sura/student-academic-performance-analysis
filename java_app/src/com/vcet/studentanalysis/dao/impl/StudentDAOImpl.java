package com.vcet.studentanalysis.dao.impl;

import com.vcet.studentanalysis.dao.StudentDAO;
import com.vcet.studentanalysis.exception.DatabaseOperationException;
import com.vcet.studentanalysis.model.Student;
import com.vcet.studentanalysis.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void addStudent(Student student) throws DatabaseOperationException {
        String sql = "INSERT INTO students (register_no, name, dept_id, year_of_study, section, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getRegisterNo());
            ps.setString(2, student.getName());
            ps.setInt(3, student.getDeptId());
            ps.setInt(4, student.getYearOfStudy());
            ps.setString(5, student.getSection());
            ps.setString(6, student.getEmail());
            ps.setString(7, student.getPhone());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error adding student: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Student> getStudentByRegisterNo(String registerNo) throws DatabaseOperationException {
        String sql = "SELECT s.*, d.dept_code FROM students s JOIN departments d ON s.dept_id = d.dept_id WHERE s.register_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, registerNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student s = new Student(
                        rs.getString("register_no"),
                        rs.getString("name"),
                        rs.getInt("dept_id"),
                        rs.getInt("year_of_study"),
                        rs.getString("section"),
                        rs.getString("email"),
                        rs.getString("phone")
                    );
                    s.setDeptCode(rs.getString("dept_code"));
                    return Optional.of(s);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error querying student by register no: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Student> getAllStudents() throws DatabaseOperationException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_code FROM students s JOIN departments d ON s.dept_id = d.dept_id ORDER BY s.register_no";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(
                    rs.getString("register_no"),
                    rs.getString("name"),
                    rs.getInt("dept_id"),
                    rs.getInt("year_of_study"),
                    rs.getString("section"),
                    rs.getString("email"),
                    rs.getString("phone")
                );
                s.setDeptCode(rs.getString("dept_code"));
                list.add(s);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error querying all students: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Student> getStudentsByDepartment(int deptId) throws DatabaseOperationException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_code FROM students s JOIN departments d ON s.dept_id = d.dept_id WHERE s.dept_id = ? ORDER BY s.register_no";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student(
                        rs.getString("register_no"),
                        rs.getString("name"),
                        rs.getInt("dept_id"),
                        rs.getInt("year_of_study"),
                        rs.getString("section"),
                        rs.getString("email"),
                        rs.getString("phone")
                    );
                    s.setDeptCode(rs.getString("dept_code"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error querying students by department: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean deleteStudent(String registerNo) throws DatabaseOperationException {
        String sql = "DELETE FROM students WHERE register_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, registerNo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting student: " + e.getMessage(), e);
        }
    }
}
