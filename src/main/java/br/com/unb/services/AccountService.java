package br.com.unb.services;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Account;
import br.com.unb.model.Project;
import br.com.unb.repository.AccountRepository;

@Component
public class AccountService {

	@Inject private AccountRepository accountRepository;
	
	public void save(Account account) {
		accountRepository.save(account);
	}

	public List<Account> list(Project project) {
		return accountRepository.list(project);
	}

	public Account find(long idAccount) {
		return accountRepository.find(idAccount);
	}
}
