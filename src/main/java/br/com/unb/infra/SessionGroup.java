package br.com.unb.infra;

import java.util.List;

import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;
import br.com.unb.model.Group;

@SessionScoped
@Component
public class SessionGroup {
	private final HttpSession session;

	public SessionGroup(HttpSession session) {
		this.session = session;
	}

	public void setGroups(List<Group> groups) {
		this.session.setAttribute("listGroups", groups);
	}

	@SuppressWarnings("unchecked")
	public List<Group> getGroups() {
		return (List<Group>) this.session.getAttribute("listGroups");
	}
	
	public Group getProject(Long idGroup) {
		List<Group> groups = getGroups();
		for (Group group : groups) {
			if (idGroup != null && idGroup.equals(group.getId())) {
				return group;
			}
		}
		return null;
	}

}
