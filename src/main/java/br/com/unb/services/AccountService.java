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
	@Inject private ProjectService projectService;
	
	public void save(Account account) {
		accountRepository.save(account);
	}

	public List<Account> list(Project project) {
		return accountRepository.list(project);
	}

	public Account find(long idAccount) {
		Account account = accountRepository.find(idAccount);
		account.setProject(projectService.findProjectByAccount(idAccount));
		return account; 
	}

	public Account findAccountByActivity(long idActivity) {
		Account account = accountRepository.findAccountByActivity(idActivity);
		account.setProject(projectService.findProjectByAccount(account.getId()));
		return account;
	}
	
	public String buildGraph(long idAccount, long idGroup) {
		return accountRepository.buildGraph(idAccount, idGroup);
	}
	
	
}
