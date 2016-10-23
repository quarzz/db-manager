<%@include file="header.jsp"%>

<c:forEach items="${requestScope.tableNames}" var="tableName">
    <a href="table?tableName=${tableName}">${tableName}</a>
    <br>
</c:forEach>