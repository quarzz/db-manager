<%@include file="header.jsp"%>

<form action="table" method="post">
    <table class="table table-hover table-striped">
        <tr>
            <c:forEach items="${requestScope.tableData[0]}" var="columnName">
                <th><label for="${columnName}">${columnName}</label></th>
            </c:forEach>
            <c:if test="${requestScope.action eq 'table'}">
                <th></th>
                <th></th>
            </c:if>
        </tr>
        <c:forEach items="${requestScope.tableData}" var="row" varStatus="status">
            <c:if test="${not status.first }">
                <tr id="${row[0]}">
                    <c:forEach items="${row}" var="col">
                        <td>${col}</td>
                    </c:forEach>
                    <c:if test="${requestScope.action eq 'table'}">
                        <td><a href="table?tableName=${requestScope.tableName}&action=update&id=${row[0]}">edit</a></td>
                        <td><a href="table?tableName=${requestScope.tableName}&action=delete&id=${row[0]}">delete</a></td>
                    </c:if>
                </tr>
            </c:if>
        </c:forEach>
        <tr>
            <c:forEach items="${requestScope.columnNames}" var="columnName">
                <td>
                    <input type="text" id="${columnName}" name="${columnName}"/>
                </td>
            </c:forEach>
        </tr>
    </table>
    <input type="hidden" name="tableName" value="${requestScope.tableName}"/>
    <c:if test="${requestScope.action eq 'table'}">
        <input type="submit" name="action" value="insert"/>
    </c:if>
</form>
