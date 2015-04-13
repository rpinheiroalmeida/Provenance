
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
public class ProcessBuilderMultipleCommandsExample {

	public static void main(String[] args) throws InterruptedException, IOException {
		// multiple commands
		// /C Carries out the command specified by string and then terminates
//		ProcessBuilder pb = new ProcessBuilder("cp","in.txt", "out.txt");
		ProcessBuilder pb = new ProcessBuilder("cp","in.txt", "out.txt");
		pb.directory(new File("/Users/rodrigopinheiro/Documents/projetos/provenance/AG001_EXECUTOR"));

		System.out.println("Run echo command");
		Process process = pb.start();	
		IOThreadHandler outputHandler = new IOThreadHandler(process.getInputStream());
		outputHandler.start();
		process.waitFor();
		System.out.println(outputHandler.getOutput());
		System.out.println("End");
	}

	private static class IOThreadHandler extends Thread {
		private InputStream inputStream;
		private StringBuilder output = new StringBuilder();

		IOThreadHandler(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public void run() {
			Scanner br = null;
			try {
				br = new Scanner(new InputStreamReader(inputStream));
				String line = null;
				while (br.hasNextLine()) {
					line = br.nextLine();
					output.append(line
							+ System.getProperty("line.separator"));
				}
			} finally {
				System.out.println("Output = " + output);
				br.close();
			}
		}

		public StringBuilder getOutput() {
			return output;
		}
	}
}
