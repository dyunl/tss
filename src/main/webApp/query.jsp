<%@page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<html>

<head>
</head>
<body>
<center>
    <h2>诗歌查询</h2>
    <form method="post" action="${pageContext.request.contextPath}/query/show">
        <input type="text" name="message"><br>
        <input type="submit" value="搜索">
    </form>
</center>
</body>
</html>
