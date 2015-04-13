package br.com.unb.transform;

import org.neo4j.graphdb.Node;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.labels.ProjectLabel;
import br.com.unb.model.Project;
import br.com.unb.util.DateUtil;

@Component
public class ProjectTransform<T extends Project> implements Transform<T>{

	@Override
	public Node transform2Node(Project project, Node node) {
		node.setProperty(ProjectLabel.NAME.getLabel(), project.getNome());
		node.setProperty(ProjectLabel.DESCRIPTION.getLabel(), project.getDescricao());
		node.setProperty(ProjectLabel.COORDENATOR.getLabel(), project.getCoordenador());
		node.setProperty(ProjectLabel.START_DATE.getLabel(), DateUtil.date2String(project.getDataHoraInicio()));
		node.setProperty(ProjectLabel.END_DATE.getLabel(), DateUtil.date2String(project.getDataHoraFim()));
		node.setProperty(ProjectLabel.OBSERVATION.getLabel(), project.getObservacao());
		node.setProperty(ProjectLabel.TYPE.getLabel(), project.getType().getName());

//		String[] nomesInstituicoesParticipantes = new String[project.getNomesInstituicoesParticipantes().size()];
//		node.setProperty(Label.PARTICIPATING_INSTITUTIONS.getLabel(), project.getNomesInstituicoesParticipantes().
//				toArray(nomesInstituicoesParticipantes));
//		
//		String[] nomesInstituicoesFinanciadoras = new String[project.getNomesInstituicoesFinanciadoras().size()];
//		node.setProperty(Label.FUNDING_INSTITUTIONS.getLabel(), project.getNomesInstituicoesFinanciadoras().
//				toArray(nomesInstituicoesFinanciadoras));

		return node;
	}

	@Override
	public Project transform2Entity(Node node) {
		Project project = new Project();
		project.setId(node.getId());
		project.setNome( (String) node.getProperty(ProjectLabel.NAME.getLabel()));
		project.setDescricao( (String) node.getProperty(ProjectLabel.DESCRIPTION.getLabel()));
		
		project.setCoordenador( (String) node.getProperty(ProjectLabel.COORDENATOR.getLabel()));
		project.setDataHoraInicio( DateUtil.string2Date((String) node.getProperty(ProjectLabel.START_DATE.getLabel())));
		project.setDataHoraFim( DateUtil.string2Date((String) node.getProperty(ProjectLabel.END_DATE.getLabel())));
		project.setObservacao( (String) node.getProperty(ProjectLabel.OBSERVATION.getLabel()));
		
		return project;
	}

}
