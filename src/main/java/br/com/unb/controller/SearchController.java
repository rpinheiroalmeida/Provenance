package br.com.unb.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.unb.infra.SessionGroup;
import br.com.unb.infra.SessionProject;
import br.com.unb.model.Account;
import br.com.unb.model.Group;
import br.com.unb.model.Project;
import br.com.unb.model.User;

@Resource
public class SearchController {

	private Result result;
	private SessionProject sessionProject;
	private SessionGroup sessionGroup;
	

	public SearchController(Result result, SessionProject sessionProject, SessionGroup sessionGroup) {
		this.result = result;
		this.sessionProject = sessionProject;
		this.sessionGroup = sessionGroup;
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
		//carregar na sessao dos projetos a lista de projetos cadastradas
		//User userSession = sessionUser.getUser();
		List<Project> projects = new ArrayList<Project>();//projectService.listProjects(userSession);
		for(int i = 0; i< 10; i++){
			projects.add(montarProjeto(Long.valueOf(i), "projeto " + i));
		}
		
		sessionProject.setProjects(projects);
		
	}

	//efetua a pesquisa
	@Get("/busca/search")
	public void search(Long idProject, Long idAccount, Long idGroup){
		//System.out.println("Search controller.search");
		result.use(Results.http()).body("<h1>Resultado da consulta</h1>");
	}
	
	private Project montarProjeto(Long id, String descricao) {
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
	
	@Get("/busca/accounts")
	public List<Account> buscarAccountsByProject(Long idProject){
		List<Account> lista = new ArrayList<Account>();
		int qtd = Double.valueOf(Math.random() * 10).intValue();
		for(int i = 0; i < qtd; i++){
			Account a = new Account();
			a.setId(Long.valueOf(i+1));
			a.setNome("Account " + i);
			lista.add(a);
		}
		result.use(Results.json()).from(lista, "accounts").serialize();
		return lista;
	}
	
}
