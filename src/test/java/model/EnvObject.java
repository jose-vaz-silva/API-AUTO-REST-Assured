package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnvObject {
	private static String base_url;
	private static LinkedHashMap<String, String> path_urls;
	private static String authenticate_url;
	private static String content_type;
	private static Map<String, String> headers;

	public static String getAuthenticate_url() {
		return authenticate_url;
	}

	public static void setAuthenticate_url(String authenticate_url) {
		EnvObject.authenticate_url = authenticate_url;
	}

	public static LinkedHashMap<?, ?> getAuthetication() {
		return authetication;
	}

	public static void setAuthetication(LinkedHashMap<?, ?> authetication) {
		EnvObject.authetication = authetication;
	}

	private static LinkedHashMap<?, ?> authetication;

	public static String getBase_url() {
		return base_url;
	}

	public static void setBase_url(String base_url) {
		EnvObject.base_url = base_url;
	}

	public static LinkedHashMap<String, String> getPath_urls() {
		return path_urls;
	}

	public static void setPath_urls(LinkedHashMap<String, String> path_urls) {
		EnvObject.path_urls = path_urls;
	}

	public static String getContent_type() {
		return content_type;
	}

	public static void setContent_type(String content_type) {
		EnvObject.content_type = content_type;
	}

	public static Map<String, String> getHeaders() {
		return headers;
	}

	public static void setHeaders(Map<String, String> headers) {
		EnvObject.headers = headers;
	}

	public static void addHeaders(String key, String value) {
		if (EnvObject.headers == null) {
			EnvObject.headers = new LinkedHashMap<String, String>();
		}
		EnvObject.headers.put(key, value);
	}
	
	public static void removeHeaders(String key) {
		if (EnvObject.headers != null && EnvObject.headers.containsKey(key)) {
			EnvObject.headers.remove(key);
		}
	}
	
	public static LinkedHashMap<String, ?> getQueryStringParams(String pathUrl, String paramsObjectName) {
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> paramsMap = LinkedHashMap.class.cast(
				LinkedHashMap.class.cast(EnvObject.getPath_urls().get(pathUrl)).get(paramsObjectName));
		return paramsMap;
	}
}
