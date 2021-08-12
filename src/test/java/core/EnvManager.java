package core;

import java.util.LinkedHashMap;

import model.EnvObject;

public class EnvManager {
	private static String base_url = "Base_Url";
	private static String path_urls = "Path_Urls";
	private static String authenticate_url = "Authenticate_Url";
	private static String content_type = "Content_Type";
	private static String headers = "Headers";
	private static String authentication = "Authentication";

	@SuppressWarnings("unchecked")
	public static void loadEnvs() throws Exception {
		LinkedHashMap<?, ?> envMap = ReadEnvs.getEnvs();

		EnvObject.setBase_url(getEnv(base_url, envMap).toString());
		EnvObject.setPath_urls(getEnv(path_urls, envMap) == null ? new LinkedHashMap<String, String>() : LinkedHashMap.class.cast(getEnv(path_urls, envMap)));
		EnvObject.setAuthenticate_url(
				getEnv(authenticate_url, envMap) == null ? "" : getEnv(authenticate_url, envMap).toString());
		EnvObject.setContent_type(getEnv(content_type, envMap).toString());
		EnvObject.setHeaders(getEnv(headers, envMap) == null ? new LinkedHashMap<String, String>() : LinkedHashMap.class.cast(getEnv(headers, envMap)));
		EnvObject.setAuthetication(LinkedHashMap.class.cast(getEnv(authentication, envMap)));
	}

	private static Object getEnv(String env, LinkedHashMap<?, ?> envMap) throws Exception {
		return envMap.get(env);
	}
}
