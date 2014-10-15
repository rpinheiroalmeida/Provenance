<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<h2>Project List</h2>
<c:forEach var="proj" items="${projectList}">
<c:out value="${proj.nome}"/>
</c:forEach>
</body>
</html>
