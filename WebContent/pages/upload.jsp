<!DOCTYPE html>
<html lang="pt-br">
<meta charset="UTF-8">
<body>
	<form method="post" enctype="multipart/form-data" id="frmUpload" action="/provenance/upload">
		<table style="width:100%">
			 <tr>
			 	<td>
			 		<input type="file" id="txtArquivo" name="uploadFiles[]" multiple="multiple" />
			 	</td>
		     </tr>
		     <tr>
		     	<td><input type="submit" id="btnEnviar" value="Start Upload"></td>
		     </tr>
		</table>
	</form>
</body>