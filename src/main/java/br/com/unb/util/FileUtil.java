package br.com.unb.util;

import java.io.File;

public class FileUtil {

	private static final String root = "/Users/rodrigopinheiro/Documents/projetos/provenance";
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	private FileUtil() {}
	
	public static void createFolder(String pathname) {
		File pathToFile = new File(pathname);
		if (!pathToFile.exists()) {
			pathToFile.mkdirs();
		}
	}
	
	public static String getPathToFile(String loginUser) {
		return root + FILE_SEPARATOR + loginUser + FILE_SEPARATOR;
	}
}
