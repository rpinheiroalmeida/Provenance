function stringParaDate(pDataHoraStringPadraoBrasil){
	if (pDataHoraStringPadraoBrasil != null && pDataHoraStringPadraoBrasil != ''){
		var dadosData = pDataHoraStringPadraoBrasil.substring(0,10).split("/");
		var horas     = pDataHoraStringPadraoBrasil.substring(11, 19);
		var novaData = dadosData[2]+"-"+dadosData[1]+"-"+dadosData[0];
		
		if (horas != '00:00:00'){
			novaData += ' ' +horas; 
		}
		return new Date(novaData);
	} else {
		return null;
	}
	
}

function validarDataInicioFim(pDataInicio, pDataFim){
	var dataHoraInicio = stringParaDate(pDataInicio); 
	var dataHoraFim    = stringParaDate(pDataFim);

	if (dataHoraInicio.getTime() > dataHoraFim.getTime()){
		return false;
	}
	return true;
	
}

function formatarData(dataString){
	//2014-03-01 00:00:00.0 BRT
	if (dataString != null && dataString != ''){
		var dadosData = dataString.substring(0,10).split("-");
		var horas     = dataString.substring(11, 19);
		var novaData = dadosData[2]+"/"+dadosData[1]+'/'+dadosData[0];
		
		if (horas != '00:00:00'){
			novaData += ' ' +horas; 
		}
		return novaData;
	} else {
		return dataString;
	}
}