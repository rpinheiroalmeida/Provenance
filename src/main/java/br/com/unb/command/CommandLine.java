package br.com.unb.command;

import java.io.File;

import br.com.unb.model.Activity;



public class CommandLine {

	private String directory;
	private String[] command;
	private Activity activity;
	
	public CommandLine(String directory, Activity activity) {
		this.directory = directory;
		this.command = splitCommand(activity.getLinhaComando());
		this.activity = activity;
	}
	
	private String[] splitCommand(String command) {
		return command.split(" ");
	}
	
	public String[] getCommand() {
		return command;
	}
	
	public File getDirectory() {
		return new File(this.directory);
	}
	
	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
