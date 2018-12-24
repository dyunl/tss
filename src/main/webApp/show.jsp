<%@page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <script>
    </script>
</head>
<body>
<center><h2>诗歌查询</h2></center>

<div>

    <table  border="1px " cellpadding="0" cellspacing="0">
        <tr>
            <td>作品</td><td>作品名-->作者</td>
        </tr>

        <c:forEach items="${requestScope.map}" var="m">
            <tr>
                <td>${m.key}</td>
                <td>${m.value}</td>
            </tr>
        </c:forEach>
    </table>
    <a href="${pageContext.request.contextPath}/query/show?page=${requestScope.page-1}&message=${requestScope.message}&size=5">上一页</a>
    <a href="${pageContext.request.contextPath}/query/show?page=${requestScope.page+1}&message=${requestScope.message}&size=5">下一页</a>


</div>




</body>
</html>
