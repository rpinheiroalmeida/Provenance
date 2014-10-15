package br.com.unb.transform;

import org.neo4j.graphdb.Node;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Activity;
import br.com.unb.util.DateUtil;

@Component
public class ActivityTransform<T extends Activity> implements Transform<T>{

	enum Label {
		NAME("name"), 
		PROGRAM_NAME("programName"), 
		PROGRAM_VERSION("programVersion"),
		COMMAND_LINE("commandLine"), 
		FUNCTION("function"), 
		START_DATE("startDate"),
		END_DATE("endDate"),
		FILE_NAME("fileName"),
		START_HOUR("startHour"),
		END_HOUR("startHour"),
		TYPE("type");
		
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
	public Node transform2Node(Activity activity, Node node) {
		node.setProperty(Label.NAME.getLabel(), activity.getNome());
		node.setProperty(Label.PROGRAM_NAME.getLabel(), activity.getNomePrograma());
		node.setProperty(Label.PROGRAM_VERSION.getLabel(), activity.getVersaoPrograma());
		node.setProperty(Label.COMMAND_LINE.getLabel(), activity.getLinhaComando());
		node.setProperty(Label.FUNCTION.getLabel(), activity.getFuncao());
		node.setProperty(Label.START_DATE.getLabel(), DateUtil.date2String(activity.getDataInicio() ));
		node.setProperty(Label.END_DATE.getLabel(), DateUtil.date2String(activity.getDataFim() ));
		node.setProperty(Label.START_HOUR.getLabel(), activity.getHoraInicio());
		node.setProperty(Label.END_HOUR.getLabel(), activity.getHoraFim());
		node.setProperty(Label.TYPE.getLabel(), activity.getType().getName());
		if (activity.getNomeArquivo() != null) {
			node.setProperty(Label.FILE_NAME.getLabel(), activity.getNomeArquivo());
		}

		return node;
	}

	@Override
	public Activity transform2Entity(Node node) {
		Activity activity = new Activity();
		activity.setId(node.getId());
		activity.setNome( (String) node.getProperty(Label.NAME.getLabel()) );
		activity.setNomePrograma((String) node.getProperty(Label.PROGRAM_NAME.getLabel()) );
		activity.setVersaoPrograma( (String) node.getProperty(Label.PROGRAM_VERSION.getLabel()) );
		activity.setLinhaComando( (String) node.getProperty(Label.COMMAND_LINE.getLabel()) );
		activity.setFuncao( (String) node.getProperty(Label.FUNCTION.getLabel()) );
		activity.setDataInicio( DateUtil.string2Date((String) node.getProperty(Label.START_DATE.getLabel())) );
		activity.setDataFim( DateUtil.string2Date((String) node.getProperty(Label.START_DATE.getLabel())) );
		activity.setHoraInicio( (String) node.getProperty(Label.START_HOUR.getLabel()) );
		activity.setHoraFim( (String) node.getProperty(Label.END_HOUR.getLabel()) );

		if (node.hasProperty(Label.FILE_NAME.getLabel())) {
			activity.setNomeArquivo( (String) node.getProperty(Label.FILE_NAME.getLabel()) );
		}
		return activity;
	}
}
