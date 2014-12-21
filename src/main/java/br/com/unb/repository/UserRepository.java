package br.com.unb.repository;

import javax.inject.Inject;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.User;
import br.com.unb.transform.UserTransform;

@Component
public class UserRepository {

	@Inject private GraphDatabaseService graphDb;
	@Inject private UserTransform<User> transformUser;
	
	public UserRepository(GraphDatabaseService graphDb, UserTransform<User> transform) {
		this.graphDb = graphDb;
		this.transformUser = transform;
	}
	
	public User find(User user) {
		Index<Node> usersIndex = graphDb.index().forNodes( "users" );
		Node userNode = usersIndex.get( "login", user.getLogin()).getSingle();
		User pUser = new User();
		if ( userNode != null )
		{
			pUser = transformUser.transform2Entity(userNode) ;
		}
		return pUser;
	}
	
	public User save(User usuario) {
		Index<Node> usersIndex = graphDb.index().forNodes( "users" );
		Node userNode = usersIndex.get( "login", usuario.getLogin() ).getSingle();
		if ( userNode == null )
		{
			
			userNode = transformUser.transform2Node(usuario, graphDb.createNode(DynamicLabel.label( "User" )) );
			usersIndex.add( userNode, "login", usuario.getLogin() );
			return transformUser.transform2Entity(userNode);
		}
		return null;
	}
}
