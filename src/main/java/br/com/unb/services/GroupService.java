package br.com.unb.services;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Group;
import br.com.unb.repository.GroupRepository;

@Component
public class GroupService {

	@Inject private GroupRepository groupRepository;
	
	public void save(Group group) {
		groupRepository.save(group);
	}

	public List<Group> listByAccount(long idAccount) {
		return groupRepository.listByAccount(idAccount);
	}

	public Group findGroupByActivity(long idActivity) {
		return groupRepository.findByActivity(idActivity);
	}

//	public List<Account> list(Project project) {
//		return accountRepository.list(project);
//	}
//
//	public Account find(long idAccount) {
//		Account account = accountRepository.find(idAccount);
//		account.setProject(projectService.findProjectByAccount(idAccount));
//		return account; 
//	}
//
//	public Account findAccountByActivity(long idActivity) {
//		Account account = accountRepository.findAccountByActivity(idActivity);
//		account.setProject(projectService.findProjectByAccount(account.getId()));
//		return account;
//	}
}
