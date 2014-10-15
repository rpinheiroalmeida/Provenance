package br.com.unb.transform;

import org.neo4j.graphdb.Node;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.User;

@Component
public class UserTransform<T extends User> implements Transform<T> {

	enum Label {
		LOGIN("login"), NAME("name"), PASSWORD("password");
		
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
	public Node transform2Node(User user, Node node) {
		node.setProperty( Label.LOGIN.getLabel(), user.getLogin() );
		node.setProperty( Label.NAME.getLabel(), user.getNome() );
		node.setProperty( Label.PASSWORD.getLabel(), user.getSenha() );

		return node;
	}

	@Override
	public User transform2Entity(Node node) {
		User user = new User();
//		user.setNome((String) node.getProperty(Label.NAME.getLabel()));
		user.setLogin((String) node.getProperty(Label.LOGIN.getLabel()));
		user.setId(node.getId());
		
		return user;
	}

}
