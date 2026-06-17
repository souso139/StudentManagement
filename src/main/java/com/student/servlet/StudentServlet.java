package com.student.servlet;

import com.student.dao.StudentDAO;
import com.student.dao.StudentDAOImpl;
import com.student.model.Student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Front-controller servlet for the Student Management application.
 *
 * URL pattern : /students
 * Actions (via "action" request parameter):
 *
 *   GET  action=list   → show all students          → list.jsp
 *   GET  action=new    → show empty add-form         → form.jsp
 *   GET  action=edit   → show pre-filled edit-form   → form.jsp
 *   GET  action=delete → delete student, redirect
 *   POST action=insert → persist new student
 *   POST action=update → update existing student
 */
@WebServlet(name = "StudentServlet", urlPatterns = {"/students", "/students/*"})
public class StudentServlet extends HttpServlet {

    private static final String VIEW_DIR = "/WEB-INF/views/";

    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAOImpl();
    }

    // ── GET dispatcher ────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new"    -> showForm(req, resp, null);
                case "edit"   -> showEditForm(req, resp);
                case "delete" -> deleteStudent(req, resp);
                default       -> listStudents(req, resp);
            }
        } catch (SQLException e) {
            handleDbError(req, resp, e);
        }
    }

    // ── POST dispatcher ───────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "insert" -> insertStudent(req, resp);
                case "update" -> updateStudent(req, resp);
                default       -> listStudents(req, resp);
            }
        } catch (SQLException e) {
            handleDbError(req, resp, e);
        }
    }

    // ── Action handlers ───────────────────────────────────────────────────────

    /** Loads all students and forwards to the list view. */
    private void listStudents(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        List<Student> students = studentDAO.getAllStudents();
        int total = studentDAO.countStudents();

        req.setAttribute("students", students);
        req.setAttribute("totalStudents", total);
        forward(req, resp, "list.jsp");
    }

    /** Shows the blank add-student form. */
    private void showForm(HttpServletRequest req, HttpServletResponse resp,
                          Student student) throws ServletException, IOException {
        req.setAttribute("student", student);
        forward(req, resp, "form.jsp");
    }

    /** Loads a student by id and shows the pre-filled edit form. */
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        int id = parseId(req.getParameter("id"));
        Student student = studentDAO.getStudentById(id);

        if (student == null) {
            setFlash(req, "error", "Student with id " + id + " not found.");
            redirect(resp, req.getContextPath() + "/students?action=list");
            return;
        }
        showForm(req, resp, student);
    }

    /** Inserts a new student from POST parameters. */
    private void insertStudent(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {

        Student student = buildStudentFromRequest(req);

        // Basic server-side validation
        String error = validate(student);
        if (error != null) {
            req.setAttribute("error", error);
            showForm(req, resp, student);
            return;
        }

        if (studentDAO.addStudent(student)) {
            setFlash(req, "success",
                    "Student '" + student.getFullName() + "' added successfully.");
        } else {
            setFlash(req, "error", "Failed to add student. Please try again.");
        }
        redirect(resp, req.getContextPath() + "/students?action=list");
    }

    /** Updates an existing student from POST parameters. */
    private void updateStudent(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {

        Student student = buildStudentFromRequest(req);
        student.setId(parseId(req.getParameter("id")));

        String error = validate(student);
        if (error != null) {
            req.setAttribute("error", error);
            showForm(req, resp, student);
            return;
        }

        if (studentDAO.updateStudent(student)) {
            setFlash(req, "success",
                    "Student '" + student.getFullName() + "' updated successfully.");
        } else {
            setFlash(req, "error", "Failed to update student. Please try again.");
        }
        redirect(resp, req.getContextPath() + "/students?action=list");
    }

    /** Deletes a student by id and redirects back to the list. */
    private void deleteStudent(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {

        int id = parseId(req.getParameter("id"));
        Student student = studentDAO.getStudentById(id);

        if (student != null && studentDAO.deleteStudent(id)) {
            setFlash(req, "success",
                    "Student '" + student.getFullName() + "' deleted.");
        } else {
            setFlash(req, "error", "Student not found or could not be deleted.");
        }
        redirect(resp, req.getContextPath() + "/students?action=list");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Builds a Student from POST form parameters. */
    private Student buildStudentFromRequest(HttpServletRequest req) {
        Student s = new Student();
        s.setFirstName(trim(req.getParameter("firstName")));
        s.setLastName (trim(req.getParameter("lastName")));
        s.setEmail    (trim(req.getParameter("email")));
        s.setPhone    (trim(req.getParameter("phone")));
        s.setMajor    (trim(req.getParameter("major")));
        try {
            s.setGpa(Double.parseDouble(req.getParameter("gpa")));
        } catch (NumberFormatException e) {
            s.setGpa(0.0);
        }
        return s;
    }

    /** Returns an error message if the student is invalid, otherwise null. */
    private String validate(Student s) {
        if (s.getFirstName().isEmpty()) return "First name is required.";
        if (s.getLastName().isEmpty())  return "Last name is required.";
        if (s.getEmail().isEmpty())     return "Email is required.";
        if (!s.getEmail().contains("@")) return "Please enter a valid email address.";
        if (s.getGpa() < 0.0 || s.getGpa() > 4.0)
            return "GPA must be between 0.0 and 4.0.";
        return null;
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String page)
            throws ServletException, IOException {
        req.getRequestDispatcher(VIEW_DIR + page).forward(req, resp);
    }

    private void redirect(HttpServletResponse resp, String url) throws IOException {
        resp.sendRedirect(url);
    }

    /** Stores a one-time flash message in the session. */
    private void setFlash(HttpServletRequest req, String type, String message) {
        HttpSession session = req.getSession();
        session.setAttribute("flashType",    type);
        session.setAttribute("flashMessage", message);
    }

    private int parseId(String raw) {
        try { return Integer.parseInt(raw); } catch (Exception e) { return -1; }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private void handleDbError(HttpServletRequest req, HttpServletResponse resp,
                                SQLException e) throws ServletException, IOException {
        req.setAttribute("errorMessage", "Database error: " + e.getMessage());
        req.getRequestDispatcher(VIEW_DIR + "error.jsp").forward(req, resp);
    }
}
