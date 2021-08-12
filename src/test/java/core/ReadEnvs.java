package core;

import java.util.LinkedHashMap;

import model.ScenarioObject;
import util.YamlManager;

public class ReadEnvs {
	public static LinkedHashMap<?, ?> getEnvs() throws Exception {
		return LinkedHashMap.class.cast(LinkedHashMap.class.cast(YamlManager.readYamlFromResources("env.yaml").get(0))
				.get(ScenarioObject.getEnv_tag()));
	}
}
