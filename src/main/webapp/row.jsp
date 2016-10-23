<%@include file="header.jsp"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<form action="table" method="post">
    <table class="table table-hover table-striped">
        <tr>
            <c:forEach items="${requestScope.columnNames}" var="columnName">
                <th><label for="${columnName}">${columnName}</label></th>
            </c:forEach>
            <th></th>
            <th></th>
        </tr>
        <tr>
            <c:forEach begin="0" end="${fn:length(requestScope.row) - 1}" var="i">
                <td>
                    <input type="text" id="${requestScope.columnNames[i]}"
                           name="${requestScope.columnNames[i]}"
                           value="${requestScope.row[i]}"/>
                </td>
            </c:forEach>
        </tr>
    </table>
    <input type="hidden" name="tableName" value="${requestScope.tableName}"/>
    <input type="hidden" name="id" value="${requestScope.row[0]}">
    <input type="submit" name="action" value="update"/>
</form>