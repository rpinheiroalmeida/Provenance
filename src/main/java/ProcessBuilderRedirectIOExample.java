import java.io.File;
import java.io.IOException;


public class ProcessBuilderRedirectIOExample {

	public static void main(String[] args) throws InterruptedException, IOException {
		ProcessBuilder pb = new ProcessBuilder("ping -c 10 google.com");

		System.out.println("Redirect output and error to file");
        File outputFile = new File("/Users/rodrigopinheiro/Documents/projetos/provenance");

	}
}
