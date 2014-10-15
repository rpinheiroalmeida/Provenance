package br.com.unb.controller;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.unb.infra.SessionProject;
import br.com.unb.infra.SessionUser;
import br.com.unb.model.Project;
import br.com.unb.model.User;
import br.com.unb.services.ProjectService;

@Resource
public class HomeController {

	private final SessionUser sessionUser;
	private final SessionProject sessionProjct;
	@Inject private ProjectService projectService;
	
	public HomeController(SessionUser sessionUser, SessionProject sessionProject) {
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
}
