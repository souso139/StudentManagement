package com.student.dao;

import com.student.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link StudentDAO}.
 * Uses prepared statements throughout to prevent SQL injection.
 */
public class StudentDAOImpl implements StudentDAO {

    // ── SQL statements ────────────────────────────────────────────────────────

    private static final String SQL_SELECT_ALL =
            "SELECT id, first_name, last_name, email, phone, major, gpa, " +
            "created_at, updated_at FROM students ORDER BY last_name, first_name";

    private static final String SQL_SELECT_BY_ID =
            "SELECT id, first_name, last_name, email, phone, major, gpa, " +
            "created_at, updated_at FROM students WHERE id = ?";

    private static final String SQL_INSERT =
            "INSERT INTO students (first_name, last_name, email, phone, major, gpa) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE students SET first_name=?, last_name=?, email=?, " +
            "phone=?, major=?, gpa=? WHERE id=?";

    private static final String SQL_DELETE =
            "DELETE FROM students WHERE id = ?";

    private static final String SQL_COUNT =
            "SELECT COUNT(*) FROM students";

    // ── CRUD operations ───────────────────────────────────────────────────────

    @Override
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(mapRow(rs));
            }
        }
        return students;
    }

    @Override
    public Student getStudentById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    @Override
    public boolean addStudent(Student student) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {

            setStudentParams(ps, student);
            int rows = ps.executeUpdate();

            // Populate the generated id back into the object
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) student.setId(keys.getInt(1));
                }
            }
            return rows > 0;
        }
    }

    @Override
    public boolean updateStudent(Student student) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            setStudentParams(ps, student);   // positions 1-6
            ps.setInt(7, student.getId());   // position 7: WHERE id=?
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteStudent(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public int countStudents() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Maps the current ResultSet row to a Student object.
     * All column reads are by name for clarity and resilience to column reordering.
     */
    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("major"),
                rs.getDouble("gpa"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }

    /**
     * Binds positions 1-6 of a PreparedStatement from a Student object.
     * Shared by both INSERT and UPDATE statements.
     */
    private void setStudentParams(PreparedStatement ps, Student s) throws SQLException {
        ps.setString(1, s.getFirstName());
        ps.setString(2, s.getLastName());
        ps.setString(3, s.getEmail());
        ps.setString(4, s.getPhone());
        ps.setString(5, s.getMajor());
        ps.setDouble(6, s.getGpa());
    }
}
