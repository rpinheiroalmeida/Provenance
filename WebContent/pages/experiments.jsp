<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@include file="header.jsp" %>

<link rel="stylesheet" type="text/css" href="../jquery/tabs.css"/>

<!--jquery-->	
<script src="../jquery/ui/i18n/ui.datepicker-pt-BR.js" type="text/javascript"></script>    
<script src="../jquery/jquery.impromptu.2.7.js" type="text/javascript"></script>
<script src="../jquery/ui/jquery-ui-1.7.2.custom.js" type="text/javascript"></script>
<script src="../jquery/ui/i18n/ui.datepicker-pt-BR.js" type="text/javascript"></script>    
<script src="../jquery/jquery.maskedinput-1.2.2.js" type="text/javascript"></script>
<script src="../jquery/jquery.impromptu.2.7.js" type="text/javascript"></script>
<script src="../_js/experiment.js"></script>
    	
<body>

<div id="formCadExperimento">
	<form id="frmManter">
		<fieldset class="cadastros">
		<h1>Proj: id: ${account.project.id}</h1>
				<legend>Account</legend>
				<div  class="rotulo">Project:</div>
				<select id="cbxProject" name="account.project.id" style="width:500px">
					<option value="0">::Selecione::</option>
					<c:forEach var="proj" items="${listProject}">
						<option value="${proj.id}">${proj.nome}</option>
						<c:if test="${account.project.id == proj.id}">
						    <option value="${proj.id}" selected>${proj.nome}</option>
						</c:if>
						
					</c:forEach>
				</select>
				<br>
		
				<div class="rotulo">Name:</div>
				<input type="text" id="txtNome" name="account.nome" maxlength="50" size="100" value="${account.nome}"><br>				
				
				<div class="rotulo">Description:</div><input type="text" id="txtDescricao" name="account.descricao" maxlength="50" size="100" value="${account.descricao}"><br>
				<div class="rotulo">Local:</div><input type="text" id="txtLocal" name="account.localExecucao" maxlength="50" size="100" value="${account.localExecucao}"><br>
				<div class="rotulo">Version:</div><input type="text" id="txtVersao" name="account.versao" maxlength="50" size="10" value="${account.versao}"><br>
										
				<div class="rotulo">Start Date:</div><input type="text" id="txtDataInicio" name="account.dataHoraInicio" value="<fmt:formatDate value="${account.dataHoraInicio}" pattern="dd/MM/yyyy"/>"><br>				
			    <div class="rotulo">End Date:</div><input type="text" id="txtDataFim" name="account.dataHoraFim" value="<fmt:formatDate value="${account.dataHoraFim}" pattern="dd/MM/yyyy"/>"><br>
			    <div class="rotulo">Version Date:</div><input type="text" id="txtDataVersao" name="account.dataVersao" value="<fmt:formatDate value="${account.dataVersao}" pattern="dd/MM/yyyy"/>"><br>
									
				<div class="rotulo">Annotation:</div><textarea id="txtObservacao" name="account.anotacoes" rows="4" cols="76">${account.anotacoes}</textarea><br>			
			
				<input type="hidden" name="acao" value="salvar"/>
						
				<input type="button" value="Clean" onclick="limparTelaExperimentos()"/>
				<c:choose>
					<c:when test="${not empty account.id && account.id gt 0}">
						<input type="hidden" name="idExperimento" id="idExperimento" value="${account.id}"/>
						<input type="button" value="Delete" onclick="deletarExperimento()">			
					</c:when>
					<c:otherwise>
						<input type="hidden" name="idExperimento" id="idExperimento" value="0"/>		
					</c:otherwise>
				</c:choose>
				<input type="button" value="Save" onclick="salvarExperimento()">						
		</fieldset>	
	</form>	
</div>

</body>
</html>