package br.com.unb.controller;

import javax.inject.Inject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.unb.model.Account;
import br.com.unb.model.Project;
import br.com.unb.services.AccountService;

@Resource
public class AccountController {

	@Inject private Result result;
	@Inject private AccountService accountService;
	
	@Get(value = {"/novo/experimento"} )
	public void newAccount(Long idProject) {
		Account account = new Account();
		account.setProject(new Project());
		account.getProject().setId(idProject);
		
		result.use(Results.json()).from(account).recursive().serialize();
	}
	
	@Post(value = {"/salvar/account"})
	public void save(Account account) {
		accountService.save(account);
	}
	
	@Get(value = {"/find/account"})
	public Account findAcctount(long idAccount) {
		Account account = accountService.find(idAccount);
		result.use(Results.json()).from(account).serialize();
		return account;
	}
}
