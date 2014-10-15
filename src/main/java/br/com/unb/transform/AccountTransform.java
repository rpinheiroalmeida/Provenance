package br.com.unb.transform;

import org.neo4j.graphdb.Node;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Account;
import br.com.unb.util.DateUtil;

@Component
public class AccountTransform<T extends Account> implements Transform<T>{

	enum Label {
		NAME("name"), 
		DESCRIPTION("description"), 
		EXECUTION_PLACE("executionPlace"),
		START_DATE("startDateTime"), 
		END_DATE("endDateTime"), 
		OBSERVATION("observation"),
		VERSION("version"),
		VERSION_DATE("versionDate"),
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
	public Node transform2Node(Account account, Node node) {
		node.setProperty(Label.NAME.getLabel(), account.getNome());
		node.setProperty(Label.DESCRIPTION.getLabel(), account.getDescricao());
		node.setProperty(Label.EXECUTION_PLACE.getLabel(), account.getLocalExecucao());
		node.setProperty(Label.START_DATE.getLabel(), DateUtil.date2String(account.getDataHoraInicio()));
		node.setProperty(Label.END_DATE.getLabel(), DateUtil.date2String(account.getDataHoraFim()));
		node.setProperty(Label.OBSERVATION.getLabel(), account.getAnotacoes());
		node.setProperty(Label.TYPE.getLabel(), account.getType().getName());

		return node;
	}

	@Override
	public Account transform2Entity(Node node) {
		Account account = new Account();
		account.setId(node.getId());
		account.setNome( (String) node.getProperty(Label.NAME.getLabel()));
		account.setDescricao( (String) node.getProperty(Label.DESCRIPTION.getLabel()));
		account.setLocalExecucao( (String) node.getProperty(Label.EXECUTION_PLACE.getLabel()));
		account.setDataHoraInicio( DateUtil.string2Date((String) node.getProperty(Label.START_DATE.getLabel())));
		account.setDataHoraFim( DateUtil.string2Date((String) node.getProperty(Label.END_DATE.getLabel())));
		account.setAnotacoes( (String) node.getProperty(Label.OBSERVATION.getLabel()));
		
		return account;
	}
}
