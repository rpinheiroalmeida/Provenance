package br.com.unb.transform;

import org.neo4j.graphdb.Node;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Account;
import br.com.unb.model.Group;

@Component
public class GroupTransform<T extends Group> implements Transform<T>{

	enum Label {
		NAME("name"),
		TYPE("type");
		
		private String label;
		
		Label(String label) 
		{
			this.label = label;
		}
		
		String getLabel() {
			return this.label;
		}
	}
	
	@Override
	public Node transform2Node(Group group, Node node) {
		node.setProperty(Label.NAME.getLabel(), group.getNome());
		node.setProperty(Label.TYPE.getLabel(), group.getType().getName());

		return node;
	}

	@Override
	public Group transform2Entity(Node node) {
		Group group = new Group();
		group.setId(node.getId());
		group.setNome( (String) node.getProperty(Label.NAME.getLabel()));
		
		return group;
	}
}
