<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Students — Student Management</title>
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

    <%-- Flash messages from session --%>
    <c:if test="${not empty sessionScope.flashMessage}">
        <div class="flash ${sessionScope.flashType}">
            <c:out value="${sessionScope.flashMessage}"/>
        </div>
        <c:remove var="flashMessage" scope="session"/>
        <c:remove var="flashType"    scope="session"/>
    </c:if>

    <%-- Stats bar --%>
    <div class="stats-bar">
        <div class="stat-item">
            <div class="label">Total Students</div>
            <div class="value">${totalStudents}</div>
        </div>
    </div>

    <div class="card">
        <div class="toolbar">
            <h2>All Students</h2>
            <a href="${pageContext.request.contextPath}/students?action=new" class="btn btn-primary">
                ＋ Add New Student
            </a>
        </div>

        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Major</th>
                        <th>GPA</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty students}">
                            <tr>
                                <td colspan="7">
                                    <div class="empty-state">
                                        <p>No students found. <a href="${pageContext.request.contextPath}/students?action=new">Add the first one!</a></p>
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="student" items="${students}" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td><strong><c:out value="${student.fullName}"/></strong></td>
                                    <td><c:out value="${student.email}"/></td>
                                    <td><c:out value="${student.phone}"/></td>
                                    <td><c:out value="${student.major}"/></td>
                                    <td>
                                        <span class="gpa-badge
                                            <c:choose>
                                                <c:when test="${student.gpa >= 3.5}">gpa-high</c:when>
                                                <c:when test="${student.gpa >= 2.5}">gpa-mid</c:when>
                                                <c:otherwise>gpa-low</c:otherwise>
                                            </c:choose>">
                                            <fmt:formatNumber value="${student.gpa}" pattern="0.00"/>
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-btns">
                                            <a href="${pageContext.request.contextPath}/students?action=edit&id=${student.id}"
                                               class="btn btn-warning btn-sm">✏ Edit</a>
                                            <a href="${pageContext.request.contextPath}/students?action=delete&id=${student.id}"
                                               class="btn btn-danger btn-sm"
                                               onclick="return confirm('Delete ${student.fullName}? This cannot be undone.');">
                                               🗑 Delete
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>

</div>

<footer>Student Management App &copy; 2024 — Servlet + JSP + JDBC + MySQL</footer>
</body>
</html>
