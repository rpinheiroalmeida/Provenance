<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<meta charset="UTF-8">
	<title>Universidade de Brasília</title>
	<!--[if lt IE 9]>
	   <link rel="stylesheet" type="text/css" href="_css/style_ie.css"/>
	   <script>
	      document.createElement('header');
	      document.createElement('nav');
	      document.createElement('section');
	      document.createElement('article');
	      document.createElement('aside');
	      document.createElement('footer');
	   </script>
	<![endif]-->	
	<link rel="stylesheet" href="_css/login.css"/>
	<link rel="stylesheet" href="_css/common_style.css"/>	
	<link rel="stylesheet" type="text/css" href="jquery/tabs.css"/>
	
	<!--jquery-->	
    <!-- <script src="jquery/jquery-1.11.1.js" type="text/javascript"></script> -->
    <script src="jquery/jquery.js" type="text/javascript"></script>
    <script src="jquery/jquery.impromptu.2.7.js" type="text/javascript"></script>
    <script src="jquery/ui/jquery-ui-1.7.2.custom.js" type="text/javascript"></script>
    <script src="jquery/ui/ui.dialog.js" type="text/javascript"></script>
	<script src="_js/validacao.js" type="text/javascript"></script>    
    <script src="_js/login.js"></script>
    
</head>
<body>

<header>
	<img src="_img/logo.png"/>
</header>

<section id="corpo" style="width:100%">
	<article id="login">
		<form id="frmLogin" action="<c:url value="/login"/>" method="POST">
			<fieldset id="containerLogin" name="containerLogin">
			<legend>Identifica&ccedil;&atilde;o</legend>
				<div class="rotulo"><label for="txtLogin" class="rotulos_senha"><span>Login</span></label></div>
				<input type="text" id="txtLogin" name="usuario.login" required  title="Digite o login" maxlength ="20" 
					placeholder="Nome de seu usu&aacute;rio" class="inputs">
				<br>
				<br>
				
				<div class="rotulo"><label for="txtSenha" class="rotulos_senha">Senha</label></div>
				<input type="password" id="txtSenha" name="usuario.senha" required="required" maxlength ="20" placeholder="M&aacute;ximo 20 caracteres"  class="inputs">
				<br>
				<br>
				
				<div style="margin-left:30px">
					<img src="_img/lock1.png" title="Efetuar Login" class="img_botoes" id="img_autenticar" onclick="autenticarUsuario()"/>
				</div>
				
				<div>
					<a href="javascript:void(0)" onclick="abrirTelaCadastroUsuario()"><span>N&atilde;o tenho usu&aacute;rio, quero me cadastrar.</span></a>
				</div>
				
		  </fieldset>
		  
		  <span id='resultado'></span>
		</form>
	</article>
	
	<article id="cadastro">
		<div id="users-contain" class="ui-widget">
			<div id="frmCadastroUsuario">
				<form id="formCadastroUsuario">
					<div class="rotulo">Nome</div>
					<input type="text" name="usuario.nome" id="nomeCadastro" maxlength="50" class="inputs" required/> 
					
					<div class="rotulo">Usu&aacute;rio</div>
					<input type="text" name="usuario.login" id="loginCadastro" maxlength="50" class="inputs" required/>
					
					<div class="rotulo">Senha</div>
					<input type="password" name="usuario.senha" id="senhaCadastro" maxlength="20" class="inputs" required/>
					
					<button type="submit">Salvar</button>
				</form>
			</div>
		</div>
	</article>
	
	
</section>

</body>

</html>

