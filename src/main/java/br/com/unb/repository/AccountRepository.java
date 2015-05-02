package br.com.unb.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.labels.CollectionLabel;
import br.com.unb.model.Account;
import br.com.unb.model.EntityType;
import br.com.unb.model.Project;
import br.com.unb.model.RelationshipProvenanceType;
import br.com.unb.transform.AccountTransform;

@Component
public class AccountRepository {

	private static final String endl = System.getProperty("line.separator");
	
	@Inject private GraphDatabaseService graphDb;
	@Inject private AccountTransform<Account> accountTransform;

	public AccountRepository(GraphDatabaseService graphDb, AccountTransform<Account> transform) {
		this.graphDb = graphDb;
		this.accountTransform = transform;
	}

	public List<Account> list(Project project) {
		List<Account> accounts = new ArrayList<>();
		Node root = graphDb.getNodeById(project.getId());
		Iterable<Relationship> itRelHas= root.getRelationships(RelationshipProvenanceType.HAS);
		if (itRelHas != null) {
			for (Relationship relHas : itRelHas) {
				Node possibleNodeAccount = relHas.getEndNode();
				if (EntityType.ACCOUNT.getName().equals(possibleNodeAccount.getProperty("type"))) {
					accounts.add(accountTransform.transform2Entity(possibleNodeAccount));
				}
			}
		}
		return accounts;
	}

	public void save(Account account) {
		Node nodeProject = graphDb.getNodeById(account.getProject().getId());
		Node nodeAccount = accountTransform.transform2Node(account, graphDb.createNode());

		Relationship relationshipHas = nodeProject.createRelationshipTo(nodeAccount, RelationshipProvenanceType.HAS);
		relationshipHas.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
	}

	public Account find(long idAccount) {
		return accountTransform.transform2Entity(graphDb.getNodeById(idAccount));
	}

	public Account findAccountByActivity(long idActivity) {
		Node nodeAccount = graphDb.getNodeById(idActivity);
		Relationship relHas = nodeAccount.getSingleRelationship(RelationshipProvenanceType.HAS, Direction.INCOMING);
		return accountTransform.transform2Entity(relHas.getStartNode());
	}

	private Set<Long> getActivities(long idAccount) {
		Set<Long> activities = new TreeSet<>();
		Node root = graphDb.getNodeById(idAccount);
		Iterable<Relationship> allRelationships = root.getRelationships(Direction.OUTGOING);
		if (allRelationships != null) {
			for (Relationship relationship : allRelationships) {
				activities.add(relationship.getOtherNode(root).getId());
			}
		}
		
		return activities;
	}
	
	private String defineNodes(Set<Long> idNodes, String form) {
		StringBuilder sb = new StringBuilder();
		Node node;
		for (Long idNode : idNodes) {
			node = graphDb.getNodeById(idNode);
			sb.append("\"").
				append(node.getProperty("name")).
			append("\"").
			append(form).
			append(";").
			append(endl);
		}
		
		return sb.toString();
	}
	
	private boolean isCollection(Node node) {
		return node.hasProperty(CollectionLabel.TYPE.getLabel()) && EntityType.COLLECTION.getName().equals( node.getProperty(CollectionLabel.TYPE.getLabel()) );
	}
	
	private Set<Long> getCollections(Set<Long> idsActivities) {
		Set<Long> idsCollection = new TreeSet<>();
		
		Node nodeActivity;
		for (Long idActivity : idsActivities) {
			nodeActivity = graphDb.getNodeById(idActivity);
			Iterable<Relationship> allRelationships = nodeActivity.getRelationships();
			if (allRelationships != null) {
				for (Relationship relationship : allRelationships) {
					Node otherNode = relationship.getOtherNode(nodeActivity);
					
					if (!idsCollection.contains(otherNode.getId()) && isCollection(otherNode) ) 
					{
						idsCollection.add(otherNode.getId());
					}
				}
			}
		}
		return idsCollection;
	}
	
	private Set<Long> getAgents(Set<Long> idsActivities) {
		Set<Long> idsCollection = new TreeSet<>();
		
		Node nodeActivity;
		for (Long idActivity : idsActivities) {
			nodeActivity = graphDb.getNodeById(idActivity);
			Iterable<Relationship> relsWasAssociatedWith = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
			if (relsWasAssociatedWith != null) {
				for (Relationship relationship : relsWasAssociatedWith) {
					Node otherNode = relationship.getOtherNode(nodeActivity);
					if (!idsCollection.contains(otherNode.getId())) 
					{
						idsCollection.add(otherNode.getId());
					}
				}
			}
		}
		return idsCollection;
	}
	
