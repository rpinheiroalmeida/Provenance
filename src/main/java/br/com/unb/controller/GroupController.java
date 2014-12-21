package br.com.unb.controller;

import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.unb.model.Group;
import br.com.unb.services.GroupService;

@Resource
public class GroupController {

	private Result result;
	private GroupService groupService;
	
	public GroupController(Result result, GroupService groupService) {
		super();
		this.result = result;
		this.groupService = groupService;
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
		groupService.save(group);
	}
	
	@Get("/groups")
	public List<Group> listarByAccount(long idAccount){
		List<Group> listGroup = groupService.listByAccount(idAccount);
		result.use(Results.json()).from(listGroup).serialize();
		return listGroup;
	}
	
}
