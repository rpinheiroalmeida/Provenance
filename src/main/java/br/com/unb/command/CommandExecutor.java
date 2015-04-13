package br.com.unb.command;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Activity;
import br.com.unb.watch.FileService;
import br.com.unb.watch.FileService.FileWatcher;

import com.google.inject.Inject;

@Component
public class CommandExecutor {

	private CommandLine commandLine;
	@Inject private FileService fileService;
	
	public CommandExecutor(String directory, Activity activity) {
		this(new CommandLine(directory, activity));
	}
	
	public CommandExecutor(CommandLine commandLine) {
		this.commandLine = commandLine;
	}
	
	public CommandExecutor() {}
	
	public void execute() {
		startFileWatcher();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Thread thread = new Thread(new CommandProcess(commandLine));
		thread.start();
	}
	
	private void startFileWatcher() {
//		if (!fileWatcherDirectories.containsKey(commandLine.getActivity().getLinhaComando())) {
//			FileService.FileWatcher fileWatcher = new FileService().new FileWatcher(commandLine.getDirectory(), commandLine.getActivityName());
//			fileWatcher.start();
			FileWatcher fileWatcher = fileService.new FileWatcher(commandLine.getDirectory(), 
					commandLine.getActivity());
			fileWatcher.start();
//			fileWatcherDirectories.put(commandLine.getActivity().getLinhaComando(), fileWatcher);
//			fileWatcherDirectories.put(commandLine.getDirectory().getPath(), fileWatcher);
//		}
	}

	public void setCommandLine(CommandLine commandLine) {
		this.commandLine = commandLine;
	}
}
