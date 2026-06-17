<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error — Student Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>🎓 Student <span>Management</span></h1>
    <nav>
        <a href="${pageContext.request.contextPath}/students?action=list">All Students</a>
    </nav>
</header>
<div class="container">
    <div class="card error-page">
        <h2>⚠️ Something Went Wrong</h2>
        <p>An unexpected error occurred while processing your request.</p>
        <c:if test="${not empty errorMessage}">
            <code><c:out value="${errorMessage}"/></code>
        </c:if>
        <a href="${pageContext.request.contextPath}/students?action=list" class="btn btn-primary">
            ← Back to Student List
        </a>
    </div>
</div>
<footer>Student Management App &copy; 2024</footer>
</body>
</html>
