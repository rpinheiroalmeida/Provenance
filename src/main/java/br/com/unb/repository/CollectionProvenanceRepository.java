package br.com.unb.repository;

import java.util.Iterator;

import javax.inject.Inject;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.labels.CollectionLabel;
import br.com.unb.model.Activity;
import br.com.unb.model.CollectionProvenance;
import br.com.unb.model.RelationshipProvenanceType;
import br.com.unb.transform.CollectionProvenanceTransform;

@Component
public class CollectionProvenanceRepository {

	@Inject private GraphDatabaseService graphDb;
	@Inject private CollectionProvenanceTransform<CollectionProvenance> collectionTransform;
	
	public CollectionProvenanceRepository(GraphDatabaseService graphDb, CollectionProvenanceTransform<CollectionProvenance> transform) {
		this.graphDb = graphDb;
		this.collectionTransform = transform;
	}

	public Long save(CollectionProvenance collectionProvenance) {
		Node nodeCollection = collectionTransform.transform2Node(collectionProvenance, graphDb.createNode());
		collectionProvenance.setId(nodeCollection.getId());
		
		return nodeCollection.getId();
	}
	
	public void update(CollectionProvenance collection) {
		String queryTemplate = "MATCH (n { id: '%d' }) SET n.%s = '%s', n.%s = '%s', n.%s = %d RETURN n";
		String query = String.format(queryTemplate, collection.getId(), 
				CollectionLabel.NAME.getLabel(), collection.getName(),
				CollectionLabel.LOCATION.getLabel(), collection.getLocation(),
				CollectionLabel.SIZE.getLabel(), collection.getSize());
		
		ExecutionEngine engine = new ExecutionEngine(this.graphDb);
		engine.execute(query);
	}
	
	public CollectionProvenance findByName(String fileName) {
		ExecutionEngine engine = new ExecutionEngine(this.graphDb);
		ExecutionResult result = engine.execute("MATCH (collection {name: "+ "'" + fileName + "'}) RETURN collection");
		
		Iterator<Node> n_column = result.columnAs( "collection" );
		for ( Node node : IteratorUtil.asIterable( n_column ) ) {
			return collectionTransform.transform2Entity(node);
		}
		return null;
	}
	
	public Node findByNameAndActivity(String fileName, Activity activity) {
		
			Node nodeActivity = this.graphDb.getNodeById(activity.getId());
			Iterable<Relationship> allRelationshipUsed = nodeActivity.getRelationships(RelationshipProvenanceType.USED);
			if (allRelationshipUsed != null) {
				for (Relationship relationshipUsed : allRelationshipUsed) {
					Node nodeUsed = relationshipUsed.getEndNode();
					String nameCollection = (String) nodeUsed.getProperty(CollectionLabel.NAME.getLabel()); 
					if (fileName.equals(nameCollection)) {
						return nodeUsed;
					}
				}
			}
			
			Iterable<Relationship> allRelationshipGenerated = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_GENERATED_BY);
			if (allRelationshipUsed != null) {
				for (Relationship relationshipUsed : allRelationshipGenerated) {
					Node nodeGeneratedBy = relationshipUsed.getEndNode();
					String nameCollection = (String) nodeGeneratedBy.getProperty(CollectionLabel.NAME.getLabel()); 
					if (fileName.equals(nameCollection)) {
						return nodeGeneratedBy;
					}
				}
			}
			
			Iterable<Relationship> allRelationshipDerivedFrom = nodeActivity.getRelationships(RelationshipProvenanceType.WAS_DERIVED_FROM);
			if (allRelationshipUsed != null) {
				for (Relationship relationshipUsed : allRelationshipDerivedFrom) {
					Node nodeDerivedFrom = relationshipUsed.getEndNode();
					String nameCollection = (String) nodeDerivedFrom.getProperty(CollectionLabel.NAME.getLabel()); 
					if (fileName.equals(nameCollection)) {
						return nodeDerivedFrom;
					}
				}
			}
			
			return null;
	}

	public void update(String user, Activity activity, CollectionProvenance collection) {
		try ( Transaction tx = graphDb.beginTx() ) {
			Node nodeCollection = findByNameAndActivity(collection.getName(), activity);
			if (nodeCollection == null) {
				saveRelationshipGeneratedBy(activity, collection);
			} else {
				collection.setId(nodeCollection.getId());
				update(collection);
			}
			tx.success();
		}
	}
	
	public void saveRelationshipGeneratedBy(Activity activity, CollectionProvenance collection) {
		Node nodeActivity = graphDb.getNodeById(activity.getId());
		
		Node nodeCollection = collectionTransform.transform2Node(collection, graphDb.createNode());
		
		Relationship relationshipUsed = nodeActivity.createRelationshipTo(nodeCollection, RelationshipProvenanceType.WAS_GENERATED_BY);
		relationshipUsed.setProperty("relationship-type", RelationshipProvenanceType.WAS_GENERATED_BY.getName());
	}
	
}
