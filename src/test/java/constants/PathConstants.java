package constants;

import java.io.File;

public class PathConstants {
	private PathConstants() {
	}

	public static final String FIXTURES_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator
			+ "test" + File.separator + "resources" + File.separator + "fixtures";
	public static final String ENV_FILE = System.getProperty("user.dir") + File.separator + "src" + File.separator
			+ "test" + File.separator + "java" + File.separator  + File.separator + "env";
	public static final String FEATURE_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator
			+ "test" + File.separator + "resources" + File.separator + "features";
	public static final String SPREADSHEETS_PATH = System.getProperty("user.dir")+File.separator +"src"+File.separator+"test"+File.separator+"resources"+File.separator+"spreadsheets";
}
