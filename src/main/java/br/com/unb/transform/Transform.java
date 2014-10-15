package br.com.unb.transform;

import org.neo4j.graphdb.Node;

import br.com.unb.model.EntityProvenance;

public interface Transform<T extends EntityProvenance> {

	Node transform2Node(T entity, Node node);
	
	EntityProvenance transform2Entity(Node node);
}
