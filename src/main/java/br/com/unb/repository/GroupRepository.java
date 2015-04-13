package br.com.unb.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.EntityType;
import br.com.unb.model.Group;
import br.com.unb.model.RelationshipProvenanceType;
import br.com.unb.transform.GroupTransform;

@Component
public class GroupRepository {

	@Inject private GraphDatabaseService graphDb;
	@Inject private GroupTransform<Group> groupTransform;
	
	public GroupRepository(GraphDatabaseService graphDb, GroupTransform<Group> transform) {
		this.graphDb = graphDb;
		this.groupTransform = transform;
	}

	public void save(Group group) {
		Node nodeAccount = graphDb.getNodeById(group.getAccount().getId());
		Node nodeGroup = groupTransform.transform2Node(group, graphDb.createNode());
		
		Relationship relationshipHas = nodeAccount.createRelationshipTo(nodeGroup, RelationshipProvenanceType.HAS_GROUP);
		relationshipHas.setProperty("relationship-type", RelationshipProvenanceType.HAS_GROUP.getName());;
	}

	public List<Group> listByAccount(long idAccount) {
		List<Group> groups = new ArrayList<>();
		Node root = graphDb.getNodeById(idAccount);
		Iterable<Relationship> itRelHas= root.getRelationships(RelationshipProvenanceType.HAS_GROUP, Direction.OUTGOING);
		if (itRelHas != null) {
			for (Relationship relHas : itRelHas) {
				Node possibleNodeGroup = relHas.getOtherNode(root);
				System.out.println(possibleNodeGroup.getId() + " - " + possibleNodeGroup.getProperty("type"));
				if (EntityType.GROUP.getName().equals(possibleNodeGroup.getProperty("type"))) {
					groups.add(groupTransform.transform2Entity(possibleNodeGroup));
				}
			}
		}
		return groups;
	}

	public Group findByActivity(long idActivity) {
		Node nodeActivity = graphDb.getNodeById(idActivity);
		Relationship relHasGroup= nodeActivity.getSingleRelationship(RelationshipProvenanceType.HAS_GROUP, Direction.OUTGOING);
		
		return relHasGroup != null ? groupTransform.transform2Entity(relHasGroup.getEndNode()) : null;
	}
	
		
}
