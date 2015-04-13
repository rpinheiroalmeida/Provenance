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
			showGraph(data);
		},
		error: function( xhr, er ){
			$.prompt('Os dados n&atilde;o for&atilde;o enviados. Motivo: ' + xhr.status + ', ' + xhr.statusText + ' | ' + er);
		}
	});	
	
}

function src(id) {
    return document.getElementById(id).innerHTML;
}

function showGraph(data) {
	var result;
//	data = "digraph G { " +
//		"subgraph cluster_0 { " +
//		"style=filled; " +
//		"color=lightgrey; " + 
//		"node [style=filled,color=white]; " +
//		"a0 -> a1 -> a2 -> a3; " +
//		"label = 'process #1'; " +
//	"}" +
//
//	"subgraph cluster_1 {  " +
//		"node [style=filled]; " +
//		"b0 -> b1 -> b2 -> b3; " +
//		"label = 'process #2';"+
//		 "color=blue " +
//	"}"
//	"start -> a0; " +
//	"start -> b0; " +
//	"a1 -> b3; " +
//	"b2 -> a3; " +
//	"a3 -> a0; " +
//	"a3 -> end; " +
//	"b3 -> end; " +
//
//	"start [shape=Mdiamond] " +
//	"end [shape=Msquare]; " +
//"}"
    try {
      result = Viz(data, "svg", "dot");
      document.getElementById('resultado_busca').innerHTML += result;
//      if (format === "svg")
//        return result;
//      else
//        return inspect(result);
    } catch(e) {
      console.log(e.toString());
//      return inspect(e.toString());
    }
}

//function showGraph(data) {
//	 try {
//	      // Provide a string with data in DOT language
//	      data = {
//	        dot: "digraph topology {"+ 
//  "node[shape=circle fontSize=12] " +
//  "edge[length=170 fontSize=12] " + 
//  "10.0.255.1 -> 10.0.255.3[label=1.000]; " + 
//  "10.0.255.1 -> 10.0.255.2[label=1.000]; " +
//  "10.0.255.1 -> 10.0.255.2[label=1.000]; " +
//  "10.0.255.1 -> 10.0.255.3[label=1.000]; " +
//  "10.0.255.2 -> 10.0.255.1[label=1.000]; " +
//  "10.0.255.2 -> 10.0.255.3[label=1.000]; " +
//  "10.0.255.3 -> 10.0.255.1[label=1.000]; " +
//  "10.0.255.3 -> 10.0.255.2[label=1.000]; " +
//  "10.0.255.3 -> 10.0.3.0[label=HNA, shape=solid]; " +
//  "10.0.3.0[shape=box]; " +
//  "10.0.255.2 -> 10.0.2.0[label=HNA]; " +
//  "10.0.2.0[shape=box]; " +
//  "10.0.255.1 -> 10.0.1.0[label=HNA]; " +
//  "10.0.1.0[shape=box] ;" +
//  "}"
//	      };
//
//	      // create a network
//	      var container = document.getElementById('resultado_busca');
//	      var options = {};
//	      network = new vis.Network(container, data, options);
//	      alert(data);
//	    }
//	    catch (err) {
//	      // set the cursor at the position where the error occurred
//	    	console.log(err);
//	      var match = /\(char (.*)\)/.exec(err);
//	      if (match) {
//	        var pos = Number(match[1]);
//	        if(txtData.setSelectionRange) {
//	          txtData.focus();
//	          txtData.setSelectionRange(pos, pos);
//	        }
//	      }
//
//	    }
////	alert('aa');
////	var container = document.getElementById('resultado_busca');
////	var data = {
////		dot: 'dinetwork {node[shape=square]; a -> a -> ab; ab -> abc; ab -- abcd; ab -> a; abc -> a; abc -> abcd }'
////	};
////	var network = new vis.Network(container, data);
//}

