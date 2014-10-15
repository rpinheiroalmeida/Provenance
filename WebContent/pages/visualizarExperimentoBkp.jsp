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

<style type="text/css">
            div.bar {
                display: inline-block;
                width: 20px;
                height: 75px;
                background-color: teal;
                margin-right: 2px;
            }
            .node text {
                pointer-events: none;
                font: 12px sans-serif;
            }
            .link text {
                pointer-events: none;
                font: 12px sans-serif;
            }
            line.link {
    			stroke-width: 2px;
    			stroke: #999;
    			stroke-opacity: 0.6;
			}

			marker#arrow {
    			stroke: #999;
    			fill: #999;
			}
            .link {
                fill: none;
                stroke: #000;
                stroke-width: 4px;
                cursor: default;
            }
</style>
            	
<body>

<svg width="640" height="480" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
	<defs>
		<mask id="maskAgent" x="0" y="0" width="100" height="100" >
        	<path 
            	id="svg_1" d="m199.99998,168.15999l31.09076,0l25.90926,28.57805l-25.90926,28.57823l-31.09076,0l0,-57.15628z" stroke-width="2" stroke="#000000" fill="#ffaa56"/>
		</mask>
        <mask id="maskCollection" x="0" y="0" width="100" height="100" >
        	<circle cx="25" cy="25" r="25" style="stroke:none; fill: #ffffff"/>
         </mask>
         <marker id="arrow" viewbox="0 -5 10 10" refX="18" refY="0" markerWidth="6" markerHeight="6" orient="auto">
             <path d="M0,-5L10,0L0,5Z"/>
         </marker>
         <mask id="maskActivity" x="0" y="0" width="100" height="100" >
        	<rect width="50" height="50" style="stroke:none; fill: #ffffff"/>
         </mask>
	</defs>
</svg>
<script type="text/javascript">

           var width = 1500, height = 1500;
            
           
           var data = '<c:out value="${data}"/>';
           data = data.replace(/&#039;/g, '\'');
           data = data.replace(/&#034;/g, '\"');
           data = eval("(" + data + ")");
           /* alert(data); */
           /* var data = {nodes : [{id:347827, name: 'A11', type:'Activity' },{id:347828, name: 'http.conf.old', type:'Collection' },{id:242670, name: 'adm', type:'Agent' },{id:339740, name: 'A10', type:'Activity' },{id:339741, name: 'blogPostDAO.pyc', type:'Collection' }] , links : [{source : 347827, target : 347828},{source : 347827, target : 242670},{source : 339740, target : 339741},{source : 339740, target : 242670}]}; */ 

            // make links reference nodes directly for this particular data format:
    	    var hash_lookup = [];
        	 // make it so we can lookup nodes in O(1):
         	data.nodes.forEach(function(d, i) {
           		hash_lookup[d.id] = d;
         	});
         	data.links.forEach(function(d, i) {
           		d.source = hash_lookup[d.source];
           		d.target = hash_lookup[d.target];
         	});
         	// and now we have Links[] and Nodes[] ready for use by D3::force...

         /* force.nodes(json.Nodes);
         force.links(json.Links); */

         
            var svg = d3.select("body")
                        .append("svg")
                        .attr("width", width)
                        .attr("height", height);

            var force = d3.layout.force()
                            .gravity(.05)
                            .distance(150)
                            .charge(-200)
                            .size([width, height])
                            .nodes(data.nodes)
                            .links(data.links)
                            .start();
            
            var node = svg.selectAll(".node")
                            .data(data.nodes)
                            .enter()
                            .append("g")
                            .attr("class", "node")
                            .call(force.drag);

            node.append("g")
                .append("rect")
                .attr("width", 300)
                .attr("height", 300)
                .attr("mask", function(d) {
                    if (d.type == 'Agent') return "url(#maskAgent)";
                    else if (d.type == 'Collection') return "url(#maskCollection)";
                    else return "url(#maskActivity)";
                })
                .attr("fill", "#f4ad6b")
                .attr("stroke-width", 2)
                .attr("d", "m199.99998,168.15999l31.09076,0l25.90926,28.57805l-25.90926,28.57823l-31.09076,0l0,-57.15628z")
                .attr("transform", "rotate(-90.11238861083984 228.5,196.73812866210938)")
                .attr("class", "node");

           node.append("text")
           	   .attr("x", function(d) { 
               	   if (d.type == 'Agent') return 250;
               	   else return -30; 
                })
           	   .attr("y", function(d) {
					if (d.type == 'Agent') return 200;
					else return 500;
               	})
           	   .attr("transform", "matrix(0.6192358409656578,0,0,0.6666666716337204,76.26317006991846,62.666665732860565)")
           	   .attr("stroke", "#000000")
           	   .attr("xml:space", "preserve")
           	   .attr("text-anchor", "middle")
               .attr("font-family", "serif")
               .attr("font-size", 30)
               .attr("stroke-width", 0)
               .attr("fill", "#000000")
           	   .text(function(d) {return d.name; }); 

            var link = svg.selectAll(".link") 
                            .data(data.links)
                            .enter()
                            .append("line")
                            .attr("class", "link")
        					.attr("marker-end", "url(#arrow)");

            link.append("text")
                  .attr("x", function(d) { return (d.source.y + d.target.y) / 2; }) 
    			  .attr("y", function(d) { return (d.source.x + d.target.x) / 2; }) 
                  .attr("text-anchor", "middle")
                  .text(function(d) {
                      return "Used";
                  });

            force.on("tick", function() {
                link.attr("x1", function(d) { return d.source.x; })
                    .attr("y1", function(d) { return d.source.y; }) 
                    .attr("x2", function(d) { return d.target.x; }) 
                    .attr("y2", function(d) { return d.target.y; });
                
                node.attr("transform", function(d) { 
                    if (d.type == 'Agent') 
                        return "translate(" + (d.x-230) + "," + (d.y-180) + ")";
                    else return "translate(" + (d.x-50) + "," + (d.y-410) + ")"; 
                });

            });
        </script>

</body>

</html>