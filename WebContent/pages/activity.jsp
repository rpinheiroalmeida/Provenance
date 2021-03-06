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

<script type="text/javascript">
$("#cbxProjectActivity").change(function(){ buscarAccounts(0); });
$("#cbxAccount").change(function(){ buscarGroups(0,0); });

function reinicializarComboAccount(){
	var optionSelected = $('option', '#cbxAccount')[0];
	$('option', '#cbxAccount').remove();
	$('#cbxAccount').append(optionSelected);
	
}

function buscarAccounts(idASelecionar){
	var id = $("#cbxProjectActivity").val();
	var url = "/provenance/accounts/project";
	reinicializarComboAccount();
	if (id > 0){
		$.ajax({
			url:url,
			dataType:'json',
			data: {idProject:+id},
			type:'GET',
			success: function( data ){
				var accounts = data.accounts;
				if (accounts.length > 0){
					//ser� alimentada a vari�vel html devido ao IE n�o funcionar com $("#cbxAccount").append(new Option(nome,id));
					for(var i = 0; i < accounts.length; i++){
						if (idASelecionar > 0 && accounts[i].id == idASelecionar ){
							selected = 'selected';
						} else {
							selected = '';
						}
						$("#cbxAccount").append("<option value='"+accounts[i].id+"' "+ selected+">"+accounts[i].nome+"</option>");
					}
				}
			},
			error: function( xhr, er ){
				$.prompt('Os dados n&atilde;o for&atilde;o enviados. Motivo: ' + xhr.status + ', ' + xhr.statusText + ' | ' + er);
			}
		});
	}
}

function buscarGroups(idASelecionar,idAccount){
	var id = idAccount == 0 ? $("#cbxAccount").val() : idAccount;
	if (id > 0){
		$.ajax({
			url:'/provenance/groups',
			data: {idAccount:id},
			dataType:'json',
			type:'GET',
			success: function( data ){
				var lista = data.list;
				if (lista.length > 0){
					for(var i = 0; i < lista.length; i++){
						if (idASelecionar > 0 && lista[i].id == idASelecionar ){
							selected = 'selected';
						} else {
							selected = '';
						}
						$("#cbxGroup").append("<option value='"+lista[i].id+"' "+ selected+">"+lista[i].nome+"</option>");
					}
				}
			},
			error: function( xhr, er ){
				$.prompt('Os dados n&atilde;o for&atilde;o enviados. Motivo: ' + xhr.status + ', ' + xhr.statusText + ' | ' + er);
			}
		});
	}	
}

function loadGroups(idAccount){
		$.ajax({
			url:'/provenance/groups',
			data: {idAccount:idAccount},
			dataType:'json',
			type:'GET',
			success: function( data ){
				var lista = data.list;
				alert('lista.length: ' + lista.length);
				if (lista.length > 0){
					for(var i = 0; i < lista.length; i++){
						if (idAccount > 0 && lista[i].id == idAccount){
							selected = 'selected';
						} else {
							selected = '';
						}
						$("#cbxGroup").append("<option value='"+lista[i].id+"' "+ selected+">"+lista[i].nome+"</option>");
					}
				}
			},
			error: function( xhr, er ){
				$.prompt('Os dados n&atilde;o for&atilde;o enviados. Motivo: ' + xhr.status + ', ' + xhr.statusText + ' | ' + er);
			}
		});
}

</script>

<body>
<div id="formCadAtividade" title="">
	<form id="frmManterAtividade">
		<fieldset class="cadastros">
				<legend>Activity</legend>
				<div  class="rotulo">Project:</div>
				<select id="cbxProjectActivity" name="activity.account.project.id" style="width:500px">
					<option value="0">::Selecione::</option>
					<c:forEach var="proj" items="${listProject}">
						<option value="${proj.id}">${proj.nome}</option>
					</c:forEach>
				</select>
				<br>
		
				<div  class="rotulo">Account:</div>
				<select id="cbxAccount" name="activity.account.id" style="width:500px">
					<option value="0">::Selecione::</option>
				</select>
				<br>

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
				<select id="cbxGroup" name="activity.group.id" style="width:500px">
					<option value="0">::Selecione::</option>
				</select>
				<br>
				
				<div class="rotulo">Command line:</div>
				<textarea id="txtComando" name="activity.linhaComando" rows="5" maxlength="2000" style="width: 500px">${activity.linhaComando}</textarea>
				<br>
				
				<div class="rotulo">File:</div>
				<div id="nomeArquivoSelecionado"></div>
				<iframe src="upload.jsp" id="frameUpload" style="width:500px"></iframe>		
				
				<br/>
				<input type="hidden" id="txtNomeArquivo" name="activity.nomeArquivo" value="${activity.nomeArquivo}"/>
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