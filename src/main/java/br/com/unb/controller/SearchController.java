package br.com.unb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.unb.infra.SessionGroup;
import br.com.unb.infra.SessionProject;
import br.com.unb.infra.SessionUser;
import br.com.unb.model.Account;
import br.com.unb.model.Group;
import br.com.unb.model.Project;
import br.com.unb.model.User;
import br.com.unb.services.AccountService;
import br.com.unb.services.ProjectService;

@Resource
public class SearchController {

	private Result result;
	private SessionProject sessionProject;
	private SessionGroup sessionGroup;
	private final SessionUser sessionUser;
	@Inject private ProjectService projectService;
	@Inject private AccountService accountService;
	

	public SearchController(Result result, SessionProject sessionProject, SessionGroup sessionGroup, SessionUser sessionUser) {
		this.result = result;
		this.sessionProject = sessionProject;
		this.sessionGroup = sessionGroup;
		this.sessionUser = sessionUser;
	}
	
	//carrega o form com os combos de grupo e projeto preenchidos
	@Get("/new/search")
	public void formInicial(){
		result.use(Results.http()).body("sucesso");
		//listar os projetos e os grupos e armazenar na sessão dos mesmos a fim de garantir que sempre aparecerão atualizados
		carregarProjetos();
		carregarGrupos();
	}
	
	private void carregarGrupos() {
		//carregar na sessao dos grupos a lista de grupos cadastradas
		List<Group> groups = new ArrayList<Group>();
		for(int i = 0; i < 10; i++){
			Group g = new Group();
			g.setId(Long.valueOf(i));
			g.setNome("Group " + i);
			groups.add(g);
		}
		sessionGroup.setGroups(groups);
	}

	private void carregarProjetos() {
		User userSession = sessionUser.getUser();
		List<Project> projects = projectService.listProjects(userSession);

		sessionProject.setProjects(projects);
	}

	//efetua a pesquisa
	@Get("/busca/search")
	public String search(Long idProject, Long idAccount, Long idGroup){
		//System.out.println("Search controller.search");
		String json = accountService.buildJsonAccount(idAccount, idGroup);
		result.use(Results.http()).body(json);
		sessionUser.setJson(json);
		return json;
//		result.use(Results.http()).body("<h1>Resultado da consulta</h1>");
	}
	
	@Get("/busca/accounts")
	public List<Account> buscarAccountsByProject(Long idProject){
		Project project = projectService.findById(idProject);
		List<Account> list = accountService.list(project);
		result.use(Results.json()).from(list, "accounts").serialize();
		return list;		
	}
	
}
