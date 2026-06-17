<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%-- Determine if this is an edit (student != null) or add operation --%>
<c:set var="isEdit"     value="${not empty student and student.id > 0}"/>
<c:set var="pageTitle"  value="${isEdit ? 'Edit Student' : 'Add New Student'}"/>
<c:set var="formAction" value="${isEdit ? 'update' : 'insert'}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} — Student Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<header>
    <h1>🎓 Student <span>Management</span></h1>
    <nav>
        <a href="${pageContext.request.contextPath}/students?action=list">All Students</a>
        <a href="${pageContext.request.contextPath}/students?action=new">+ Add Student</a>
    </nav>
</header>

<div class="container">

    <div class="card" style="max-width:760px; margin: 0 auto;">

        <div class="form-title">
            ${isEdit ? '✏️ Edit Student' : '➕ Add New Student'}
        </div>

        <%-- Inline validation error --%>
        <c:if test="${not empty error}">
            <div class="error-msg">⚠ <c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/students"
              novalidate>

            <input type="hidden" name="action" value="${formAction}"/>

            <c:if test="${isEdit}">
                <input type="hidden" name="id" value="${student.id}"/>
            </c:if>

            <div class="form-grid">

                <%-- First Name --%>
                <div class="form-group">
                    <label for="firstName">First Name <span style="color:#e53e3e">*</span></label>
                    <input type="text" id="firstName" name="firstName"
                           value="<c:out value='${student.firstName}'/>"
                           placeholder="e.g. Alice" required maxlength="50">
                </div>

                <%-- Last Name --%>
                <div class="form-group">
                    <label for="lastName">Last Name <span style="color:#e53e3e">*</span></label>
                    <input type="text" id="lastName" name="lastName"
                           value="<c:out value='${student.lastName}'/>"
                           placeholder="e.g. Johnson" required maxlength="50">
                </div>

                <%-- Email --%>
                <div class="form-group full-width">
                    <label for="email">Email Address <span style="color:#e53e3e">*</span></label>
                    <input type="email" id="email" name="email"
                           value="<c:out value='${student.email}'/>"
                           placeholder="e.g. alice@university.edu" required maxlength="100">
                </div>

                <%-- Phone --%>
                <div class="form-group">
                    <label for="phone">Phone Number</label>
                    <input type="tel" id="phone" name="phone"
                           value="<c:out value='${student.phone}'/>"
                           placeholder="e.g. 555-0101" maxlength="20">
                </div>

                <%-- Major --%>
                <div class="form-group">
                    <label for="major">Major</label>
                    <input type="text" id="major" name="major"
                           value="<c:out value='${student.major}'/>"
                           placeholder="e.g. Computer Science" maxlength="100">
                </div>

                <%-- GPA --%>
                <div class="form-group">
                    <label for="gpa">GPA (0.00 – 4.00)</label>
                    <input type="number" id="gpa" name="gpa"
                           value="<c:out value='${student.gpa}'/>"
                           placeholder="e.g. 3.75"
                           min="0.00" max="4.00" step="0.01">
                </div>

            </div><%-- .form-grid --%>

            <div class="form-actions">
                <button type="submit" class="btn ${isEdit ? 'btn-warning' : 'btn-primary'}">
                    ${isEdit ? '💾 Save Changes' : '➕ Add Student'}
                </button>
                <a href="${pageContext.request.contextPath}/students?action=list"
                   class="btn btn-secondary">✖ Cancel</a>
            </div>

        </form>
    </div>
</div>

<footer>Student Management App &copy; 2024 — Servlet + JSP + JDBC + MySQL</footer>
</body>
</html>
