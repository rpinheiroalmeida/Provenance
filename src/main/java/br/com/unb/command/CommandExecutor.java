package br.com.unb.command;

public class CommandExecutor {

	private CommandProcess commandProcess;
	
	public CommandExecutor(CommandProcess commandProcess) {
		this.commandProcess = commandProcess;
	}
	
	public CommandExecutor(String command) {
		this.commandProcess = new CommandProcess(command);
	}
	
	public void execute() {
		Thread thread = new Thread(commandProcess);
		thread.start();
	}
	
	public static void main(String[] args) {
//		CommandExecutor executor = new CommandExecutor("sudo find /. -name '*.java' -print >> result.txt");
//		CommandExecutor executor = new CommandExecutor("sudo find . -name '*.java' -print >> result.txt");
//		CommandExecutor executor = new CommandExecutor("sudo cat resul.txt >> result.txt");
		CommandExecutor executor = new CommandExecutor("grep -R 'tecnologia' /Users/rodrigopinheiro/");
		

		executor.execute();
	}
}