	private String getStringRelationshipWasAssociatedWith(Node nodeActivity) {
		StringBuilder sb = new StringBuilder();
		
		Iterable<Relationship> relsWasAssociatedWith = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
		
		if (relsWasAssociatedWith != null)  {
			for (Relationship relWasAssociatedWith : relsWasAssociatedWith) {
				Node nodeAgent = relWasAssociatedWith.getOtherNode(nodeActivity);
				
				sb.append("\""+nodeActivity.getProperty("name")+"\"")
				 .append(" -> ")
				 .append("\""+nodeAgent.getProperty("name")+"\"")
				 .append("[style=dotted, label=wasAssociatedWith]")
				 .append(";")
				 .append(endl);
			}
		}

		return sb.toString();
	}
	
	private String getStringRelationshipWasGeneratedBy(Node nodeActivity) {
		StringBuilder sb = new StringBuilder();
		
		Iterable<Relationship> relsWasGeneratedBy = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_GENERATED_BY);
		
		if (relsWasGeneratedBy != null)  {
			for (Relationship relWasGeneratedBy : relsWasGeneratedBy) {
				Node nodeCollection = relWasGeneratedBy.getOtherNode(nodeActivity);
				
				sb.append("\"").append(nodeCollection.getProperty("name")).append("\"")
				  .append(" -> ")
				  .append("\""+nodeActivity.getProperty("name")+"\"")
				  .append("[label=wasGeneratedBy]").append(";").append(endl);
			}
		}

