package br.com.unb.services;

import javax.inject.Inject;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.exception.UserException;
import br.com.unb.model.User;
import br.com.unb.repository.UserRepository;

@Component
public class UserService {

	@Inject UserRepository usuarioRepository;
	
	public UserService(UserRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	public User salvar(User user) throws UserException {
		return usuarioRepository.save(user);
	}

	public User find(User usuario) {
		return usuarioRepository.find(usuario);
	}

}
