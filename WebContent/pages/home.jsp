<%@include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- <script src="http://d3js.org/d3.v3.min.js" type="text/javascript" charset="utf-8"></script> -->
<script>
function init(){
  alert("Buceta...");
}
</script>
<body>

<div class="tela">
	<header>
		<img src="../_img/logo.png"/>
	</header>

	<aside>
		<a href="#" onClick="efetuarLogoff()"><img src="../_img/exit.png">&lt&lt Saída</img></a>	
	
		<nav id="menus">
			 
				<ul>
					<div class="item_menu">
					<li><img src="../_img/menu_projetos.png" width="100%" id="menu_projetos" oncontextmenu="mostrarContexto(this, 'novo_projeto'); return false;"></li>
					</div>
					<li class="item_menu">
					<c:choose>
						<c:when test="${not empty listaProjetos}">
							<div id="projetos_cadastrados" style="height:450px; overflow:scroll; width:100%">
								<ul class="filetree" id="projetos">
								<c:forEach var="proj" items="${listaProjetos}">
									<li>
										<span class="folder" id="abrirProjeto" oncontextmenu="mostrarContexto(this, 'novo_experimento<c:out value="${proj.id}"/>'); return false;" onclick="abrirTela('projeto', <c:out value="${proj.id}"/>)"><c:out value="${proj.nome}"/>
										</span>
							 		    <span class="menu_contexto" id="novo_experimento<c:out value="${proj.id}"/>"><a href="javascript:void(0)" onclick="novoExperimento(<c:out value="${proj.id}"/>)"><img src="../_img/adiciona.png"> Adicionar Experimento </img></a><
							 		    /span>
									
									<c:if test="${not empty proj.accounts}">
										<ul>
											<c:forEach var="exp" items="${proj.accounts}">
												<li>
													<span class="folder" oncontextmenu="mostrarContexto(this, 'nova_atividade<c:out value="${exp.id}"/>'); return false;" 
														onclick="abrirTela('experimento', <c:out value="${exp.id}"/>)">${exp.nome}
													</span>
												 	<span class="menu_contexto" id="nova_atividade<c:out value="${exp.id}"/>">
												 		<a href="javascript:void(0)" onclick="novaAtividade(<c:out value="${exp.id}"/>)">
												 			<img src="../_img/adiciona.png">Adicionar Atividade </img>
												 		</a>
												 		<br/>
												 		<a href="javascript:void(0)" onclick="visualizarExperimento(<c:out value="${exp.id}"/>)">
												 			<img src="../_img/adiciona.png">Visualizar Experimento </img>
												 		</a>
												 	</span>
													<c:if test="${not empty exp.atividades}">
													<ul>
													<c:forEach var="ativ" items="${exp.atividades}">
														<li><span class="file" oncontextmenu="mostrarContexto(this, 'nova_atividade2'); return false;" onclick="abrirTela('atividade', <c:out value="${ativ.id}"/>)">${ativ.nomeAtividade}</span></li>
													</c:forEach>
													</ul>
													</c:if>
												</li>
											</c:forEach>
										</ul>					
									</c:if>
								</c:forEach>
								</ul>
							</div>
						</c:when>
						
						<c:otherwise>
							<h1>Nenhum projeto cadastrado</h1>
						</c:otherwise>
					</c:choose>
					</li>
					<div class="menu_contexto">
						<li><img src="../_img/menu_sistemas.png" width="100%"></li>

					</div>
	 			</ul>
		</nav>
	</aside>
	
	<section id="corpo">
		<article id="exibicao_conteudo" >
			<img src="../_img/logo_inicial.png" alt="Logo do sistema" id="img_home"/>
		</article>	
	</section>
	
</div>

<span class="menu_contexto" id="novo_projeto"><a href="#" onClick="novoProjeto()"><img src="../_img/adiciona.png">Novo Projeto</img></a></span>

</body>
</html>