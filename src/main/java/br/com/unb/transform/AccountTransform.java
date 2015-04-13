package br.com.unb.transform;

import org.neo4j.graphdb.Node;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.labels.AccountLabel;
import br.com.unb.model.Account;
import br.com.unb.util.DateUtil;

@Component
public class AccountTransform<T extends Account> implements Transform<T>{

	@Override
	public Node transform2Node(Account account, Node node) {
		node.setProperty(AccountLabel.NAME.getLabel(), account.getNome());
		node.setProperty(AccountLabel.DESCRIPTION.getLabel(), account.getDescricao());
		node.setProperty(AccountLabel.EXECUTION_PLACE.getLabel(), account.getLocalExecucao());
		node.setProperty(AccountLabel.START_DATE.getLabel(), DateUtil.date2String(account.getDataHoraInicio()));
		node.setProperty(AccountLabel.END_DATE.getLabel(), DateUtil.date2String(account.getDataHoraFim()));
		node.setProperty(AccountLabel.OBSERVATION.getLabel(), account.getAnotacoes());
		node.setProperty(AccountLabel.TYPE.getLabel(), account.getType().getName());

		return node;
	}

	@Override
	public Account transform2Entity(Node node) {
		Account account = new Account();
		account.setId(node.getId());
		account.setNome( (String) node.getProperty(AccountLabel.NAME.getLabel()));
		account.setDescricao( (String) node.getProperty(AccountLabel.DESCRIPTION.getLabel()));
		account.setLocalExecucao( (String) node.getProperty(AccountLabel.EXECUTION_PLACE.getLabel()));
		account.setDataHoraInicio( DateUtil.string2Date((String) node.getProperty(AccountLabel.START_DATE.getLabel())));
		account.setDataHoraFim( DateUtil.string2Date((String) node.getProperty(AccountLabel.END_DATE.getLabel())));
		account.setAnotacoes( (String) node.getProperty(AccountLabel.OBSERVATION.getLabel()));
		
		return account;
	}
}
