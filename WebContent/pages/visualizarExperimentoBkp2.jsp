<%@page import="java.util.Enumeration"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@include file="header.jsp" %>

<link rel="stylesheet" type="text/css" href="../jquery/tabs.css"/>

<!--jquery-->	
<script src="../jquery/ui/i18n/ui.datepicker-pt-BR.js" type="text/javascript"></script>    
<script src="../jquery/jquery.impromptu.2.7.js" type="text/javascript"></script>
<script src="../jquery/ui/jquery-ui-1.7.2.custom.js" type="text/javascript"></script>
<script src="../jquery/ui/i18n/ui.datepicker-pt-BR.js" type="text/javascript"></script>    
<script src="../jquery/jquery.maskedinput-1.2.2.js" type="text/javascript"></script>
<script src="../jquery/jquery.impromptu.2.7.js" type="text/javascript"></script>
<script src="../_js/experiment.js"></script>

<body>
<script type="text/javascript">
		   var w = 1000;
		   var h = 800;
		   var linkDistance=100;
            
           
           var dataset = '<c:out value="${data}"/>';
           dataset = dataset.replace(/&#039;/g, '\'');
           dataset = dataset.replace(/&#034;/g, '\"');
           dataset = eval("(" + dataset + ")");
           /* alert(data); */
           /* var data = {nodes : [{id:347827, name: 'A11', type:'Activity' },{id:347828, name: 'http.conf.old', type:'Collection' },{id:242670, name: 'adm', type:'Agent' },{id:339740, name: 'A10', type:'Activity' },{id:339741, name: 'blogPostDAO.pyc', type:'Collection' }] , links : [{source : 347827, target : 347828},{source : 347827, target : 242670},{source : 339740, target : 339741},{source : 339740, target : 242670}]}; */ 

            // make links reference nodes directly for this particular data format:
    	    var hash_lookup = [];
        	 // make it so we can lookup nodes in O(1):
         	dataset.nodes.forEach(function(d, i) {
           		hash_lookup[d.id] = d;
         	});
         	dataset.edges.forEach(function(d, i) {
           		d.source = hash_lookup[d.source];
           		d.target = hash_lookup[d.target];
         	});
         	// and now we have Links[] and Nodes[] ready for use by D3::force...

         /* force.nodes(json.Nodes);
         force.links(json.Links); */
         var svg = d3.select("body").append("div").append("svg").attr({"width":w,"height":h});
         var svg = d3.select("section").append("svg").attr({"width":w,"height":h});

         svg.append("defs")
         	.append("mask")
         	.attr("id", "maskAgent")
         	.attr("x", 0)
         	.attr("y", 0)
         	.attr("width", 100)
         	.attr("height", 100)
         		.append("path")
         		.attr("id", "svg_1")
         		.attr("d", "m199.99998,168.15999l31.09076,0l25.90926,28.57805l-25.90926,28.57823l-31.09076,0l0,-57.15628z")
         		.attr("stroke-width", 2)
         		.attr("stroke", "#000000")
         		.attr("color", "gray")
         		.attr("fill", "#ffaa56");

 		 svg.append("defs")
 		 	.append("mask")
 		 	.attr("id", "maskCollection")
 		 	.attr("x", 0)
 		 	.attr("y", 0)
 		 	.attr("width", 100)
         	.attr("height", 100)
         		.append("circle")
         		.attr("id", "svg_2")
         		.attr("cx", 25)
         		.attr("cy", 25)
         		.attr("r", 25)
         		.attr("color", "blue")
         		.attr("fill", "#00BFFF");
         		/* .attr("style", "stroke:none; fill: #00BFFF"); */

 		svg.append("defs")
		 	.append("mask")
		 	.attr("id", "maskActivity")
		 	.attr("x", 0)
		 	.attr("y", 0)
		 	.attr("width", 100)
     		.attr("height", 100)
     			.append("rect")
     			.attr("id", "svg_3")
     			.attr("width",  50)
     			.attr("height", 50)
     			.attr("style", "stroke:none; fill: #ffffff");

        var force = d3.layout.force()
             .nodes(dataset.nodes)
             .links(dataset.edges)
             .size([w,h])
             .linkDistance([linkDistance])
             .charge([-500])
             .theta(0.1)
             .gravity(0.05)
             .start();
             
         var drag = force.drag()
         				 .on("dragstart", dragstart);
         
         var edges = svg.selectAll("line")
           .data(dataset.edges)
           .enter()
           .append("line")
           .attr("id",function(d,i) {return 'edge'+i; })
           .attr('marker-end','url(#arrowhead)')
           .style("stroke","#ccc")
           .style("pointer-events", "none");
         
         var nodes = svg.selectAll(".node")
           .data(dataset.nodes)
           .enter()
           .append("g")
           .attr("class", "node")
           .call(force.drag);
           
         nodes.append("g")
              .append("rect")
              .attr("width", 300)
              .attr("height", 300)
              .attr("mask", function(d) {
              	if (d.type == 'Agent') return "url(#maskAgent)";
                else if (d.type == 'Collection') return "url(#maskCollection)";
                else return "url(#maskActivity)";
               })
               .attr("fill", function(d) {
					if (d.type == 'Collection') return "#00BFFF";
					else if (d.type == 'Agent') return "#D3D3D3";
					else return "#f4ad6b";
               })
               .attr("stroke-width", 2)
               .attr("d", "m199.99998,168.15999l31.09076,0l25.90926,28.57805l-25.90926,28.57823l-31.09076,0l0,-57.15628z")
               .attr("transform", "rotate(-90.11238861083984 228.5,196.73812866210938)")
               .attr("class", "node")
               .call(drag);


         var nodelabels = svg.selectAll(".nodelabel") 
            .data(dataset.nodes)
            .enter()
            .append("text")
            .attr({"x":function(d){return d.x;},
                   "y":function(d){return d.y;},
                   "class":"nodelabel",
                   "stroke":"black"})
            .text(function(d){return d.name;});

          var edgepaths = svg.selectAll(".edgepath")
             .data(dataset.edges)
             .enter()
             .append('path')
             .attr({'d': function(d) {return 'M '+d.source.x+' '+d.source.y+' L '+ d.target.x +' '+d.target.y; },
                    'class':'edgepath',
                    'fill-opacity':0,
                    'stroke-opacity':0,
                    'fill':'blue',
                    'stroke':'red',
                    'id':function(d,i) {return 'edgepath'+i; }})
             .style("pointer-events", "none");

         var edgelabels = svg.selectAll(".edgelabel")
             .data(dataset.edges)
             .enter()
             .append('text')
             .style("pointer-events", "none")
             .attr({'class':'edgelabel',
                    'id':function(d,i){return 'edgelabel'+i; },
                    'dx':80,
                    'dy':0,
                    'font-size':10,
                    'fill':'#aaa'});

         edgelabels.append('textPath')
             .attr('xlink:href',function(d,i) {return '#edgepath'+i; })
             .style("pointer-events", "none")
             .text(function(d,i){return d.type;});


         svg.append('defs').append('marker')
             .attr({'id':'arrowhead',
                    'viewBox':'-0 -5 10 10',
                    'refX':25,
                    'refY':0,
                    //'markerUnits':'strokeWidth',
                    'orient':'auto',
                    'markerWidth':10,
                    'markerHeight':10,
                    'xoverflow':'visible'})
             .append('svg:path')
                 .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
                 .attr('fill', '#ccc')
                 .attr('stroke','#ccc');
           

         force.on("tick", function(){
              edges.attr({"x1": function(d){return d.source.x-k;},
                         "y1": function(d){return d.source.y-k;},
                         "x2": function(d){return d.target.x+k;},
                         "y2": function(d){return d.target.y+k;}
             }); 

            nodes.attr("transform", function(d) { 
                 if (d.type == 'Agent') 
                     return "translate(" + (d.x-230) + "," + (d.y-180) + ")";
                 else return "translate(" + (d.x-50) + "," + (d.y-410) + ")"; 
             });

             nodelabels.attr("x", function(d) { return d.x; }) 
                       .attr("y", function(d) { return d.y; });

              edgepaths.attr('d', function(d) { var path='M '+d.source.x+' '+d.source.y+' L '+ d.target.x +' '+d.target.y;
                                                //console.log(d)
                                                return path; });       

             edgelabels.attr('transform',function(d,i){
                 if (d.target.x<d.source.x){
                     bbox = this.getBBox();
                     rx = bbox.x+bbox.width/2;
                     ry = bbox.y+bbox.height/2;
                     return 'rotate(180 '+rx+' '+ry+')';
                     }
                 else {
                     return 'rotate(0)';
                     }
             });
          });

         function dragstart(d) {
        	  d3.select(this).classed("fixed", d.fixed = true);
         }
</script>
</body>
</html>