package core;

import static constants.PathConstants.FEATURE_PATH;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cucumber.api.Scenario;
import model.ScenarioObject;
import util.StringManager;

public class ScenarioManager {
	public static void loadSenarioName(Scenario cucumebrScenario) {
		ScenarioObject.setScenario_name(cucumebrScenario.getName());
	}

	public static void loadEnvTags() throws Exception {
		List<File> features = new LinkedList<File>();
		String tag = null;
		String path_url = null;
		boolean scenarioFinded = false;
		if (new File(FEATURE_PATH).isDirectory()) {
			features = Arrays.asList(new File(FEATURE_PATH).listFiles());
		} else {
			features.add(new File(FEATURE_PATH));
		}
		for (File feature : features) {
			if (scenarioFinded)
				break;
			String featureContent = new String(Files.readAllBytes(feature.toPath()));
			List<String> scenarioNameList = StringManager.getListMatcherByRegex(featureContent,
					"\\@.*\\r?\\n\\s*?.*\\:.*\\r?\\n");
			for (String scenarioName : scenarioNameList) {
				tag = StringManager.substringByRegex(scenarioName.split("\r?\n")[0].replaceAll("\\@", ""), "[Ee]nv\\S*")
						.replaceAll("[Ee]nv.?", "");
				path_url =  StringManager.substringByRegex(scenarioName.split("\r?\n")[0].replaceAll("\\@", ""), "[pP]ath.[Uu]rl.\\S*")
						.replaceAll("[pP]ath.[Uu]rl.", "");
				String scenario = scenarioName.split("\r?\n")[1].split("\\s*\\:\\s*")[1];
				if (scenario.equals(ScenarioObject.getScenario_name())) {
					ScenarioObject.setEnv_tag(tag);
					ScenarioObject.setPath_url(path_url);
					scenarioFinded = true;
					break;
				} else {
					scenarioFinded = false;
				}
			}
		}
		if (!scenarioFinded)
			throw new Exception("Scenario não Encontrado: " + ScenarioObject.getScenario_name());
	}
}
