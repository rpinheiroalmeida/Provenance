package br.com.unb.transform;

import org.neo4j.graphdb.Node;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.labels.ActivityLabel;
import br.com.unb.model.Activity;
import br.com.unb.util.DateUtil;

@Component
public class ActivityTransform<T extends Activity> implements Transform<T>{

	@Override
	public Node transform2Node(Activity activity, Node node) {
		node.setProperty(ActivityLabel.NAME.getLabel(), activity.getNome());
		node.setProperty(ActivityLabel.PROGRAM_NAME.getLabel(), activity.getNomePrograma());
		node.setProperty(ActivityLabel.PROGRAM_VERSION.getLabel(), activity.getVersaoPrograma());
		node.setProperty(ActivityLabel.COMMAND_LINE.getLabel(), activity.getLinhaComando());
		node.setProperty(ActivityLabel.FUNCTION.getLabel(), activity.getFuncao());
		node.setProperty(ActivityLabel.START_DATE.getLabel(), DateUtil.date2String(activity.getDataInicio() ));
		node.setProperty(ActivityLabel.END_DATE.getLabel(), DateUtil.date2String(activity.getDataFim() ));
		node.setProperty(ActivityLabel.START_HOUR.getLabel(), activity.getHoraInicio());
		node.setProperty(ActivityLabel.END_HOUR.getLabel(), activity.getHoraFim());
		node.setProperty(ActivityLabel.TYPE.getLabel(), activity.getType().getName());
		if (activity.getNomeArquivo() != null) {
			node.setProperty(ActivityLabel.FILE_NAME.getLabel(), activity.getNomeArquivo());
		}

		return node;
	}

	@Override
	public Activity transform2Entity(Node node) {
		Activity activity = new Activity();
		activity.setId(node.getId());
		activity.setNome( (String) node.getProperty(ActivityLabel.NAME.getLabel()) );
		activity.setNomePrograma((String) node.getProperty(ActivityLabel.PROGRAM_NAME.getLabel()) );
		activity.setVersaoPrograma( (String) node.getProperty(ActivityLabel.PROGRAM_VERSION.getLabel()) );
		activity.setLinhaComando( (String) node.getProperty(ActivityLabel.COMMAND_LINE.getLabel()) );
		activity.setFuncao( (String) node.getProperty(ActivityLabel.FUNCTION.getLabel()) );
		activity.setDataInicio( DateUtil.string2Date((String) node.getProperty(ActivityLabel.START_DATE.getLabel())) );
		activity.setDataFim( DateUtil.string2Date((String) node.getProperty(ActivityLabel.START_DATE.getLabel())) );
		activity.setHoraInicio( (String) node.getProperty(ActivityLabel.START_HOUR.getLabel()) );
		activity.setHoraFim( (String) node.getProperty(ActivityLabel.END_HOUR.getLabel()) );

		if (node.hasProperty(ActivityLabel.FILE_NAME.getLabel())) {
			activity.setNomeArquivo( (String) node.getProperty(ActivityLabel.FILE_NAME.getLabel()) );
		}
		return activity;
	}
}
