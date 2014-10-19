package br.com.unb.controller;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.unb.infra.SessionProject;
import br.com.unb.infra.SessionUser;
import br.com.unb.model.Project;
import br.com.unb.model.User;
import br.com.unb.services.ProjectService;

@Resource
public class HomeController {

	private final SessionUser sessionUser;
	private final SessionProject sessionProjct;
	private Result result;
	@Inject private ProjectService projectService;
	
	public HomeController(SessionUser sessionUser, SessionProject sessionProject, Result result) {
		this.sessionUser = sessionUser;
		this.sessionProjct = sessionProject;
	}
	
	@Path(value = {"/pages/home"} )
	public List<Project> listarProjetos() {
		User userSession = sessionUser.getUser();
		List<Project> projects = projectService.listProjects(userSession);

		sessionProjct.setProjects(projects);
		return projects;
	}
	
	@Path(value = {"/pages/logoff"} )
	public void encerrarSessao(){
		sessionUser.encerrarSessao();
		result.use(Results.http()).body("sucesso");
	}
	
	
//	private Project montarProjeto(Long id, String descricao) {
//		Project project = new Project();// sessionProject.getProject(idProject);
//		project.setUsuario(new User());
//		project.getUsuario().setLogin("wal");
//		project.setCoordenador("Wal");
//		project.setDataHoraInicio(new Date());
//		project.setDataHoraFim(new Date());
//		project.setDescricao(descricao);
//		project.setId(id);
//		project.setNome(descricao);
//		project.setObservacao("");
//		return project;
//	}
	
//	private List<Project> listarTodos(){
//		List<Project> lista = new ArrayList<>();
//		for(int i = 0; i< 10; i++){
//			Project p = montarProjeto(Long.valueOf(i), "projeto " + i);
//			p.setAccounts(new ArrayList<Account>());		
//			
//			Account account = new Account();
//			account.setId(1L);
//			account.setNome("Account " + i);
//			account.setActivities(new ArrayList<Activity>());
//			Activity ac = new Activity();
//			ac.setId(1L);
//			ac.setNome("nome atividade");
//			account.getActivities().add(ac);
//			p.getAccounts().add(account);
//
//			Account account2 = new Account();
//			account2.setId(2L);
//			account2.setNome("Account " + i);
//			p.getAccounts().add(account2);
//			
//			lista.add(p);
//		}
//		return lista;
//	}
	
}
