package model;

import java.util.LinkedHashMap;

public class ScenarioObject {
	private static String env_tag;
	private static String scenario_name;
	private static String path_url;

	public static String getEnv_tag() {
		return env_tag;
	}

	public static void setEnv_tag(String env_tag) {
		ScenarioObject.env_tag = env_tag;
	}

	public static String getScenario_name() {
		return scenario_name;
	}

	public static void setScenario_name(String scenario_name) {
		ScenarioObject.scenario_name = scenario_name;
	}

	public static String getPath_url() {
		return path_url;
	}

	public static void setPath_url(String path_url) {
		ScenarioObject.path_url = path_url;
	}

	public static LinkedHashMap<String, ?> getQueryStringParams() {
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> paramsMap = LinkedHashMap.class.cast(
				LinkedHashMap.class.cast(EnvObject.getPath_urls().get(ScenarioObject.getPath_url())).get("params"));
		return paramsMap;
	}
}
