package br.com.unb.infra;

import java.util.List;

import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;
import br.com.unb.model.Project;

@SessionScoped
@Component
public class SessionProject {

	private final HttpSession session;

	public SessionProject(HttpSession session) {
		this.session = session;
	}

	public void setProjects(List<Project> projects) {
		this.session.setAttribute("listProject", projects);
	}

	@SuppressWarnings("unchecked")
	public List<Project> getProjects() {
		return (List<Project>) this.session.getAttribute("listProject");
	}
	
	public Project getProject(Long idProject) {
		List<Project> projects = getProjects();
		for (Project project : projects) {
			if (idProject != null && idProject.equals(project.getId())) {
				return project;
			}
		}
		return null;
	}

}