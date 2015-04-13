package br.com.unb.services;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.command.CommandExecutor;
import br.com.unb.command.CommandLine;
import br.com.unb.model.Activity;

@Component
public class CommandService {

	
	@Inject private CommandExecutor commandExecutor;
	
	private static Set<String> executedCommand = new HashSet<>();
	
	public void executeCommand(Activity activity, String directory) {
		if (!executedCommand.contains(getKey(activity))) {
			commandExecutor.setCommandLine(new CommandLine(directory, activity));
			commandExecutor.execute();
			
			executedCommand.add(getKey(activity));
		}
	}
	
	private String getKey(Activity activity) {
		return String.format("%d#%s%s", activity.getAccount().getId(),
				activity.getNome(), activity.getLinhaComando());
	}
}
