package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class FileManager {
	public static File getRecursiveFiles(String direcctory, String filename) throws Exception {
		List<File> files = new LinkedList<File>();
		Files.walk(Paths.get(direcctory)).filter(Files::isRegularFile)
				.filter((f) -> f.toFile().getName().replaceAll("\\..+", "").equals(filename))
				.forEach((f) -> files.add(f.toFile()));
		return files.get(0);

	}
	
	public static String readTxtFile(File file) throws IOException {
		return new String (Files.readAllBytes(file.toPath()));
	}
}
