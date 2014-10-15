package br.com.unb.infra;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
public class Neo4jTransactionInterceptor implements Interceptor {

	private final GraphDatabaseService db;
	private final Validator validator;
	
	public Neo4jTransactionInterceptor(GraphDatabaseService db, Validator validator) {
		this.db = db;
		this.validator = validator;
	}

	public boolean accepts(ResourceMethod method) {
		return true;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method, Object object) throws InterceptionException {
		try(Transaction tx = db.beginTx())  {
			stack.next(method, object);
			if(! validator.hasErrors()) {
				tx.success();
			} else {
				tx.failure();
			}
		}
	}
}