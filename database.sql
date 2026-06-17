-- ============================================
-- Student Management App - Database Setup
-- ============================================

CREATE DATABASE IF NOT EXISTS student_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE student_db;

CREATE TABLE IF NOT EXISTS students (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    first_name  VARCHAR(50)  NOT NULL,
    last_name   VARCHAR(50)  NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    phone       VARCHAR(20),
    major       VARCHAR(100),
    gpa         DECIMAL(3,2) DEFAULT 0.00,
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Sample data
INSERT INTO students (first_name, last_name, email, phone, major, gpa) VALUES
('Alice',   'Johnson',  'alice.johnson@university.edu',  '555-0101', 'Computer Science',    3.85),
('Bob',     'Williams', 'bob.williams@university.edu',   '555-0102', 'Mathematics',         3.42),
('Carol',   'Davis',    'carol.davis@university.edu',    '555-0103', 'Physics',             3.91),
('David',   'Martinez', 'david.martinez@university.edu', '555-0104', 'Engineering',         3.67),
('Emma',    'Brown',    'emma.brown@university.edu',     '555-0105', 'Business',            3.55);
