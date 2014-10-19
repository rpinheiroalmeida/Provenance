<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@include file="header.jsp" %>

<link rel="stylesheet" type="text/css" href="../jquery/tabs.css"/>
<script src="../_js/search.js"></script>

<!--jquery-->	
<script src="../jquery/ui/jquery-ui-1.7.2.custom.js" type="text/javascript"></script>
<script src="../jquery/jquery.impromptu.2.7.js" type="text/javascript"></script>
    	
<body>

<div id="dlgBusca">
	<form id="frmBusca">
		<fieldset class="cadastros">
				<legend>Searching</legend>
				<div  class="rotulo">Project:</div>
				<select id="cbxProject" name="idProject" style="width:500px">
					<option value="0">::Selecione::</option>
					<c:forEach var="proj" items="${listProject}"> 
						<option value="${proj.id}">${proj.nome}</option>
					</c:forEach>
				</select>
				<br>
		
				<div  class="rotulo">Account:</div>
				<select id="cbxAccount" name="idAccount" style="width:500px">
					<option value="0">::Selecione::</option>
					
				</select>
				<br>
				
				<div  class="rotulo">Group:</div>
				<select id="cbxGroup" name="idGroup" style="width:500px">
					<option value="0">::Selecione::</option>
					<c:forEach var="group" items="${listGroups}"> 
						<option value="${group.id}">${group.nome}</option>
					</c:forEach>
				</select>
				<br>
						
				<input type="button" value="Clean" onclick="limparTela()">
				<input type="button" value="Search" onclick="consultar()">						
		</fieldset>	
	</form>	
</div>

<fieldset id="cadastros">
<div id="resultado_busca" style="width:500px; height: 300px"></div>
</fieldset>


</body>
</html>