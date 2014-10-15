jQuery(document).ready(function(){    

    $.datepicker.setDefaults($.extend({showMonthAfterYear: false}, $.datepicker.regional['pt-BR']));
    $("#txtDataInicio").datepicker($.datepicker.regional['pt-BR']);
    $("#txtDataFim").datepicker($.datepicker.regional['pt-BR']);
    $("#txtDataVersao").datepicker($.datepicker.regional['pt-BR']);
    
});

function salvarExperimento(){
	if (! isDadosValidos() ){
		$.prompt("Informe nome, local, vers&atilde;o, data inicio, data fim e data da versao");
		return false;
	}
	
	if (!validarDataInicioFim($('#txtDataInicio').val(), $('#txtDataFim').val())){
		$.prompt("Data inicial maior que final!");
		return false;
	}		
	
	$.ajax({
	    url:'/provenance/salvar/account',
	    dataType:'html',
	    data:$('#frmManter').serialize(),
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


function isDadosValidos(){
	
	if ($('#txtNome').val() == '' || 
		$("#txtLocal").val()=='' || 
		$("#txtVersao").val() =='' || 
		$("#txtDataInicio").val()==''||
		$("#txtDataInicio").val()==''|| 
		$("#txtDataVersao").val()=='') {
		return false;
	};    
	
	return true;
	
}

function deletarProjeto()
{
    var deletarReg = 
	function (v,m,f){
		if( v == true )
		{
			$.ajax({
				url:'../experiment.do?acao=excluir',
				dataType:'text',
				data:{idExperimento:$("#idExperimento").val()},
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

function limparTela(){
	$("#txtNome").val("");	
	$("#txtLocal").val("");		
	$("#txtVersao").val("");		
	$("#txtDataInicio").val("");				
	$("#txtDataFim").val("");
	$("#txtDataVersao").val("");
	$("#txtObservacao").val("");
	$("#txtDescricao").val("");
	
}
