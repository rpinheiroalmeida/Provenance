jQuery(document).ready(function(){    

});

function saveGroup(){
	if (! isDadosValidos() ){
		$.prompt("Informe nome do grupo");
		return false;
	}
	
	$.ajax({
	    url:'/provenance/save/group',
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
	
	if ($('#txtNome').val() == ''){ 
		return false;
	};    
	
	return true;
	
}


function limparTela(){
	$("#txtNome").val("");	
}
