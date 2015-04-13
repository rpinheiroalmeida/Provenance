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
		
		Set<Long> idsUsados = new HashSet<>();

		//Ugly. Improve
		Node root = graphDb.getNodeById(idAccount);
		Iterable<Relationship> allRelationships = root.getRelationships(Direction.OUTGOING);
		if (allRelationships != null) {
			for (Relationship relationship : allRelationships) {
				Node nodeActivity = relationship.getOtherNode(root);
				if (!idsUsados.contains(nodeActivity.getId())) {
					graph.append("\""+nodeActivity.getProperty("name")+"\"").append("[shape=box, color=orange]").append(";").append(endl);//main [shape=box]; 
					idsUsados.add(Long.valueOf(nodeActivity.getId()));
				}
				
				Iterable<Relationship> allRelationshipUsed = nodeActivity.getRelationships(RelationshipProvenanceType.USED);
				if (allRelationshipUsed != null) {
					for (Relationship relationshipUsed : allRelationshipUsed) {
						Node nodeUsed = relationshipUsed.getEndNode();
						if (!idsUsados.contains(nodeUsed.getId())) {
							graph.append("\""+nodeUsed.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";").append(endl);
							idsUsados.add(Long.valueOf(nodeUsed.getId()));
						}
						if (!idsUsados.contains(relationshipUsed.getId())) {
							graph.append("\""+nodeActivity.getProperty("name")+"\"").
								append(" -> ").
								append("\""+nodeUsed.getProperty("name")+"\"").append("[style=dotted, label=used]").
								append(";").append(endl);
							idsUsados.add(Long.valueOf(relationshipUsed.getId()));
						}
						
						Iterable<Relationship> relsWasDerivedFrom = nodeUsed.getRelationships(RelationshipProvenanceType.WAS_DERIVED_FROM);
						if (relsWasDerivedFrom != null)  {
							for (Relationship relWasDerivedFrom : relsWasDerivedFrom) {
								Node startCollection = relWasDerivedFrom.getStartNode();
								Node endCollection = relWasDerivedFrom.getEndNode();
								if (!idsUsados.contains(startCollection.getId())) {
									graph.append("\""+startCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";").append(endl);		
									idsUsados.add(Long.valueOf(startCollection.getId()));
								}
								if (!idsUsados.contains(endCollection.getId())) {
									graph.append("\""+endCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";");
									idsUsados.add(Long.valueOf(endCollection.getId()));
								}
								if (!idsUsados.contains(relWasDerivedFrom.getId())) {
									graph.append("\""+startCollection.getProperty("name")+"\"")
										 .append(" -> ")
										 .append("\""+endCollection.getProperty("name")+"\"")
										 .append("[label=wasDerivedFrom]").append(";").append(endl);
									idsUsados.add(Long.valueOf(relWasDerivedFrom.getId()));
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
							graph.append("\""+nodeAgent.getProperty("name")+"\"").append("[shape=house, color=gray]").append(";").append(endl);
							idsUsados.add(Long.valueOf(nodeAgent.getId()));
						}
						if (!idsUsados.contains(relWasAssociatedWith.getId())) {
							graph.append("\""+nodeActivity.getProperty("name")+"\"")
								 .append(" -> ")
								 .append("\""+nodeAgent.getProperty("name")+"\"")
								 .append("[style=dotted, label=wasAssociatedWith]").append(";").append(endl);
							idsUsados.add(Long.valueOf(relWasAssociatedWith.getId()));
						}

					}
				}
				Iterable<Relationship> relsWasGeneratedBy = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_GENERATED_BY);
				if (relsWasGeneratedBy != null)  {
					for (Relationship relWasGeneratedBy : relsWasGeneratedBy) {
						Node nodeCollection = relWasGeneratedBy.getOtherNode(nodeActivity);
						if (!idsUsados.contains(nodeCollection.getId())) {
							graph.append("\""+nodeCollection.getProperty("name")+"\"").append("[shape=ellipse, color=deepskyblue]").append(";").append(endl);
							idsUsados.add(Long.valueOf(nodeCollection.getId()));
						}
						if (!idsUsados.contains(relWasGeneratedBy.getId())) {
							graph.append("\""+nodeCollection.getProperty("name")+"\"") 
							 .append(" -> ")
							 .append("\""+nodeActivity.getProperty("name")+"\"")
							 .append("[label=wasGeneratedBy]").append(";").append(endl);
							idsUsados.add(Long.valueOf(relWasGeneratedBy.getId()));
						}
					}
				}
			}
		}
		graph.append("}");
		return graph.toString();
	}


}
