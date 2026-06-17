package com.student.model;

import java.sql.Timestamp;

/**
 * Model class representing a Student entity.
 * Maps directly to the `students` table in the database.
 */
public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String major;
    private double gpa;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Student() {}

    /** Constructor used when creating a new student (no id yet). */
    public Student(String firstName, String lastName, String email,
                   String phone, String major, double gpa) {
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
        this.phone     = phone;
        this.major     = major;
        this.gpa       = gpa;
    }

    /** Full constructor including id (used when reading from DB). */
    public Student(int id, String firstName, String lastName, String email,
                   String phone, String major, double gpa,
                   Timestamp createdAt, Timestamp updatedAt) {
        this.id        = id;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
        this.phone     = phone;
        this.major     = major;
        this.gpa       = gpa;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Returns the full name as "First Last". */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }

    public String getFirstName()                   { return firstName; }
    public void setFirstName(String firstName)     { this.firstName = firstName; }

    public String getLastName()                    { return lastName; }
    public void setLastName(String lastName)       { this.lastName = lastName; }

    public String getEmail()                       { return email; }
    public void setEmail(String email)             { this.email = email; }

    public String getPhone()                       { return phone; }
    public void setPhone(String phone)             { this.phone = phone; }

    public String getMajor()                       { return major; }
    public void setMajor(String major)             { this.major = major; }

    public double getGpa()                         { return gpa; }
    public void setGpa(double gpa)                 { this.gpa = gpa; }

    public Timestamp getCreatedAt()                { return createdAt; }
    public void setCreatedAt(Timestamp createdAt)  { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt()                { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt)  { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + getFullName() +
               "', email='" + email + "', major='" + major + "', gpa=" + gpa + "}";
    }
}
