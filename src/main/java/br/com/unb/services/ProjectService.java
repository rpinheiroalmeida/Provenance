package br.com.unb.services;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Account;
import br.com.unb.model.Project;
import br.com.unb.model.User;
import br.com.unb.repository.ProjectRepository;

@Component
public class ProjectService {

	@Inject private ProjectRepository projectRepository;
	@Inject private AccountService accountService;
	@Inject private ActivityService activityService;
	
	public void save(Project project) {
		projectRepository.save(project);
		projectRepository.createGraph(project.getUsuario().getId());
//		projectRepository.createTestGraph(project.getUsuario().getId());
	}

	public List<Project> listProjects(User userSession) {
		List<Project> projects = projectRepository.list(userSession);
		for (Project project : projects) {
			project.setAccounts(accountService.list(project));
			for (Account account : project.getAccounts()) {
				account.setActivities(activityService.list(account));
			}
		}
		return projects;
	}
	
	public Project findProjectByAccount(long idAccount) {
		return projectRepository.findProjectByAccount(idAccount);
	}
	
	public Project findById(Long idProject) {
		return projectRepository.findById(idProject);
	}
}
