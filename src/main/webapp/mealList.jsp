<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <c:choose>
        <c:when test="${empty id}">
            <h2>Add meal form:</h2>
        </c:when>
        <c:otherwise>
            <h2>Edit meal form:</h2>
        </c:otherwise>
    </c:choose>

<form method="POST" action="./meals">
    <table>
        <tr>
            <td>Date and time:</td>
            <td><input type="text" name="dateTime" value="${dateTime}"></td>
        </tr>

        <tr>
            <td>Description</td>
            <td><input type="text" name="description" value="${description}"/></td>
        </tr>

        <tr>
            <td>Calories</td>
            <td><input type="text" name="calories" value="${calories}"/>
                <input type="hidden" name="id" value="${id}"/></td>
        </tr>
    </table>

    <input type="submit" value="GO" />

</form>

<hr>
<h2>Meals list</h2>

<table style="border: 1px solid; width: 500px; text-align:center">
    <thead style="background:#fcf">
    <tr>
        <th>Date and time</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <c:url var="editUrl" value="./meals?do=edit&id=${meal.getId()}" />
        <c:url var="deleteUrl" value="./meals?do=delete&id=${meal.getId()}" />
        <tr <c:if test="${meal.exceed}">bgcolor="#db7093" </c:if>>
            <td><c:out value="${meal.dateTime}" /> </td>
            <td><c:out value="${meal.description}" /></td>
            <td><c:out value="${meal.calories}" /></td>

            <td><a href="${editUrl}">Edit</a></td>
            <td><a href="${deleteUrl}">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<c:if test="${empty meals}">
    There are currently no meals in the list.
</c:if>

</body>
</html>
