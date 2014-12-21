<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@include file="header.jsp" %>
 <style type="text/css">
    body, html {
      font: 10pt sans;
      width: 100%;
      height: 100%;
      padding: 0;
      margin: 0;
      color: #4d4d4d;
    }

    #frame {
      width: 100%;
      height: 99%;
    }
    #frame td {
      padding: 10px;
      height: 100%;
    }
    #error {
      color: red;
    }

    #data {
      width: 100%;
      height: 100%;
      border: 1px solid #d3d3d3;
    }

    #mynetwork {
      float: left;
      width: 100%;
      height: 100%;
      border: 1px solid #d3d3d3;
      box-sizing: border-box;
      -moz-box-sizing: border-box;
      overflow: hidden;
    }

    textarea.example {
      display: none;
    }
  </style>
  
<link rel="stylesheet" type="text/css" href="../jquery/tabs.css"/>
<!--jquery-->	
<script src="../jquery/ui/jquery-ui-1.7.2.custom.js" type="text/javascript"></script>
<script src="../jquery/jquery.impromptu.2.7.js" type="text/javascript"></script>
<script src="../_js/search.js"></script>
<script src="http://www.webgraphviz.com/viz.js"></script>



<body>

 <script type="text/vnd.graphviz" id="cluster">
digraph G {

	subgraph cluster_0 {
		style=filled;
		color=lightgrey;
		node [style=filled,color=white];
		a0 -> a1 -> a2 -> a3;
		label = "process #1";
	}

	subgraph cluster_1 {
		node [style=filled];
		b0 -> b1 -> b2 -> b3;
		label = "process #2";
		color=blue
	}
	start -> a0;
	start -> b0;
	a1 -> b3;
	b2 -> a3;
	a3 -> a0;
	a3 -> end;
	b3 -> end;

	start [shape=Mdiamond];
	end [shape=Msquare];
}
</script>
      
<div id="dlgBusca">
	<form id="frmBusca">
		<fieldset class="cadastros">
				<legend>Searching</legend>
				<div  class="rotulo">Project:</div>
				<select id="cbxProject" name="idProject" style="width:500px">
					<option value="0">::Selecione::</option>
					<c:forEach var="proj" items="${listProject}"> 
						<option value="${proj.id}">${proj.nome}</option>
					</c:forEach>
				</select>
				<br>
		
				<div  class="rotulo">Account:</div>
				<select id="cbxAccount" name="idAccount" style="width:500px">
					<option value="0">::Selecione::</option>
					
				</select>
				<br>
				
				<div  class="rotulo">Group:</div>
				<select id="cbxGroup" name="idGroup" style="width:500px">
					<option value="0">::Selecione::</option>
					<c:forEach var="group" items="${listGroups}"> 
						<option value="${group.id}">${group.nome}</option>
					</c:forEach>
				</select>
				<br>
						
				<input type="button" value="Clean" onclick="limparTela()">
				<input type="button" value="Search" onclick="consultar()">						
		</fieldset>	
	</form>	
</div>
  
<fieldset id="cadastros" style="width:627px; height: 400px">
<div id="resultado_busca" style="width:627px; height: 400px">
</div>
</fieldset>

</body>
</html>