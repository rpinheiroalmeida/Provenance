<%@page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link rel="stylesheet" type="text/css" href="../jquery/tabs.css"/>

<!--jquery-->	
<script src="../jquery/ui/i18n/ui.datepicker-pt-BR.js" type="text/javascript"></script>    
<script src="../jquery/jquery.impromptu.2.7.js" type="text/javascript"></script>
<script src="../jquery/ui/jquery-ui-1.7.2.custom.js" type="text/javascript"></script>
<script src="../jquery/ui/i18n/ui.datepicker-pt-BR.js" type="text/javascript"></script>    
<script src="../jquery/jquery.maskedinput-1.2.2.js" type="text/javascript"></script>
<script src="../jquery/jquery.impromptu.2.7.js" type="text/javascript"></script>
<script src="../_js/activity.js"></script>


<body>
<div id="formCadAtividade" title="">
	<form id="frmManterAtividade">
		<fieldset class="cadastros">
				<legend>Activity</legend>
				<div  class="rotulo">Name:</div>
				<input type=text id="txtNome" name="activity.nome" maxlength="50" style="width:500px"  value="${activity.nome}">
				<br>
				
				<div  class="rotulo">Program:</div>
				<input type=text id="txtPrograma" name="activity.nomePrograma" maxlength="50" style="width:500px"  value="${activity.nomePrograma}">
				<br>
				
				<div class="rotulo">Version:</div>
				<input type="text" id="txtVersao" name="activity.versaoPrograma" maxlength="50" size="22"  value="${activity.versaoPrograma}">
				<br>

				<div class="rotulo">Function:</div>
				<input type="text" id="txtFuncao" name="activity.funcao" maxlength="50" style="width:500px"  value="${activity.funcao}">
				<br>
				
				<div class="rotulo">Start Date/Hour:</div>
				<input type="text" id="txtInicioAtividade" name="activity.dataInicio" maxlength="50" size="20"  value="<fmt:formatDate value="${activity.dataInicio}" pattern="dd/MM/yyyy"/>">
				<input type="text" id="txtHoraInicioAtividade" name="activity.horaInicio" maxlength="50" size="10"  value="<fmt:formatDate value="${activity.horaInicio}" pattern="hh:mm"/>">
				<br>
				
				<div class="rotulo">End Date/Hour:</div>
				<input type="text" id="txtFimAtividade" name="activity.dataFim" maxlength="50" size="20"  value="<fmt:formatDate value="${activity.dataFim}" pattern="dd/MM/yyyy"/>">
				<input type="text" id="txtHoraFimAtividade" name="activity.horaFim" maxlength="50" size="10"  value="<fmt:formatDate value="${activity.horaFim}" pattern="hh:mm"/>">
				<br>
				
				<div class="rotulo">Group:</div>
				<input type=text list=browsers >
				<datalist id=browsers >
   					<option> Google
   					<option> IE9
				</datalist>
				<br>
				
				<div class="rotulo">Command line:</div>
				<textarea id="txtComando" name="activity.linhaComando" rows="5" maxlength="100" style="width: 500px">${activity.linhaComando}</textarea>
				<br>
				
				<div class="rotulo">File:</div>
				<div id="nomeArquivoSelecionado"></div>
				<iframe src="upload.jsp" id="frameUpload" style="width:500px"></iframe>		
				<br>
				<input type="hidden" id="activity.txtNomeArquivo" name="nomeArquivo"/>
				<input type="hidden" name="acao" value="salvar"/>
				
				<input type="hidden" id="idExperimento" name="activity.account.id" value="${activity.account.id}"/>
				
				<input type="button" value="Run command" onclick="executarComando()">
				<input type="button" value="Clean" onclick="limparTela()">
				<c:choose>
					<c:when test="${not empty activity.id && activity.id gt 0}">
					    <input type="hidden" name="activity.id" id="idAtividade" value="${activity.id}"/>
						<input type="button" value="Clean" onclick="deletarAtividade()"/>			
					</c:when>
					<c:otherwise>
						<input type="hidden" name="activity.id" id="idAtividade" value="0"/>		
					</c:otherwise>
				</c:choose>
				
				
				<input type="button" value="Save" onclick="salvarAtividade()">
		</fieldset>
	</form>

</div>
				

<div id="users-contain" class="ui-widget" style="display:none">
	<div id="telaResultadoComando">
		<textarea id="txtResultadoComando" style="width:450px; height: 380px" disabled></textarea>
	</div>
</div>

<div id="users-contain" class="ui-widget" style="display:none">
	<div id="divProcessando">
		<img src="../_img/loading_50_50.gif" id="imgProcessando" style="margin-left: 200px"/>
		<h1>Aguarde enquanto o comando est&aacute; sendo executado!!!</h1>
	</div>
</div>


</body>
</html>