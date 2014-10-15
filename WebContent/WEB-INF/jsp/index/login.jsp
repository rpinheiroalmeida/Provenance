<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<body>
<h2>Project List</h2>
<c:forEach var="proj" items="${projectList}">
<c:out value="${proj.nome}"/>
</c:forEach>
<c:out value="Ok"/>
</body>
</html>