		return sb.toString();
	}
	
	private String getStringRelationshipWasUsed(Node nodeActivity) {
		StringBuilder sb = new StringBuilder();
		
		Iterable<Relationship> relsUsed = nodeActivity.getRelationships(RelationshipProvenanceType.USED);
		
		if (relsUsed != null)  {
			for (Relationship relUsed : relsUsed) {
				Node nodeCollection = relUsed.getOtherNode(nodeActivity);
				sb.append("\"").append(nodeActivity.getProperty("name")).append("\"")
				  .append(" -> ")
				  .append("\"").append(nodeCollection.getProperty("name")).append("\"")
				  .append("[style=dotted, label=used]")
				  .append(";")
				  .append(endl);
			}
		}
		return sb.toString();
	}
	
	private String getStringRelationshipWasDerivedFrom(Node nodeCollection) {
		StringBuilder sb = new StringBuilder();
		
		Iterable<Relationship> relsWasDerivedFrom = nodeCollection.getRelationships(Direction.INCOMING, RelationshipProvenanceType.WAS_DERIVED_FROM);
		
		if (relsWasDerivedFrom != null)  {
			for (Relationship relWasDerivedFrom : relsWasDerivedFrom) {
				Node nodeEnd = relWasDerivedFrom.getOtherNode(nodeCollection);
				
				sb.append("\"").append(nodeCollection.getProperty("name")).append("\"")
				  .append(" -> ")
				  .append("\"").append(nodeEnd.getProperty("name")).append("\"")
				  .append("[label=wasDerivedFrom]").
				  append(";").
				  append(endl);
				
//				sb.append("\"").append(nodeCollection.getProperty("name")).append("\"")
//				  .append(" -> ")
//				  .append("\"").append(nodeCollection.getProperty("name")).append("\"")
//				  .append("[style=dotted, label=used]")
//				  .append(";")
//				  .append(endl);
			}
		}
		
//		Iterable<Relationship> relsWasDerivedFrom = nodeUsed.getRelationships(RelationshipProvenanceType.WAS_DERIVED_FROM);
//		if (relsWasDerivedFrom != null)  {
//			for (Relationship relWasDerivedFrom : relsWasDerivedFrom) {
//				Node startCollection = relWasDerivedFrom.getStartNode();
//				Node endCollection = relWasDerivedFrom.getEndNode();
//				if (!idsUsados.contains(startCollection.getId())) {
//					graph.append("\""+startCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";").append(endl);		
//					idsUsados.add(Long.valueOf(startCollection.getId()));
//				}
//				if (!idsUsados.contains(endCollection.getId())) {
//					graph.append("\""+endCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";");
//					idsUsados.add(Long.valueOf(endCollection.getId()));
//				}
//				if (!idsUsados.contains(relWasDerivedFrom.getId())) {
//					graph.append("\""+startCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]")
//						 .append(" -> ")
//						 .append("\""+endCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]")
//						 .append("[label=wasDerivedFrom]").append(";").append(endl);
//					idsUsados.add(Long.valueOf(relWasDerivedFrom.getId()));
//				}
//			}
//		}
		
		return sb.toString();
	}
	
	public String buildGraph(long idAccount, long idGroup) {
		StringBuilder graph = new StringBuilder("digraph G {");
		graph.append("graph [").append(endl)
			 .append("fontsize = 36,").append(endl)
			 .append("rankdir=BT,").append(endl)
			 .append("size = 15];").append(endl)
			 .append("node [	shape = polygon,").append(endl)
			 .append("distortion = 0.0,").append(endl)
			 .append("orientation = 0.0,").append(endl)
			 .append("skew = 0.0,").append(endl)
			 .append("color = white,").append(endl)
			 .append("style = filled ];").append(endl);
		
		Set<Long> idsActivities = getActivities(idAccount);
		Set<Long> idsCollections = getCollections(idsActivities);
		Set<Long> idsAgents = getAgents(idsActivities);
		
		graph.append(defineNodes(idsActivities, "[shape=box, color=orange]"));
		graph.append(defineNodes(idsCollections, "[shape=ellipse, color=deepskyblue]"));
		graph.append(defineNodes(idsAgents, "[shape=house, color=gray]"));
		
		for (Long idActivity : idsActivities) {
			Node nodeActivity = graphDb.getNodeById(idActivity);
			graph.append(getStringRelationshipWasAssociatedWith(nodeActivity));
			graph.append(getStringRelationshipWasGeneratedBy(nodeActivity));
			graph.append(getStringRelationshipWasUsed(nodeActivity));
		}
		
		for (Long idCollection : idsCollections) {
			Node nodeCollection = graphDb.getNodeById(idCollection);
			graph.append(getStringRelationshipWasDerivedFrom(nodeCollection));
		}
//		Set<Long> idsUsados = new HashSet<>();
//
//		//Ugly. Improve
//		Node root = graphDb.getNodeById(idAccount);
//		Iterable<Relationship> allRelationships = root.getRelationships(Direction.OUTGOING);
//		if (allRelationships != null) {
//			for (Relationship relationship : allRelationships) {
//				Node nodeActivity = relationship.getOtherNode(root);
//				if (!idsUsados.contains(nodeActivity.getId())) {
//					graph.append("\""+nodeActivity.getProperty("name")+"\"").append("[shape=box, color=orange]").append(";").append(endl);//main [shape=box]; 
//					idsUsados.add(Long.valueOf(nodeActivity.getId()));
//				}
//				OK
//				Iterable<Relationship> allRelationshipUsed = nodeActivity.getRelationships(RelationshipProvenanceType.USED);
//				if (allRelationshipUsed != null) {
//					for (Relationship relationshipUsed : allRelationshipUsed) {
//						Node nodeUsed = relationshipUsed.getEndNode();
//						if (!idsUsados.contains(nodeUsed.getId())) {
//							graph.append("\""+nodeUsed.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";").append(endl);
//							idsUsados.add(Long.valueOf(nodeUsed.getId()));
//						}
//						if (!idsUsados.contains(relationshipUsed.getId())) {
//							graph.append("\""+nodeActivity.getProperty("name")+"\"").append("[shape=box, color=orange]").
//								append(" -> ").
//								append("\""+nodeUsed.getProperty("name")+"\"").append("[style=dotted, label=used]").
//								append(";").append(endl);
//							idsUsados.add(Long.valueOf(relationshipUsed.getId()));
//						}
//						
//						Iterable<Relationship> relsWasDerivedFrom = nodeUsed.getRelationships(RelationshipProvenanceType.WAS_DERIVED_FROM);
//						if (relsWasDerivedFrom != null)  {
//							for (Relationship relWasDerivedFrom : relsWasDerivedFrom) {
//								Node startCollection = relWasDerivedFrom.getStartNode();
//								Node endCollection = relWasDerivedFrom.getEndNode();
//								if (!idsUsados.contains(startCollection.getId())) {
//									graph.append("\""+startCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";").append(endl);		
//									idsUsados.add(Long.valueOf(startCollection.getId()));
//								}
//								if (!idsUsados.contains(endCollection.getId())) {
//									graph.append("\""+endCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";");
//									idsUsados.add(Long.valueOf(endCollection.getId()));
//								}
//								if (!idsUsados.contains(relWasDerivedFrom.getId())) {
//									graph.append("\""+startCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]")
//										 .append(" -> ")
//										 .append("\""+endCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]")
//										 .append("[label=wasDerivedFrom]").append(";").append(endl);
//									idsUsados.add(Long.valueOf(relWasDerivedFrom.getId()));
//								}
//							}
//						}
//					}
//				}
//				
//				OK
//				Iterable<Relationship> relsWasAssociatedWith = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
//				if (relsWasAssociatedWith != null)  {
//					for (Relationship relWasAssociatedWith : relsWasAssociatedWith) {
//						Node nodeAgent = relWasAssociatedWith.getOtherNode(nodeActivity);
//						if (!idsUsados.contains(nodeAgent.getId())) {
//							graph.append("\""+nodeAgent.getProperty("name")+"\"").append("[shape=house, color=gray]").append(";").append(endl);
//							idsUsados.add(Long.valueOf(nodeAgent.getId()));
//						}
//						if (!idsUsados.contains(relWasAssociatedWith.getId())) {
//							graph.append("\""+nodeActivity.getProperty("name")+"\"").append("[shape=box, color=orange]")
//								 .append(" -> ")
//								 .append("\""+nodeAgent.getProperty("name")+"\"").append("[shape=house, color=gray]")
//								 .append("[style=dotted, label=wasAssociatedWith]").append(";").append(endl);
//							idsUsados.add(Long.valueOf(relWasAssociatedWith.getId()));
//						}
//
//					}
//				}
			//OK
//				Iterable<Relationship> relsWasGeneratedBy = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_GENERATED_BY);
//				if (relsWasGeneratedBy != null)  {
//					for (Relationship relWasGeneratedBy : relsWasGeneratedBy) {
//						Node nodeCollection = relWasGeneratedBy.getOtherNode(nodeActivity);
//						if (!idsUsados.contains(nodeCollection.getId())) {
//							graph.append("\""+nodeCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";").append(endl);
//							idsUsados.add(Long.valueOf(nodeCollection.getId()));
//						}
//						if (!idsUsados.contains(relWasGeneratedBy.getId())) {
//							graph.append("\""+nodeCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]")
//							 .append(" -> ")
//							 .append("\""+nodeActivity.getProperty("name")+"\"").append("[shape=box, color=orange]")
//							 .append("[label=wasGeneratedBy]").append(";").append(endl);
//							idsUsados.add(Long.valueOf(relWasGeneratedBy.getId()));
//						}
//					}
//				}
//			}
//		}
		graph.append("}");
		return graph.toString();
	}

	
