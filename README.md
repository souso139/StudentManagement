# 🎓 Student Management App

A complete **CRUD** web application for managing students, built with:

| Layer | Technology |
|---|---|
| Presentation | JSP + JSTL |
| Controller | Jakarta Servlet |
| Data access | DAO pattern + interface |
| Database driver | JDBC |
| Database | MySQL 8 |
| Build tool | Maven |

---

## Project Structure

```
StudentManagement/
├── pom.xml
├── database.sql                          ← DB setup script
└── src/main/
    ├── java/com/student/
    │   ├── model/
    │   │   └── Student.java              ← Entity / POJO
    │   ├── dao/
    │   │   ├── StudentDAO.java           ← DAO interface
    │   │   ├── StudentDAOImpl.java       ← JDBC implementation
    │   │   └── DBConnection.java        ← Connection factory
    │   └── servlet/
    │       └── StudentServlet.java      ← Front controller
    └── webapp/
        ├── index.jsp                    ← Redirect to list
        ├── css/style.css
        └── WEB-INF/
            ├── web.xml
            └── views/
                ├── list.jsp             ← Student list
                ├── form.jsp             ← Add / Edit form
                └── error.jsp
```

---

## Setup & Run

### 1. Database

```sql
-- Run the included script
mysql -u root -p < database.sql
```

### 2. Configure connection (optional)

The defaults in `DBConnection.java` are:

| Property | Default |
|---|---|
| URL | `jdbc:mysql://localhost:3306/student_db` |
| User | `root` |
| Password | *(empty)* |

Override at Tomcat startup without recompiling:

```
-Ddb.url=jdbc:mysql://host:3306/student_db?useSSL=false&serverTimezone=UTC
-Ddb.user=myuser
-Ddb.password=secret
```

### 3. Build

```bash
mvn clean package
```

This produces `target/StudentManagement.war`.

### 4. Deploy

Copy the WAR to your Tomcat `webapps/` directory:

```bash
cp target/StudentManagement.war $CATALINA_HOME/webapps/
```

Then open: **http://localhost:8080/StudentManagement/**

---

## Features

| Action | URL |
|---|---|
| List all students | `GET /students?action=list` |
| Show add form | `GET /students?action=new` |
| Submit new student | `POST /students` (action=insert) |
| Show edit form | `GET /students?action=edit&id={id}` |
| Submit edit | `POST /students` (action=update) |
| Delete student | `GET /students?action=delete&id={id}` |

---

## Design Highlights

- **Single servlet** (`StudentServlet`) acts as a front controller, dispatching by the `action` parameter.
- **DAO interface** (`StudentDAO`) decouples the business logic from JDBC details — swap implementations without touching the servlet.
- **PreparedStatement** everywhere — no SQL injection risk.
- **Try-with-resources** for every JDBC object — no connection leaks.
- **Flash messages** via `HttpSession` survive the post-redirect-get cycle.
- **Server-side validation** with inline form error display.
- GPA badge colours: green ≥ 3.5 / yellow ≥ 2.5 / red < 2.5.
"# StudentManagement" 
