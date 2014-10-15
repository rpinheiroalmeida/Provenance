package br.com.unb.infra;

import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;
import br.com.unb.model.User;

@SessionScoped
@Component
public class SessionUser {

	private final HttpSession session;

	public SessionUser(HttpSession session) {
		this.session = session;
	}

	public void setUser(User user) {
		this.session.setAttribute("currentUser", user);
	}

	public User getUser() {
		return (User) this.session.getAttribute("currentUser");
	}

}