//	public String buildGraph(long idAccount, long idGroup) {
//		StringBuilder graph = new StringBuilder("digraph G {");
//		graph.append("graph [").append(endl)
//			 .append("fontsize = 36,").append(endl)
//			 .append("rankdir=BT,").append(endl)
//			 .append("size = 15];").append(endl)
//			 .append("node [	shape = polygon,").append(endl)
//			 .append("distortion = 0.0,").append(endl)
//			 .append("orientation = 0.0,").append(endl)
//			 .append("skew = 0.0,").append(endl)
//			 .append("color = white,").append(endl)
//			 .append("style = filled ];").append(endl);
//		
//		Set<Long> idsUsados = new HashSet<>();
//
//		//Ugly. Improve
//		Node root = graphDb.getNodeById(idAccount);
//		Iterable<Relationship> allRelationships = root.getRelationships(Direction.OUTGOING);
//		if (allRelationships != null) {
//			for (Relationship relationship : allRelationships) {
//				Node nodeActivity = relationship.getOtherNode(root);
//				if (!idsUsados.contains(nodeActivity.getId())) {
//					graph.append("\""+nodeActivity.getProperty("name")+"\"").append("[shape=box, color=orange]").append(";").append(endl);//main [shape=box]; 
//					idsUsados.add(Long.valueOf(nodeActivity.getId()));
//				}
//				
//				Iterable<Relationship> allRelationshipUsed = nodeActivity.getRelationships(RelationshipProvenanceType.USED);
//				if (allRelationshipUsed != null) {
//					for (Relationship relationshipUsed : allRelationshipUsed) {
//						Node nodeUsed = relationshipUsed.getEndNode();
//						if (!idsUsados.contains(nodeUsed.getId())) {
//							graph.append("\""+nodeUsed.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";").append(endl);
//							idsUsados.add(Long.valueOf(nodeUsed.getId()));
//						}
//						if (!idsUsados.contains(relationshipUsed.getId())) {
//							graph.append("\""+nodeActivity.getProperty("name")+"\"").append("[shape=box, color=orange]").
//								append(" -> ").
//								append("\""+nodeUsed.getProperty("name")+"\"").append("[style=dotted, label=used]").
//								append(";").append(endl);
//							idsUsados.add(Long.valueOf(relationshipUsed.getId()));
//						}
//						
//						Iterable<Relationship> relsWasDerivedFrom = nodeUsed.getRelationships(RelationshipProvenanceType.WAS_DERIVED_FROM);
//						if (relsWasDerivedFrom != null)  {
//							for (Relationship relWasDerivedFrom : relsWasDerivedFrom) {
//								Node startCollection = relWasDerivedFrom.getStartNode();
//								Node endCollection = relWasDerivedFrom.getEndNode();
//								if (!idsUsados.contains(startCollection.getId())) {
//									graph.append("\""+startCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";").append(endl);		
//									idsUsados.add(Long.valueOf(startCollection.getId()));
//								}
//								if (!idsUsados.contains(endCollection.getId())) {
//									graph.append("\""+endCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";");
//									idsUsados.add(Long.valueOf(endCollection.getId()));
//								}
//								if (!idsUsados.contains(relWasDerivedFrom.getId())) {
//									graph.append("\""+startCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]")
//										 .append(" -> ")
//										 .append("\""+endCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]")
//										 .append("[label=wasDerivedFrom]").append(";").append(endl);
//									idsUsados.add(Long.valueOf(relWasDerivedFrom.getId()));
//								}
//							}
//						}
//					}
//				}
//				
//				
//				Iterable<Relationship> relsWasAssociatedWith = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
//				if (relsWasAssociatedWith != null)  {
//					for (Relationship relWasAssociatedWith : relsWasAssociatedWith) {
//						Node nodeAgent = relWasAssociatedWith.getOtherNode(nodeActivity);
//						if (!idsUsados.contains(nodeAgent.getId())) {
//							graph.append("\""+nodeAgent.getProperty("name")+"\"").append("[shape=house, color=gray]").append(";").append(endl);
//							idsUsados.add(Long.valueOf(nodeAgent.getId()));
//						}
//						if (!idsUsados.contains(relWasAssociatedWith.getId())) {
//							graph.append("\""+nodeActivity.getProperty("name")+"\"").append("[shape=box, color=orange]")
//								 .append(" -> ")
//								 .append("\""+nodeAgent.getProperty("name")+"\"").append("[shape=house, color=gray]")
//								 .append("[style=dotted, label=wasAssociatedWith]").append(";").append(endl);
//							idsUsados.add(Long.valueOf(relWasAssociatedWith.getId()));
//						}
//
//					}
//				}
//				Iterable<Relationship> relsWasGeneratedBy = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_GENERATED_BY);
//				if (relsWasGeneratedBy != null)  {
//					for (Relationship relWasGeneratedBy : relsWasGeneratedBy) {
//						Node nodeCollection = relWasGeneratedBy.getOtherNode(nodeActivity);
//						if (!idsUsados.contains(nodeCollection.getId())) {
//							graph.append("\""+nodeCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";").append(endl);
//							idsUsados.add(Long.valueOf(nodeCollection.getId()));
//						}
//						if (!idsUsados.contains(relWasGeneratedBy.getId())) {
//							graph.append("\""+nodeCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]")
//							 .append(" -> ")
//							 .append("\""+nodeActivity.getProperty("name")+"\"").append("[shape=box, color=orange]")
//							 .append("[label=wasGeneratedBy]").append(";").append(endl);
//							idsUsados.add(Long.valueOf(relWasGeneratedBy.getId()));
//						}
//					}
//				}
//			}
//		}
//		graph.append("}");
//		return graph.toString();
//	}


}
