package br.com.unb.infra;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
@ApplicationScoped
public class Neo4jEmbeddedGraphDatabaseFactory implements ComponentFactory<GraphDatabaseService> {

	private static final String DB_PATH = "/usr/local/Cellar/neo4j/2.0.1/libexec/data/graph.db";
	private GraphDatabaseService graphDb;
	
	@PostConstruct
    public void init() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
    }
	
	@PreDestroy
	public void destroy() {
		graphDb.shutdown();
	}
	
	public GraphDatabaseService getInstance() {
		return this.graphDb;
	}
	

}
