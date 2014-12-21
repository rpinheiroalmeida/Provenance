package br.com.unb.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Account;
import br.com.unb.model.Activity;
import br.com.unb.model.CollectionProvenance;
import br.com.unb.model.EntityType;
import br.com.unb.model.Project;
import br.com.unb.model.RelationshipProvenanceType;
import br.com.unb.model.User;
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

	public String buildJsonAccount(Long idExperimento) {
		StringBuilder jsonNodes = new StringBuilder("\"nodes\" : [");
		StringBuilder jsonLinks = new StringBuilder("\"edges\" : [");
		Set<Long> idsUsados = new HashSet<>();

		//		Transaction tx = null;
		//		try  {
		//			tx = graphDb.beginTx();

		Node root = graphDb.getNodeById(idExperimento);
		Iterable<Relationship> allRelationships = root.getRelationships(Direction.OUTGOING);
		if (allRelationships != null) {
			for (Relationship relationship : allRelationships) {
				Node nodeActivity = relationship.getOtherNode(root);
				if (!idsUsados.contains(nodeActivity.getId())) {
					jsonNodes.append(Activity.buildJson(nodeActivity)).append(",");
					idsUsados.add(nodeActivity.getId());
				}
				Iterable<Relationship> allRelationshipUsed = nodeActivity.getRelationships(RelationshipProvenanceType.USED);
				if (allRelationshipUsed != null) {
					for (Relationship relationshipUsed : allRelationshipUsed) {
						Node collectionUsed = relationshipUsed.getEndNode();
						if (!idsUsados.contains(collectionUsed.getId())) {
							jsonNodes.append(CollectionProvenance.buildJson(collectionUsed)).append(",");
							idsUsados.add(collectionUsed.getId());
						}
						if (!idsUsados.contains(relationshipUsed.getId())) {
							jsonLinks.append("{source : ").append(nodeActivity.getId()).append(", target : ")
							.append(collectionUsed.getId()).append(" ,type: '").append(relationshipUsed.getProperty("relationship-type"))
							.append("'}").append(",");
						}

						Iterable<Relationship> relsWasDerivedFrom = collectionUsed.getRelationships(RelationshipProvenanceType.WAS_DERIVED_FROM);
						if (relsWasDerivedFrom != null)  {
							for (Relationship relWasDerivedFrom : relsWasDerivedFrom) {
								Node startNode = relWasDerivedFrom.getStartNode();
								Node endNode = relWasDerivedFrom.getEndNode();
								if (!idsUsados.contains(startNode.getId())) {
									jsonNodes.append(CollectionProvenance.buildJson(startNode)).append(",");
									idsUsados.add(startNode.getId());
								}
								if (!idsUsados.contains(endNode.getId())) {
									jsonNodes.append(CollectionProvenance.buildJson(endNode)).append(",");
									idsUsados.add(endNode.getId());
								}
								if (!idsUsados.contains(relWasDerivedFrom.getId())) {
									jsonLinks.append("{source : ").append(startNode.getId()).append(", target : ")
									.append(endNode.getId()).append(" ,type: '").append(relWasDerivedFrom.getProperty("relationship-type"))
									.append("'}").append(",");
								}
							}
						}

					}
				}
				Iterable<Relationship> relsWasAssociatedWith = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
				if (relsWasAssociatedWith != null)  {
					for (Relationship relWasAssociatedWith : relsWasAssociatedWith) {
						Node nodeAgent = relWasAssociatedWith.getOtherNode(nodeActivity);
						if (!idsUsados.contains(nodeAgent.getId())) {
							jsonNodes.append(User.buildJson(nodeAgent)).append(",");
							idsUsados.add(nodeAgent.getId());
						}
						if (!idsUsados.contains(relWasAssociatedWith.getId())) {
							jsonLinks.append("{source : ").append(nodeActivity.getId()).append(", target : ")
							.append(nodeAgent.getId()).append(" ,type: '").append(relWasAssociatedWith.getProperty("relationship-type"))
							.append("'}").append(",");
						}

					}
				}
				Iterable<Relationship> relsWasGeneratedBy = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_GENERATED_BY);
				if (relsWasGeneratedBy != null)  {
					for (Relationship relWasGeneratedBy : relsWasGeneratedBy) {
						Node nodeCollection = relWasGeneratedBy.getOtherNode(nodeActivity);
						if (!idsUsados.contains(nodeCollection.getId())) {
							jsonNodes.append(CollectionProvenance.buildJson(nodeCollection)).append(",");
							idsUsados.add(nodeCollection.getId());
						}
						if (!idsUsados.contains(relWasGeneratedBy.getId())) {
							jsonLinks.append("{source : ").append(nodeCollection.getId()).append(", target : ")
							.append(nodeActivity.getId()).append(" ,type: '").append(relWasGeneratedBy.getProperty("relationship-type"))
							.append("'}").append(",");
						}
					}
				}
			}
		}
		//		} catch (Exception e) {
		//			throw e;
		//		}
		//		finally {
		//			if (tx != null) {
		//				tx.close();
		//			}
		//		}
		jsonNodes.deleteCharAt(jsonNodes.length()-1).append("]");
		jsonLinks.deleteCharAt(jsonLinks.length()-1).append("]");
		return "{" +  jsonNodes.toString() + " , " + jsonLinks.toString() + "}";
	}

	public String buildGraph(long idAccount, long idGroup) {
		StringBuilder graph = new StringBuilder("digraph G {");
		graph.append("graph [	fontname = 'Helvetica-Oblique',").append(endl)
			 .append("fontsize = 36,").append(endl)
			 .append("label = Provenance").append(endl)
			 .append("size = '6,6'];").append(endl)
			 .append("node [	shape = polygon,").append(endl)
			 .append("distortion = '0.0,'").append(endl)
			 .append("orientation = '0.0',").append(endl)
			 .append("skew = '0.0',").append(endl)
			 .append("color = white,").append(endl)
			 .append("style = filled,").append(endl)
			 .append("fontname = 'Helvetica-Outline' ];");
		
		Set<Long> idsUsados = new HashSet<>();

		//Ugly. Improve
		Node root = graphDb.getNodeById(idAccount);
		Iterable<Relationship> allRelationships = root.getRelationships(Direction.OUTGOING);
		if (allRelationships != null) {
			for (Relationship relationship : allRelationships) {
				Node nodeActivity = relationship.getOtherNode(root);
				if (!idsUsados.contains(nodeActivity.getId())) {
					graph.append(nodeActivity.getProperty("name")).append("[shape=box, color=orange]");//main [shape=box]; 
					idsUsados.add(nodeActivity.getId());
				}
				
				Iterable<Relationship> allRelationshipUsed = nodeActivity.getRelationships(RelationshipProvenanceType.USED);
				if (allRelationshipUsed != null) {
					for (Relationship relationshipUsed : allRelationshipUsed) {
						Node nodeUsed = relationshipUsed.getEndNode();
						if (!idsUsados.contains(nodeUsed.getId())) {
							graph.append(nodeUsed.getProperty("name")).append("[color=deepskyblue]");
							idsUsados.add(nodeUsed.getId());
						}
						if (!idsUsados.contains(relationshipUsed.getId())) {
							graph.append(nodeActivity.getProperty("name")).append("->").append(nodeUsed.getProperty("name")).append("[label=used]");
						}
						
						Iterable<Relationship> relsWasDerivedFrom = nodeUsed.getRelationships(RelationshipProvenanceType.WAS_DERIVED_FROM);
						if (relsWasDerivedFrom != null)  {
							for (Relationship relWasDerivedFrom : relsWasDerivedFrom) {
								Node startCollection = relWasDerivedFrom.getStartNode();
								Node endCollection = relWasDerivedFrom.getEndNode();
								if (!idsUsados.contains(startCollection.getId())) {
									graph.append(startCollection.getProperty("name")).append("[color=deepskyblue]");		
									idsUsados.add(startCollection.getId());
								}
								if (!idsUsados.contains(endCollection.getId())) {
									graph.append(startCollection.getProperty("name")).append("[color=deepskyblue]");
									idsUsados.add(endCollection.getId());
								}
								if (!idsUsados.contains(relWasDerivedFrom.getId())) {
									graph.append(startCollection.getProperty("name"))
										 .append("->")
										 .append(endCollection.getProperty("name"))
										 .append("[label=wasDerivedFrom]");
								}
							}
						}
					}
				}
				
				
				Iterable<Relationship> relsWasAssociatedWith = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
				if (relsWasAssociatedWith != null)  {
					for (Relationship relWasAssociatedWith : relsWasAssociatedWith) {
						Node nodeAgent = relWasAssociatedWith.getOtherNode(nodeActivity);
						if (!idsUsados.contains(nodeAgent.getId())) {
							graph.append(nodeAgent.getProperty("name")).append("[shape=house, color=gray]");
							idsUsados.add(nodeAgent.getId());
						}
						if (!idsUsados.contains(relWasAssociatedWith.getId())) {
							graph.append(nodeActivity.getProperty("name"))
								 .append("->")
								 .append(nodeAgent.getProperty("name"))
								 .append("[label=wasAssociatedWith]");
						}

					}
				}
				Iterable<Relationship> relsWasGeneratedBy = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_GENERATED_BY);
				if (relsWasGeneratedBy != null)  {
					for (Relationship relWasGeneratedBy : relsWasGeneratedBy) {
						Node nodeCollection = relWasGeneratedBy.getOtherNode(nodeActivity);
						if (!idsUsados.contains(nodeCollection.getId())) {
							graph.append(nodeCollection.getProperty("name")).append("[color=deepskyblue]");
							idsUsados.add(nodeCollection.getId());
						}
						if (!idsUsados.contains(relWasGeneratedBy.getId())) {
							graph.append(nodeActivity.getProperty("name")) 
							 .append("<-")
							 .append(nodeCollection.getProperty("name"))
							 .append("[label=wasGeneratedBy]");
						}
					}
				}
			}
		}
		return graph.toString();

	}


}
