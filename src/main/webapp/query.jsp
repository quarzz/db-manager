<%@include file="header.jsp"%>

<form action="${pageContext.request.contextPath}/query" method="post">
    <fieldset>
        <legend>${requestScope.queryName}</legend>
        <c:forEach items="${requestScope.queryParameterNames}" var="parameterName">
            <p>
                <label for="${parameterName}">${parameterName}</label>
                <input type="text" id="${parameterName}" name="${parameterName}"/>
            </p>
        </c:forEach>
        <input type="hidden" name="queryName" value="${requestScope.queryName}"/>
        <input type="submit" name="execute" value="execute">
    </fieldset>
</form>

