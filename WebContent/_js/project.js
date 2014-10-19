jQuery(document).ready(function(){    

    $.datepicker.setDefaults($.extend({showMonthAfterYear: false}, $.datepicker.regional['pt-BR']));
    $("#txtInicio").datepicker($.datepicker.regional['pt-BR']);
    $("#txtFim").datepicker($.datepicker.regional['pt-BR']);
    
    ativaAbaParticipantes();
	$('#abaParticipante').click(function(){ ativaAbaParticipantes(); });
	$('#abaFinanciadora').click(function(){ ativaAbaFinanciadoras(); });
});


function salvarProjeto(){
	$("carregando").css("display", "inline");
	$('#lstFinanciadoras option').attr("selected", "selected");
	$('#lstParticipantes option').attr("selected", "selected");

	if (!isDadosValidos()){
		$.prompt('Informe nome do projeto, data inicial e data final.');
		return;
	}
	
	if (!validarDataInicioFim($('#txtInicio').val(), $('#txtFim').val())){
		$.prompt("Data inicial maior que final!");
		return;
	}		
//	$.prompt('/provenance/salvarProjeto');
	$.ajax({
	    url:'/provenance/salvarProjeto',
	    dataType:'html',
	    data:$('#frmManter').serialize(),
	    type:'POST',
	    success: function( data ){
  	    	if (data == "sucesso"){
  	    		$("carregando").css("display", "none");
	    		$.prompt("Cadastro salvo com sucesso!");
	    		window.location.reload(true);
	    	} else {
	    		$("carregando").css("display", "none");
	    		$.prompt("Erro no cadastro. Motivo:" + data);
	    	}
	    },	
	    error: function( xhr, er ){
	    	$("carregando").css("display", "none");
	        $.prompt('Os dados n&atilde;o for&atilde;o salvos. Causa:' + data);
	    }
	});
}

function isDadosValidos(){
	if ($("#txtNome").val()=='' || $("#txtInicio").val()=='' || $("#txtFim").val() =='') {
		return false;
	}
	return true;
	
}

function deletarProjeto()
{
    var deletarReg = 
	function (v,m,f){
		if( v == true )
		{
			$.ajax({
				url:'../project.do?acao=excluir',
				dataType:'text',
				data:{idProjeto:$("#idProjeto").val()},
				type:'POST',
				success: function( data, textStatus ){
					if( data == 'sucesso' ){
						$.prompt( 'Registro excluido com sucesso!'); 
						window.location.reload();						
					}
					else
					{
						$.prompt( 'Os dados n&atilde;o for&atilde;o exclu&iacute;do. Favor tentar novamente' );
					}
				},
				error: function( xhr, er ){
					$.prompt('Os dados n&atilde;o for&atilde;o salvos. Motivo: ' + xhr.status + ', ' + xhr.statusText + ' | ' + er);
				}
			});
		}
	};
	$.prompt( 'Deseja realmente excluir este projeto?', {buttons: {'Sim':true, 'N&atilde;o':false}, callback: deletarReg} );	
}


function ativaAbaParticipantes(){
	$('#inst_participantes').show('normal');
	$('#inst_financeiras').hide('normal');
	$('#abaParticipante').attr('class','abaAtiva');
	$('#abaFinanciadora').attr('class','abaInativa');	
	
}

function ativaAbaFinanciadoras(){
	$('#inst_financeiras').show('normal');
	$('#inst_participantes').hide('normal');
	$('#abaFinanciadora').attr('class','abaAtiva');
	$('#abaParticipante').attr('class','abaInativa');	
}


function adicionarNomeNaLista(pIdNome, pIdLista){
	var nome = $("#"+pIdNome).val();
	
	if (nome == ''){
		$.prompt("Informe um nome para adicionar!");
		return;
	}
	
	var tamLista = $("#"+pIdLista).find('option').size();
	var jaExiste = temRegistroNaLista(pIdLista, nome);
	if (tamLista == 0 || !jaExiste){
		var option = new Option(nome, nome);
		$("#"+pIdLista).append(option);
		$("#"+pIdNome).val('');
	} 
}

function temRegistroNaLista(pIdLista, pValue){
	var achou = false;
	$("#"+pIdLista + " option").each(function(){
		if ($(this).val() == pValue){
			$.prompt("Registro j&aacute; cadastrado na lista");
			achou = true;
		}
	});
	return achou;
}

function removerItem(pIdItem){
	$("#"+pIdItem + " option").each(function(){
		if ($(this).is(":selected")){
			$(this).remove();
		}
	});
}


function limparTela(){
	$.prompt('teste...');
	$("#txtNome").val("");
	$("#txtDescricao").val("");		
	$("#txtCoordenador").val("");		
	$("#txtInicio").val("");		
	$("#txtFim").val("");				
	$("#idProjeto").val("0");	
	$("#txtObservacao").val("");	
	$("#lstFinanciadoras option").remove();
	$("#lstParticipantes option").remove();
}




