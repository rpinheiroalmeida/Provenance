package br.com.unb.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
}
