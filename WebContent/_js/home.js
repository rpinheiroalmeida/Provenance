$(document).ready(function(){

//	$("document").ajaxStart($.blockUI).ajaxStop($.unblockUI);
	
	$("#projetos").treeview();
});

function abrirTela(nome, id){
	var nomeServlet = "";
	var paramsServlet = new Object();
	if (nome == "projeto"){
		nomeServlet = "/provenance/find/project";
		paramsServlet = "idProject="+id;
		nome = "project.jsp";
	} else if (nome == "experimento"){
		nomeServlet = "/provenance/find/account";
		paramsServlet = "idAccount="+id;
		nome = "experiments.jsp";
	} else if (nome == "atividade"){
		nomeServlet = "/provenance/find/activity";
		paramsServlet = "idActivity="+id;
		nome = "activity.jsp";
	} else {
		$.prompt("Página inválida!");
		return;
	}
	
	enviar(nomeServlet, nome, true, "exibicao_conteudo", paramsServlet);
}

function efetuarLogoff(){
	enviar('../login.do?acao=logoff', '../index.jsp', false, "");
}

function novoProjeto(){
	enviar("/provenance/novoProjeto", "project.jsp", true, "exibicao_conteudo");
	fecharTodosMenus();
}

function novoExperimento(idProjeto){
//	enviar("../experiment.do?acao=novo&idProjeto="+idProjeto, "experiments.jsp", true, "exibicao_conteudo");
	enviar("/provenance/novo/experimento", "experiments.jsp", true, "exibicao_conteudo", "idProject="+idProjeto);
	fecharTodosMenus();
}

function novaAtividade(idAccount){
//	enviar("../activity.do?acao=novo&idExperimento="+idExperimento, "activity.jsp", true, "exibicao_conteudo");
	enviar("/provenance/new/activity", "activity.jsp", true, "exibicao_conteudo", "idAccount="+idAccount);
	fecharTodosMenus();
}

function visualizarExperimento(idExperimento){
	enviar("../experiment.do?acao=visualizarGrafo&idExperimento="+idExperimento, "visualizarExperimento.jsp", true, "exibicao_conteudo");
	fecharTodosMenus();
}


function enviar(pUrl, pNomeTelaAbrir, pAbreNoFrame, pIdTela, pData ){
	$.ajax({
		url:pUrl,
		dataType:'json',
		type:'GET',
		data: pData,
		success: function( data ){
			console.log(data);
			if (pNomeTelaAbrir == 'project.jsp') {
				$("#"+pIdTela).load(pNomeTelaAbrir, data.project, function() {
					loadDataProject(data);
				});
			} else if (pNomeTelaAbrir == 'experiments.jsp') {
				$("#"+pIdTela).load(pNomeTelaAbrir, data.account, function() {
					loadDataAccount(data);
				});
			} else if (pNomeTelaAbrir == 'activity.jsp') {
				$("#"+pIdTela).load(pNomeTelaAbrir, data.activity, function() {
					loadDataActivity(data);
				});
			}
		},
		error: function( xhr, er ){
			$.prompt('Os dados n&atilde;o for&atilde;o enviados. Motivo: ' + xhr.status + ', ' + xhr.statusText + ' | ' + er);
		}
	});	
	
}

function fecharTodosMenus(){
	$(".menu_contexto").each(function(){
		$(this).hide('normal');
	});	
}

function loadDataProject(data) {
	$('#txtNome').val(data.project.nome);
	$('#txtDescricao').val(data.project.descricao);
	$('#txtCoordenador').val(data.project.coordenador);
	$('#txtInicio').val(formatarData(data.project.dataHoraInicio));
	$('#txtFim').val(formatarData(data.project.dataHoraFim));
	$('#txtObservacao').val(data.project.observacao);
}

function loadDataAccount(data) {
	$('#txtNome').val(data.account.nome);
	$('#txtDescricao').val(data.account.descricao);
	$('#txtLocal').val(data.account.localExecucao);
	$('#txtVersao').val(data.account.versao);
	$('#txtDataInicio').val(formatarData(data.account.dataHoraInicio));
	$('#txtDataFim').val(formatarData(data.account.dataHoraFim));
	$('#txtDataVersao').val(data.account.dataVersao);
	$('#txtObservacao').val(data.account.anotacoes);
	if (data.account.project != null) {
		$('#idProjeto').val(data.account.project.id);
	}
}

function loadDataActivity(data) {

	$('#txtNome').val(data.activity.nome);
	$('#txtPrograma').val(data.activity.nomePrograma);
	$('#txtVersao').val(data.activity.versaoPrograma); 
	$('#txtComando').val(data.activity.linhaComando);
	$('#txtFuncao').val(data.activity.funcao);
	$('#txtInicioAtividade').val(formatarData(data.activity.dataInicio));
	$('#txtHoraInicioAtividade').val(data.activity.horaInicio);
	$('#txtFimAtividade').val(formatarData(data.activity.dataFim));
	$('#txtHoraFimAtividade').val(data.activity.horaFim);
	if (data.activity.account != null) {
		$('#idExperimento').val(data.activity.account.id);
	}
}

function mostrarContexto(pElemOrigem, pIdMenu){
	//pegar a posição do clique
	fecharTodosMenus();
	var menu = document.getElementById(pIdMenu);
	var posicaoElemento = getPosicaoElemento(pElemOrigem.id);
	var largura = (pElemOrigem.width != null)? pElemOrigem.width : 50;
	
	var left = posicaoElemento.left + (largura/2);
	var top = posicaoElemento.top + (pElemOrigem.height/2);
	
	menu.style.visibility = "visible";
	menu.style.display = "block";
	menu.style.position="absolute";
	menu.style.top = top + "px";
	menu.style.left= left + "px";
	menu.style.border="1px solid #000000";
	menu.style.background="#ffffff";
	menu.style.boxShadow= "5px 5px 5px rgba(0,0,0, 0.4)";
}	

function apagarContexto(pIdMenu){
	$("#"+pIdMenu).hide('normal');
}

function getPosicaoElemento(elemID){
    var offsetTrail = document.getElementById(elemID);
    var offsetLeft = 0;
    var offsetTop = 0;
    while (offsetTrail) {
        offsetLeft += offsetTrail.offsetLeft;
        offsetTop += offsetTrail.offsetTop;
        offsetTrail = offsetTrail.offsetParent;
    }
    if (navigator.userAgent.indexOf("Mac") != -1 && 
        typeof document.body.leftMargin != "undefined") {
        offsetLeft += document.body.leftMargin;
        offsetTop += document.body.topMargin;
    }
    return {left:offsetLeft, top:offsetTop};
}

function formatarData(date){
	var myDate = date.replace(/^(\d{4})\-(\d{2})\-(\d{2}).*$/, '$2/$3/$1');
	console.log(myDate);
    return myDate;
}
