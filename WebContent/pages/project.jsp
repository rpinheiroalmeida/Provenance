<%@page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="header.jsp" %>
<link rel="stylesheet" href="../_css/project.css"/>	
<link rel="stylesheet" type="text/css" href="../jquery/tabs.css"/>

<!--jquery-->	
<script src="../jquery/ui/jquery-ui-1.7.2.custom.js" type="text/javascript"></script>
<script src="../jquery/ui/i18n/ui.datepicker-pt-BR.js" type="text/javascript"></script>    
<script src="../jquery/jquery.maskedinput-1.2.2.js" type="text/javascript"></script>
<script src="../jquery/jquery.impromptu.2.7.js" type="text/javascript"></script>
<!--  <script src="../jquery/jquery/ui/ui.tabs.js" type="text/javascript"></script> -->    
<script src="../_js/project.js"></script>
	
<body>

<div id="formCadProjeto">
	
	<form id="frmManter">
		<fieldset class="cadastros">
			<legend>Project</legend>
			<div class="rotulo">Name:</div>
			<input type="text" id="txtNome" name="project.nome" maxlength="50" size="100" value="${project.nome}"><br>				
			
			<div class="rotulo">Description:</div><input type="text" id="txtDescricao" name="project.descricao" maxlength="50" size="100"  value="${project.descricao}"><br>
			<div class="rotulo">Coordinator:</div><input type="text" id="txtCoordenador" name="project.coordenador" maxlength="50" size="100"  value="${project.coordenador}"><br>
									
			<div class="rotulo">Start Date:</div><input type="text" id="txtInicio" name="project.dataHoraInicio" value="<fmt:formatDate value="${project.dataHoraInicio}" pattern="dd/MM/yyyy"/>"><br>				
		    <div class="rotulo">End Date:</div><input type="text" id="txtFim" name="project.dataHoraFim"  value="<fmt:formatDate value="${project.dataHoraFim}" pattern="dd/MM/yyyy"/>"><br>
								
			<div class="rotulo">Observation:</div><textarea id="txtObservacao" name="project.observacao" rows="4" cols="76">${project.observacao}</textarea><br>
			
			<div id="abaAcesso">
				<div id="abaParticipante" class="abaAtiva" >Participating institutions</div>
				<div id="abaFinanciadora" class="abaInativa" >Funding institutions</div>
							
				<div id="inst_participantes"> 
					<input type="text" id="txtNomesParticipantes" size="90" maxlength="50">
					<img src="../_img/adiciona.png" id="imgAdicionaParticipante" name="imgAddParticipante" alt="Adicionar" onclick="adicionarNomeNaLista('txtNomesParticipantes','lstParticipantes')"/>
					<img src="../_img/cancel.png" id="imgRemoveParticipante" name="imgDelParticipante" alt="Remover" onclick="removerItem('lstParticipantes')"/>					
					<div>
						<select multiple class="lista_instituicoes" name="nomesInstituicoesParticipantes" id="lstParticipantes">
						<c:if test="${not empty project.nomesInstituicoesParticipantes}">
							<c:forEach var="participante" items="${project.nomesInstituicoesParticipantes}">
								<option value="${participante}">${participante}</option>
							</c:forEach>
						</c:if>
						</select>
					</div>
				</div>
	
				<div id="inst_financeiras">
					<input type="text" id="txtNomesFinanciadores" size="90" maxlength="50">
					<img src="../_img/adiciona.png" id="imgAdicionaFinanciadora" name="imgAddFinanciadora" alt="Adicionar" onclick="adicionarNomeNaLista('txtNomesFinanciadores','lstFinanciadoras')"/>
					<img src="../_img/cancel.png" id="imgRemoveFinanciadora" name="imgDelFinanciadora" alt="Remover" onclick="removerItem('lstFinanciadoras')"/>				
					
					<div>
						<select multiple class="lista_instituicoes" name="nomesInstituicoesFinanciadoras" id="lstFinanciadoras">
						<c:if test="${not empty project.nomesInstituicoesFinanciadoras}">
							<c:forEach var="financiadora" items="${project.nomesInstituicoesFinanciadoras}">
								<option value="${financiadora}">${financiadora}</option>
							</c:forEach>
						</c:if>
						
						</select>
					</div>
									
				</div>
			</div>
			<input type="hidden" name="acao" value="salvar"/>
			<input type="button" value="Clean" onclick="limparTela()">
			<c:choose>
				<c:when test="${not empty project.id && project.id gt 0}">
					<input type="hidden" name="idProjeto" id="idProjeto" value="<c:out value="${project.id}"/>"/>
					<input type="button" id="excluir" value="Delete">			
				</c:when>
				<c:otherwise>
					<input type="hidden" name="idProjeto" id="idProjeto" value="0"/>
				</c:otherwise>
			</c:choose>
			<input type="button" value="Save" onclick="salvarProjeto()">

		</fieldset>
	</form>
	
</div>

</body>
</html>