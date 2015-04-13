package br.com.unb.command;

import java.io.IOException;

public class CommandProcess implements Runnable {

	private CommandLine commandLine;
	
	public CommandProcess(CommandLine commandLine) {
		this.commandLine = commandLine;
	}
	
	@Override
	public void run() {
		process();
	}

	private void process() {
		
		ProcessBuilder pb = new ProcessBuilder(commandLine.getCommand());
		pb.directory(commandLine.getDirectory());

		System.out.println("Run echo command: " + commandLine.getCommand());
		try {
			Process process = pb.start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}	
		System.out.println("End Execution Command: " + commandLine.getCommand());
	}
	
	
	
	
//	try {
//	System.out.println("############################ BEGIN ########################");
//	System.out.println("COMMAAND TO BE EXECUTED: " + command);
//	Process process = Runtime.getRuntime().exec(command);
//	process.waitFor();
	
//	int pid = retrievePID(process);
//	
//	System.out.println("PID: " + pid);
//	
//	while(true) {
//		Process procMonitor = Runtime.getRuntime().exec("lsof -p " + pid);
//
//		BufferedReader resultado2 = new BufferedReader(new InputStreamReader(procMonitor.getInputStream()));  
//        String s2; 
//        List<String> openFiles = new ArrayList<>();
//        while ((s2 = resultado2.readLine()) != null) {
//        	String openFile = getOpenFile(s2);
//        	if (openFile != null) {
//        		openFiles.add(openFile);
//        	}
//        	System.out.println(s2);  
//        }
//        System.out.println(openFiles);
//        System.out.println("############################ END ########################");
//	}
//} catch (IOException  e) {
//	e.printStackTrace();
//}

//	private String getOpenFile(String line) {
//		String[] tokens = line.split("\\s+");
//		if ("REG".equals(tokens[4]) && tokens[8].endsWith(".*")) {
//			return tokens[8];
//		}
//		return null;
//	}
//	
//	public static void main(String[] args) {
//		Matcher matcher = Pattern.compile("+.+").matcher("/Users/rodrigopinheiro/Bioinfo/pipeline/projeto_2nd_2013/output/2outscan");
//		System.out.println(matcher.find());
//	}
//
//	/**
//	 * Get the process id (PID) associated with a {@code Process}
//	 * @param process {@code Process}, or null
//	 * @return Integer containing the PID of the process; null if the
//	 *  PID could not be retrieved or if a null parameter was supplied
//	 */
//	Integer retrievePID(final Process process) {
//	    if (process == null) {
//	        return null;
//	    }
//
//	    //--------------------------------------------------------------------
//	    // Jim Tough - 2014-11-04
//	    // NON PORTABLE CODE WARNING!
//	    // The code in this block works on the company UNIX servers, but may
//	    // not work on *any* UNIX server. Definitely will not work on any
//	    // Windows Server instances.
//	    final String EXPECTED_IMPL_CLASS_NAME = "java.lang.UNIXProcess";
//	    final String EXPECTED_PID_FIELD_NAME = "pid";
//	    final Class<? extends Process> processImplClass = process.getClass();
//	    if (processImplClass.getName().equals(EXPECTED_IMPL_CLASS_NAME)) {
//	        try {
//	            Field f = processImplClass.getDeclaredField(
//	                    EXPECTED_PID_FIELD_NAME);
//	            f.setAccessible(true); // allows access to non-public fields
//	            int pid = f.getInt(process);
//	            return pid;
//	        } catch (Exception e) {
////	            logger.warn("Unable to get PID", e);
//	        }
//	    } else {
////	        logger.warn(Process.class.getName() + " implementation was not " +
////	                EXPECTED_IMPL_CLASS_NAME + " - cannot retrieve PID" +
////	                " | actual type was: " + processImplClass.getName());
//	    }
//	    //--------------------------------------------------------------------
//
//	    return null; // If PID was not retrievable, just return null
//	}
//
//	public String getCommand() {
//		return command;
//	}
//
//	public void setCommand(String command) {
//		this.command = command;
//	}

}
