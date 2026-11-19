package com.vcet.studentanalysis.dao.impl;

import com.vcet.studentanalysis.dao.MarkDAO;
import com.vcet.studentanalysis.exception.DatabaseOperationException;
import com.vcet.studentanalysis.model.Mark;
import com.vcet.studentanalysis.model.StudentResult;
import com.vcet.studentanalysis.util.DBConnection;

import java.sql.*;
import java.util.*;

public class MarkDAOImpl implements MarkDAO {

    @Override
    public void recordMarks(Mark mark) throws DatabaseOperationException {
        // Upsert marks (insert or replace)
        String sql = "INSERT INTO marks (register_no, subject_code, internal_marks, external_marks, total_marks, grade, grade_point, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON CONFLICT(register_no, subject_code) DO UPDATE SET " +
                     "internal_marks = excluded.internal_marks, " +
                     "external_marks = excluded.external_marks, " +
                     "total_marks = excluded.total_marks, " +
                     "grade = excluded.grade, " +
                     "grade_point = excluded.grade_point, " +
                     "status = excluded.status";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mark.getRegisterNo());
            ps.setString(2, mark.getSubjectCode());
            ps.setDouble(3, mark.getInternalMarks());
            ps.setDouble(4, mark.getExternalMarks());
            ps.setDouble(5, mark.getTotalMarks());
            ps.setString(6, mark.getGrade());
            ps.setInt(7, mark.getGradePoint());
            ps.setString(8, mark.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error recording marks: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Mark> getMarksByStudent(String registerNo) throws DatabaseOperationException {
        List<Mark> list = new ArrayList<>();
        String sql = "SELECT m.*, sub.subject_name, sub.credits " +
                     "FROM marks m " +
                     "JOIN subjects sub ON m.subject_code = sub.subject_code " +
                     "WHERE m.register_no = ? ORDER BY m.subject_code";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, registerNo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mark m = new Mark();
                    m.setMarkId(rs.getInt("mark_id"));
                    m.setRegisterNo(rs.getString("register_no"));
                    m.setSubjectCode(rs.getString("subject_code"));
                    m.setSubjectName(rs.getString("subject_name"));
                    m.setCredits(rs.getInt("credits"));
                    m.setInternalMarks(rs.getDouble("internal_marks"));
                    m.setExternalMarks(rs.getDouble("external_marks"));
                    m.setTotalMarks(rs.getDouble("total_marks"));
                    m.setGrade(rs.getString("grade"));
                    m.setGradePoint(rs.getInt("grade_point"));
                    m.setStatus(rs.getString("status"));
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching marks for student: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Mark> getMarksBySubject(String subjectCode) throws DatabaseOperationException {
        List<Mark> list = new ArrayList<>();
        String sql = "SELECT m.*, sub.subject_name, sub.credits " +
                     "FROM marks m " +
                     "JOIN subjects sub ON m.subject_code = sub.subject_code " +
                     "WHERE m.subject_code = ? ORDER BY m.total_marks DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mark m = new Mark();
                    m.setMarkId(rs.getInt("mark_id"));
                    m.setRegisterNo(rs.getString("register_no"));
                    m.setSubjectCode(rs.getString("subject_code"));
                    m.setSubjectName(rs.getString("subject_name"));
                    m.setCredits(rs.getInt("credits"));
                    m.setInternalMarks(rs.getDouble("internal_marks"));
                    m.setExternalMarks(rs.getDouble("external_marks"));
                    m.setTotalMarks(rs.getDouble("total_marks"));
                    m.setGrade(rs.getString("grade"));
                    m.setGradePoint(rs.getInt("grade_point"));
                    m.setStatus(rs.getString("status"));
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching marks for subject: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public StudentResult getStudentSemesterSummary(String registerNo) throws DatabaseOperationException {
        String sql = "SELECT * FROM v_student_semester_summary WHERE register_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, registerNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StudentResult res = new StudentResult(
                        rs.getString("register_no"),
                        rs.getString("student_name"),
                        rs.getString("section"),
                        rs.getString("dept_code")
                    );
                    res.setGrandTotal(rs.getDouble("grand_total"));
                    res.setAverageMarks(rs.getDouble("average_marks"));
                    res.setGpa(rs.getDouble("gpa"));
                    res.setSubjectsPassed(rs.getInt("subjects_passed"));
                    res.setSubjectsFailed(rs.getInt("subjects_failed"));
                    res.setOverallStatus(rs.getString("overall_result"));
                    res.setMarkList(getMarksByStudent(registerNo));
                    return res;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching student semester summary: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<StudentResult> getAllStudentSummaries() throws DatabaseOperationException {
        List<StudentResult> list = new ArrayList<>();
        String sql = "SELECT * FROM v_student_semester_summary ORDER BY gpa DESC, grand_total DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StudentResult res = new StudentResult(
                    rs.getString("register_no"),
                    rs.getString("student_name"),
                    rs.getString("section"),
                    rs.getString("dept_code")
                );
                res.setGrandTotal(rs.getDouble("grand_total"));
                res.setAverageMarks(rs.getDouble("average_marks"));
                res.setGpa(rs.getDouble("gpa"));
                res.setSubjectsPassed(rs.getInt("subjects_passed"));
                res.setSubjectsFailed(rs.getInt("subjects_failed"));
                res.setOverallStatus(rs.getString("overall_result"));
                list.add(res);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error querying all student summaries: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getSubjectPerformanceSummary() throws DatabaseOperationException {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT * FROM v_subject_performance ORDER BY pass_percentage ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("subject_code", rs.getString("subject_code"));
                map.put("subject_name", rs.getString("subject_name"));
                map.put("credits", rs.getInt("credits"));
                map.put("total_appeared", rs.getInt("total_appeared"));
                map.put("total_passed", rs.getInt("total_passed"));
                map.put("total_failed", rs.getInt("total_failed"));
                map.put("pass_percentage", rs.getDouble("pass_percentage"));
                map.put("avg_marks", rs.getDouble("avg_marks"));
                map.put("min_marks", rs.getDouble("min_marks"));
                map.put("max_marks", rs.getDouble("max_marks"));
                list.add(map);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error querying subject performance summary: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getDepartmentSummary() throws DatabaseOperationException {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT * FROM v_department_summary ORDER BY dept_code";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("dept_code", rs.getString("dept_code"));
                map.put("dept_name", rs.getString("dept_name"));
                map.put("total_students", rs.getInt("total_students"));
                map.put("total_evaluations", rs.getInt("total_evaluations"));
                map.put("dept_avg_marks", rs.getDouble("dept_avg_marks"));
                map.put("overall_pass_percentage", rs.getDouble("overall_pass_percentage"));
                list.add(map);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error querying department summary: " + e.getMessage(), e);
        }
        return list;
    }
}
