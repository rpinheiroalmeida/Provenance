jQuery(document).ready(function(){    
	$("#cbxProject").change(function(){ buscarAccountsFromProject(); });
});

//carregar dados iniciais: combos projeto e grupo

function limparTela(){
	$("#cbxProject").val("");
	$("#cbxGroup").val("");
	$("#cbxAccount").val("");
	reinicializarComboAccount();
}

function reinicializarComboAccount(){
	var optionSelected = $('option', '#cbxAccount')[0];
	$('option', '#cbxAccount').remove();
	$('#cbxAccount').append(optionSelected);
	
}

function buscarAccountsFromProject(){
	var id = $("#cbxProject").val();
	var url = "/provenance/busca/accounts";
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
						$("#cbxAccount").append("<option value='"+accounts[i].id+"'>"+accounts[i].nome+"</option>");
					}
					
				}
			},
			error: function( xhr, er ){
				$.prompt('Os dados n&atilde;o for&atilde;o enviados. Motivo: ' + xhr.status + ', ' + xhr.statusText + ' | ' + er);
			}
		});
	}
}

function consultar(){
	$.ajax({
		url:'/provenance/busca/search',
		dataType:'http',
		type:'GET',
		data: $("#frmBusca").serialize(),
		success: function( data ){
			$("#resultado_busca").val(data);
		},
		error: function( xhr, er ){
			$.prompt('Os dados n&atilde;o for&atilde;o enviados. Motivo: ' + xhr.status + ', ' + xhr.statusText + ' | ' + er);
		}
	});	
	
}