package com.student.dao;

import com.student.model.Student;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO interface defining all CRUD operations for the Student entity.
 * Programming to the interface allows easy swapping of implementations
 * (e.g. switching from MySQL to PostgreSQL or an in-memory mock for tests).
 */
public interface StudentDAO {

    /**
     * Retrieves every student, ordered by last name then first name.
     *
     * @return a list of all students (empty list if none exist)
     * @throws SQLException if a database error occurs
     */
    List<Student> getAllStudents() throws SQLException;

    /**
     * Looks up a single student by primary key.
     *
     * @param id the student's id
     * @return the matching Student, or {@code null} if not found
     * @throws SQLException if a database error occurs
     */
    Student getStudentById(int id) throws SQLException;

    /**
     * Persists a new student record.
     *
     * @param student the student to insert (id field is ignored)
     * @return {@code true} if the insert succeeded
     * @throws SQLException if a database error occurs (e.g. duplicate email)
     */
    boolean addStudent(Student student) throws SQLException;

    /**
     * Updates an existing student record identified by {@code student.getId()}.
     *
     * @param student the student with updated values
     * @return {@code true} if exactly one row was updated
     * @throws SQLException if a database error occurs
     */
    boolean updateStudent(Student student) throws SQLException;

    /**
     * Removes the student with the given id.
     *
     * @param id the student's id
     * @return {@code true} if exactly one row was deleted
     * @throws SQLException if a database error occurs
     */
    boolean deleteStudent(int id) throws SQLException;

    /**
     * Returns the total number of students.
     *
     * @return row count
     * @throws SQLException if a database error occurs
     */
    int countStudents() throws SQLException;
}
