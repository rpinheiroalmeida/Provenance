package br.com.unb.transform;

import org.neo4j.graphdb.Node;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.labels.CollectionLabel;
import br.com.unb.model.CollectionProvenance;

@Component
public class CollectionProvenanceTransform<T extends CollectionProvenance> implements Transform<T>{

	@Override
	public Node transform2Node(CollectionProvenance collectionProvenance, Node node) {
		node.setProperty(CollectionLabel.NAME.getLabel(), collectionProvenance.getName());
		node.setProperty(CollectionLabel.TYPE.getLabel(), collectionProvenance.getType().getName());
		node.setProperty(CollectionLabel.LOCATION.getLabel(), collectionProvenance.getLocation());
		node.setProperty(CollectionLabel.SIZE.getLabel(), collectionProvenance.getSize());

		return node;
	}

	@Override
	public CollectionProvenance transform2Entity(Node node) {
		CollectionProvenance collectionProvenance = new CollectionProvenance();
		collectionProvenance.setId(node.getId());
		collectionProvenance.setName( (String) node.getProperty(CollectionLabel.NAME.getLabel()));
		collectionProvenance.setLocation((String) node.getProperty(CollectionLabel.LOCATION.getLabel()));
		collectionProvenance.setSize((Long) node.getProperty(CollectionLabel.SIZE.getLabel()));
		
		return collectionProvenance;
	}

}
