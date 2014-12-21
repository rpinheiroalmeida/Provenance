package br.com.unb.services;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.command.CommandProcess;
import br.com.unb.model.Activity;

@Component
public class CommandService {

	public void executeCommand(Activity activity) {
		CommandProcess commandProcess = new CommandProcess(activity.getLinhaComando());
	}
}
