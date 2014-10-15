package br.com.unb.controller;

import java.io.IOException;

import javax.inject.Inject;

import org.hamcrest.Matchers;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;
import br.com.unb.exception.UserException;
import br.com.unb.infra.SessionUser;
import br.com.unb.model.User;
import br.com.unb.services.UserService;

@Resource
public class IndexController {

	private Result result;
	private Validator validator;
	private final SessionUser sessionUser;
	@Inject private UserService usuarioService;
	
	@Path(value = { "/index" })
	public void index() {
	}
	
	@Path(value = { "/cadastro" })
	@Post
	public void cadastro(final User usuario) throws UserException, IOException  {
		validator.checking(new Validations() {
			{
				that(usuarioService.find(usuario).getId(), Matchers.is(Matchers.notNullValue()) , 
						"", "user.already.exists");
			}
	    });
		validator.onErrorUsePageOf(IndexController.class).index();
		usuarioService.salvar(usuario);
	}
	
	@Path(value = { "/login" })
	public void  login(final User usuario) {
		final User pUser = usuarioService.find(usuario);
		validator.checking(new Validations() {
			{
				that( pUser.getId(), Matchers.is(Matchers.notNullValue()) , "", "user.does.not.already.exists");
			}
	    });
		validator.onErrorUsePageOf(IndexController.class).index();
        sessionUser.setUser(pUser);

        result.redirectTo(HomeController.class).listarProjetos();
	}
	
	public IndexController(Result result, Validator validator, SessionUser sessionUser) {
		this.result = result;
		this.validator = validator;
		this.sessionUser = sessionUser;
	}
}
