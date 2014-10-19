package br.com.unb.controller;

import java.util.ArrayList;
import java.util.List;

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

//	private Account montarAccount() {
//		Account account = new Account();
//		account.setAnotacoes("anotacoes");
//		account.setDataHoraFim(new Date());
//		account.setDataHoraInicio(new Date());
//		account.setDataVersao(new Date());
//		account.setDescricao("descricao");
//		account.setId(1L);
//		account.setLocalExecucao("localExecucao");
//		account.setNome("nome de testes");
//		account.setProject(new Project());
//		account.getProject().setId( Double.valueOf(Math.random() * 9).longValue()  );
//		account.getProject().setNome("project name");
//		account.setVersao("1.0.0");
//		return account;
//	}
	
	@Get(value = {"/find/accountsByProject"})
	public List<Account> findAccountsFromProject(long idProject) {
		List<Account> lista = new ArrayList<Account>();
//		for(int i = 0; i < 10; i++){
//			Account a = montarAccount();
//			lista.add(a);
//		}
		result.use(Results.json()).from(lista).serialize();
		return lista;
		
	}
	
}
