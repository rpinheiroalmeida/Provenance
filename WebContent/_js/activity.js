jQuery(document).ready(function(){    

    $.datepicker.setDefaults($.extend({showMonthAfterYear: false}, $.datepicker.regional['pt-BR']));
    $("#txtInicioAtividade").datepicker($.datepicker.regional['pt-BR']);
    $("#txtFimAtividade").datepicker($.datepicker.regional['pt-BR']);
    $("#txtHoraInicioAtividade").mask("99:99");    
    $("#txtHoraFimAtividade").mask("99:99");
	
	$("#telaResultadoComando").dialog({
    	autoOpen: false,
    	height: 500,
		width: 500,
    	modal: true,
    	buttons: {
    		'Fechar': function() {
    			$(this).dialog('close');
    		},
    	}
	});
	
	$("#divProcessando").dialog({
    	autoOpen: false,
    	height: 200,
		width: 500,
    	modal: true,
	});
	
});


function salvarAtividade(){
	if (! isDadosValidosDaAtividade()){
		$.prompt("Informe arquivo do programa, datas e horas inicio e fim.");
		return;
	}
	
	//validar data e hora
	if (!validarDataInicioFim($('#txtInicioAtividade').val() + ' ' +$("#txtHoraInicioAtividade").val(), $('#txtFimAtividade').val() + ' ' +$("#txtHoraFimAtividade").val())){
		$.prompt("Data/hora iniciais maior que finais!");
		return;
	}	
	
	mostrarNomeArquivo();
	
	$.ajax({
	    url:'/provenance/save/activity',
	    dataType:'html',
	    data:$('#frmManterAtividade').serialize(),
	    type:'POST',
	    success: function( data ){
  	    	if (data == "sucesso"){
	    		$.prompt("Cadastro salvo com sucesso!");
	    		window.location.reload(true);
	    	} else {
	    		$.prompt("Erro no cadastro. Motivo:" + data);
	    	}
	    },	
	    error: function( xhr, er ){
	        $.prompt('Os dados n&atilde;o for&atilde;o salvos. Causa:' + data);
	    }
	});
}

function executarComando(){
	if (!canRunCommand()) {
		$.prompt("Command cannot be executed.");
		return;
	}
	$.ajax({
		url:'/provenance/runCommand',
		dataType: 'html',
		data:$('#frmManterAtividade').serialize(),
        type: 'POST',
	    beforeSend: function(){
	    	$("#divProcessando").dialog('open');
	    },  
	    success: function(data){
			$("#divProcessando").dialog('close');
			exibirResultadoNaTela(data);			
	    },
		error: function( xhr, er ){
			$.prompt('Ocorreu o seguinte problema na execu&ccedil;&atilde;o desse comando. Motivo: ' + xhr.status + ', ' + xhr.statusText + ' | ' + er);
			$("#divProcessando").dialog('close');
		}
	});		
}


function isDadosValidosDaAtividade(){
	
	if ($('#txtPrograma').val() == '' || 
		$("#txtInicioAtividade").val()=='' || 
		$("#txtFimAtividade").val() =='' || 
		$("#txtHoraInicioAtividade").val()==''|| 
		$("#txtHoraFimAtividade").val()=='') {
		return false;
	};    
	
	return true;
}

function canRunCommand(){
	if ($('#txtComando').val() == '') {
		return false;
	} 
	return true;
}

function deletarAtividade(codAtividade)
{
    var deletarReg = 
	function (v,m,f){
		if( v == true )
		{
			$.ajax({
				url:'../activity.do?acao=excluir',
				dataType:'text',
				data:{idAtividade:codAtividade},
				type:'POST',
				success: function( data, textStatus ){
					if( data == 'sucesso' ){
						$.prompt( 'Registro excluido com sucesso!'); 
						window.location.reload(true);						
					}
					else
					{
						$.prompt( 'Os dados n&atilde;o foram exclu&iacute;dos. Favor tentar novamente' );
					}
				},
				error: function( xhr, er ){
					$.prompt('Os dados n&atilde;o for&atilde;o salvos. Motivo: ' + xhr.status + ', ' + xhr.statusText + ' | ' + er);
				}
			});
		}
	};
	$.prompt( 'Deseja realmente excluir esta atividade?', {buttons: {'Sim':true, 'N&atilde;o':false}, callback: deletarReg} );	
}


function limparTela(){
	$("#txtPrograma").val("");		
	$("#txtVersao").val("");		
	$("#txtFuncao").val("");		
	$("#txtInicioAtividade").val("");				
	$("#txtHoraInicioAtividade").val("");
	$("#txtFimAtividade").val("");				
	$("#txtHoraFimAtividade").val("");		
	$("#txtComando").val("");
	$("#id").val("");
//	$("#idProjetoDaAtividade").val("");	
	$("#txtArquivo").val("");	
	$('#txtNomeArquivo').val("");	
	$("#nomeArquivoSelecionado").html("");
	$("#btnEnviar").hide('normal');
	reinicializarComboAccount();
}


function mostrarNomeArquivo(){
	var arquivo = $('#frameUpload').contents().find('#txtArquivo').val();
	var ultimoIndiceBarra = arquivo.lastIndexOf("\\")+1;
	if (ultimoIndiceBarra == -1){
		ultimoIndiceBarra = 0;
	}
	var nomeArquivo = arquivo.substring(ultimoIndiceBarra, arquivo.length);
	
	$("#txtNomeArquivo").val(nomeArquivo);
	$("#nomeArquivoSelecionado").html("Arquivo selecionado:" + nomeArquivo);

	if(nomeArquivo != ''){
		$("#btnEnviar").show('normal');
	} else {
		$("#btnEnviar").hide('normal');
	}
}

