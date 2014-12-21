<%@page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="header.jsp" %>
<link rel="stylesheet" href="../_css/group.css"/>	
<link rel="stylesheet" type="text/css" href="../jquery/tabs.css"/>

<!--jquery-->	
<script src="../jquery/ui/jquery-ui-1.7.2.custom.js" type="text/javascript"></script>
<script src="../_js/group.js"></script>

<script type="text/javascript">
$("#cbxProjectActivity").change(function(){ buscarAccounts(0); });

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
					//será alimentada a variável html devido ao IE não funcionar com $("#cbxAccount").append(new Option(nome,id));
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

function buscarGroups(idASelecionar){
	
	if (idASelecionar > 0){
		$.ajax({
			url:'/provenance/groups',
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

</script>

	
<body>

<div id="formCadGrupo">
	
	<form id="frmManter">
		<fieldset class="cadastros">
			<legend>Group</legend>
			<div  class="rotulo">Project:</div>
			<select id="cbxProjectActivity" name="group.account.project.id" style="width:500px">
				<option value="0">::Selecione::</option>
					<c:forEach var="proj" items="${listProject}">
						<option value="${proj.id}">${proj.nome}</option>
					</c:forEach>
			</select>
			<br>
		
			<div  class="rotulo">Account:</div>
			<select id="cbxAccount" name="group.account.id" style="width:500px">
				<option value="0">::Selecione::</option>
			</select>
			<br>
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