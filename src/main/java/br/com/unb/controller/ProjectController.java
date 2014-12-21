package br.com.unb.controller;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.unb.infra.SessionProject;
import br.com.unb.infra.SessionUser;
import br.com.unb.model.Project;
import br.com.unb.model.User;
import br.com.unb.services.ProjectService;

@Resource
public class ProjectController {

	private final SessionProject sessionProject;
	private final SessionUser sessionUser;
	private Result result;
	@Inject private ProjectService projectService;
	
	public ProjectController(Result result, SessionProject sessionProject, SessionUser sessionUser) {
		this.result = result;
		this.sessionProject = sessionProject;
		this.sessionUser = sessionUser;
	}
	
	@Get(value = {"/novoProjeto"} )
	public void criarProjeto() {
		System.out.println("Criando novo projeto...");
		result.use(Results.http()).body("sucesso");
	}
	
	@Path(value = {"/salvarProjeto"} )
	@Post
	public void salvarProjeto(Project project) {
		project.setUsuario(sessionUser.getUser());
		projectService.save(project);
		result.use(Results.http()).body("sucesso");
	}
	
	@Get(value = {"/find/project"})
	public Project findProject(long idProject) {
//		result.use(Results.http()).body("sucesso");
//		return projectService.queryPorject(idProject);
//		result.include("project", sessionProject.getProject(idProject));
		Project project = sessionProject.getProject(idProject);
		project.setUsuario(sessionUser.getUser());
		result.use(Results.json()).from(project).serialize();
		return sessionProject.getProject(idProject);
	}

/*	private Project montarProjeto(Long id, String descricao) {
		Project project = new Project();// sessionProject.getProject(idProject);
		project.setUsuario(new User());
		project.getUsuario().setLogin("wal");
		project.setCoordenador("Wal");
		project.setDataHoraInicio(new Date());
		project.setDataHoraFim(new Date());
		project.setDescricao(descricao);
		project.setId(id);
		project.setNome(descricao);
		project.setObservacao("");
		return project;
	}
	*/
	@Get("/projects")
	public List<Project> listarTodos(){
		User userSession = sessionUser.getUser();
		List<Project> lista = projectService.listProjects(userSession);
		
//		lista = new ArrayList<>();
//		for(int i = 0; i< 10; i++){
//			lista.add(montarProjeto(Long.valueOf(i), "projeto " + i));
//		}
		sessionProject.setProjects(lista);
		return lista;
	}
	
}
