package br.com.unb.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Account;
import br.com.unb.model.Activity;
import br.com.unb.model.EntityType;
import br.com.unb.model.RelationshipProvenanceType;
import br.com.unb.transform.ActivityTransform;

@Component
public class ActivityRepository {

	@Inject private GraphDatabaseService graphDb;
	@Inject private ActivityTransform<Activity> activityTransform;
	
	public ActivityRepository(GraphDatabaseService graphDb, ActivityTransform<Activity> transform) {
		this.graphDb = graphDb;
		this.activityTransform = transform;
	}
	
//	public List<Project> listProjects(User user) {
////		ExecutionEngine engine = new ExecutionEngine(graphDb);
////		ExecutionResult result;
////		try(Transaction tx = managerConnection.beginTx())  {
////			result = engine.execute(createQueryListar());
////			return montaLista(result);
////		}
//		List<Project> projects = new ArrayList<>();
//		Node root = graphDb.getNodeById(user.getId());
//		Iterable<Relationship> itRelHas= root.getRelationships(RelationshipProvenanceType.HAS);
//		if (itRelHas != null) {
//			for (Relationship relHas : itRelHas) {
//				Node possibleNodeProject = relHas.getEndNode();
//				if (EntityType.PROJECT.getName().equals(possibleNodeProject.getProperty("type"))) {
//					projects.add(projectTransform.transform2Entity(possibleNodeProject));
//				}
//			}
//		}
//		
//		return projects;
//	}
	
//	public List<Account> listAccounts(Project project) {
//		List<Account> accounts = new ArrayList<>();
//		Node root = graphDb.getNodeById(project.getId());
//		Iterable<Relationship> itRelHas= root.getRelationships(RelationshipProvenanceType.HAS);
//		if (itRelHas != null) {
//			for (Relationship relHas : itRelHas) {
//				Node possibleNodeAccount = relHas.getEndNode();
//				if (EntityType.ACCOUNT.getName().equals(possibleNodeAccount.getProperty("type"))) {
//					accounts.add(activityTransform.transform2Entity(possibleNodeAccount));
//				}
//			}
//		}
//		return accounts;
//	}
	
	public List<Activity> list(Account account) {
		List<Activity> activities = new ArrayList<>();
		Node root = graphDb.getNodeById(account.getId());
		Iterable<Relationship> itRelHas= root.getRelationships(RelationshipProvenanceType.HAS);
		if (itRelHas != null) {
			for (Relationship relHas : itRelHas) {
				Node possibleNodeActivity = relHas.getEndNode();
				if (EntityType.ACTIVITY.getName().equals(possibleNodeActivity.getProperty("type"))) {
					activities.add(activityTransform.transform2Entity(possibleNodeActivity));
				}
			}
		}
		return activities;
	}
	
	public void save(Activity activity) {
		Node nodeAccount = graphDb.getNodeById(activity.getAccount().getId());
		Node nodeActivity = activityTransform.transform2Node(activity, graphDb.createNode());
		Node nodeGroup = graphDb.getNodeById(activity.getGroup().getId());
		
		Relationship relationshipHas = nodeAccount.createRelationshipTo(nodeActivity, RelationshipProvenanceType.HAS);
		relationshipHas.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());
		
		Relationship relationshipHasGroup = nodeActivity.createRelationshipTo(nodeGroup, RelationshipProvenanceType.HAS_GROUP);
		relationshipHasGroup.setProperty("relationship-type", RelationshipProvenanceType.HAS_GROUP.getName());
	}

	public Activity find(long idActivity) {
		return activityTransform.transform2Entity(graphDb.getNodeById(idActivity));
	}
}
