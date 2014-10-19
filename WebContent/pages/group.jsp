<%@page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="header.jsp" %>
<link rel="stylesheet" href="../_css/group.css"/>	
<link rel="stylesheet" type="text/css" href="../jquery/tabs.css"/>

<!--jquery-->	
<script src="../jquery/ui/jquery-ui-1.7.2.custom.js" type="text/javascript"></script>
<script src="../_js/group.js"></script>
	
<body>

<div id="formCadGrupo">
	
	<form id="frmManter">
		<fieldset class="cadastros">
			<legend>Group</legend>
			<div class="rotulo">Name:</div>
			<input type="text" id="txtNome" name="group.nome" maxlength="50" size="100" value="${group.nome}"><br>				
			
			<input type="hidden" name="acao" value="salvar"/>
			<input type="button" value="Clean" onclick="limparTela()">
			<c:choose>
				<c:when test="${not empty group.id && group.id gt 0}">
					<input type="hidden" name="idGrupo" id="idGrupo" value="<c:out value="${group.id}"/>"/>
					<input type="button" id="excluir" value="Delete">			
				</c:when>
				<c:otherwise>
					<input type="hidden" name="idGrupo" id="idGrupo" value="0"/>
				</c:otherwise>
			</c:choose>
			<input type="button" value="Save" onclick="saveGroup()">

		</fieldset>
	</form>
	
</div>

</body>
</html>