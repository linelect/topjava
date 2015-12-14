<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {color: green;}
        .exceeded {color: red;}
    </style>
</head>
<body>
<section>
    <h2><a href="index.html">Home</a></h2>
    <h3>Meal list</h3>

    <c:if test="${empty mealList}">You not login or you have not meals. For login <a href="index.html">click</a></c:if>
    <hr>
    <form method="post" action="meals">
        <dl>
            <dt>From Date:</dt>
            <dd><input type="date" value="${fromDate}" name="fromDate"></dd>
        </dl>
        <dl>
            <dt>To Date:</dt>
            <dd><input type="date" value="${toDate}" name="toDate"></dd>
        </dl>
        <dl>
            <dt>From Time:</dt>
            <dd><input type="time" value="${fromTime}" name="fromTime"></dd>
        </dl>
        <dl>
            <dt>To Time:</dt>
            <dd><input type="time" value="${toTime}" name="toTime"></dd>
        </dl>
        <button type="submit">Filter</button>

    </form>

    <hr>
    <a href="meals?action=create">Add Meal</a>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${mealList}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.UserMealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                   <%--<fmt:parseDate value="${meal.dateTime}" pattern="y-M-dd'T'H:m" var="parsedDate"/>--%>
                   <%--<fmt:formatDate value="${parsedDate}" pattern="yyyy.MM.dd HH:mm" />--%>
                    <%=TimeUtil.toString(meal.getDateTime())%>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>