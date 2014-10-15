package br.com.unb.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.EntityType;
import br.com.unb.model.Project;
import br.com.unb.model.RelationshipProvenanceType;
import br.com.unb.model.User;
import br.com.unb.transform.ProjectTransform;

@Component
public class ProjectRepository {

	@Inject private GraphDatabaseService graphDb;
	@Inject private ProjectTransform<Project> projectTransform;
	
	public ProjectRepository(GraphDatabaseService graphDb, ProjectTransform<Project> transform) {
		this.graphDb = graphDb;
		this.projectTransform = transform;
	}
	
	public List<Project> list(User user) {
		List<Project> projects = new ArrayList<>();
		Node root = graphDb.getNodeById(user.getId());
		Iterable<Relationship> itRelHas= root.getRelationships(RelationshipProvenanceType.HAS);
		if (itRelHas != null) {
			for (Relationship relHas : itRelHas) {
				Node possibleNodeProject = relHas.getEndNode();
				if (EntityType.PROJECT.getName().equals(possibleNodeProject.getProperty("type"))) {
					projects.add(projectTransform.transform2Entity(possibleNodeProject));
				}
			}
		}
		
		return projects;
	}
	
	public void save(Project project) {
		Node nodeUser = graphDb.getNodeById(project.getUsuario().getId());
		Node nodeProject = projectTransform.transform2Node(project, graphDb.createNode());
		
		Relationship relationshipHas = nodeUser.createRelationshipTo(nodeProject, RelationshipProvenanceType.HAS);
		relationshipHas.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
	}
}
