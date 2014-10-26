package br.com.unb.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.unb.infra.SessionGroup;
import br.com.unb.model.Group;

@Resource
public class GroupController {

	private Result result;
	private SessionGroup sessionGroup;
	
	public GroupController(Result result, SessionGroup sessionGroup) {
		super();
		this.result = result;
		this.sessionGroup = sessionGroup;
	}

	@Get(value="/new/group")
	public void formInicial(){
		result.use(Results.http()).body("sucesso");
	}


	@Get(value = {"/group/find"} )
	public Group findGroup(long idGroup) {
		Group group = new Group();
		group.setId(1L);
		group.setNome("Grouop");
		result.use(Results.json()).from(group).serialize();
		return group;
		
	}

	@Post(value = {"/save/group"})
	public void save(Group group) {
		System.out.println("Salvando grupo");
		List<Group> grupos = sessionGroup.getGroups();
		grupos.add(group);
		sessionGroup.setGroups(grupos);
		
	}
	
	@Get("/groups")
	public List<Group> listarTodos(){
		List<Group> lista = new ArrayList<>();
		for(int i = 0; i < 10; i++){
			Group group = new Group();
			group.setId(1L);
			group.setNome("Grouop");
			lista.add(group);
		}
		result.use(Results.json()).from(lista).serialize();
		return lista;
	}
	
}
