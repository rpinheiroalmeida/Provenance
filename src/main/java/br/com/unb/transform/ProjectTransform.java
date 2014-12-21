package br.com.unb.transform;

import org.neo4j.graphdb.Node;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Project;
import br.com.unb.util.DateUtil;

@Component
public class ProjectTransform<T extends Project> implements Transform<T>{

	enum Label {
		NAME("name"), 
		DESCRIPTION("description"), 
		COORDENATOR("coordenator"),
		START_DATE("startDateTime"), 
		END_DATE("endDateTime"), 
		OBSERVATION("observation"),
		TYPE("type"), 
		PARTICIPATING_INSTITUTIONS("participatingInstitutions"),
		FUNDING_INSTITUTIONS("fundingInstitutions");
		
		private String label;
		
		Label(String label) 
		{
			this.label = label;
		}
		
		String getLabel() {
			return this.label;
		}
	}
	
	@Override
	public Node transform2Node(Project project, Node node) {
		node.setProperty(Label.NAME.getLabel(), project.getNome());
		node.setProperty(Label.DESCRIPTION.getLabel(), project.getDescricao());
		node.setProperty(Label.COORDENATOR.getLabel(), project.getCoordenador());
		node.setProperty(Label.START_DATE.getLabel(), DateUtil.date2String(project.getDataHoraInicio()));
		node.setProperty(Label.END_DATE.getLabel(), DateUtil.date2String(project.getDataHoraFim()));
		node.setProperty(Label.OBSERVATION.getLabel(), project.getObservacao());
		node.setProperty(Label.TYPE.getLabel(), project.getType().getName());

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
		project.setNome( (String) node.getProperty(Label.NAME.getLabel()));
		project.setDescricao( (String) node.getProperty(Label.DESCRIPTION.getLabel()));
		
		project.setCoordenador( (String) node.getProperty(Label.COORDENATOR.getLabel()));
		project.setDataHoraInicio( DateUtil.string2Date((String) node.getProperty(Label.START_DATE.getLabel())));
		project.setDataHoraFim( DateUtil.string2Date((String) node.getProperty(Label.END_DATE.getLabel())));
		project.setObservacao( (String) node.getProperty(Label.OBSERVATION.getLabel()));
		
		return project;
	}

}
