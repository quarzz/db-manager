<%@include file="header.jsp"%>

<c:forEach items="${requestScope.queryParameterNames}" var="parameterName">
    ${parameterName}<br>
</c:forEach>