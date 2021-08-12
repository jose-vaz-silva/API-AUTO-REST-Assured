package core;

import static constants.PathConstants.FIXTURES_PATH;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import constants.PathConstants;
import io.restassured.response.Response;
import model.EnvObject;
import model.ScenarioObject;
import util.FileManager;
import util.JsonManager;
import util.StringManager;

public class RequestManager {
	private RequestManager() {
	}

	public static final String TOKEN_FIELD = "Token";

	public static Response post(String jsonName) throws Exception {
		File payload = FileManager.getRecursiveFiles(FIXTURES_PATH, jsonName);
		JsonObject jsonObject = JsonManager.getJsonObject(payload);
		return given().with().body(jsonObject.toString()).when().post().then().extract().response();
	}

	public static Response postFile(String filename) throws Exception {
		String filePath = PathConstants.SPREADSHEETS_PATH;
		return given().contentType("multipart/form-data")
				.multiPart("file", FileManager.getRecursiveFiles(filePath, filename)).when().post();
	}

	public static Response post(HashMap<String, Object> headers, String jsonName) throws Exception {
		File payload = FileManager.getRecursiveFiles(FIXTURES_PATH, jsonName);
		JsonObject jsonObject = JsonManager.getJsonObject(payload);
		return given().with().body(jsonObject.toString()).headers(headers).when().post();
	}

	public static Response post(String url, JsonObject json) throws Exception {
		return given().with().body(json.toString()).when().post(url);
	}

	public static Response get(HashMap<String, Object> headers) {
		return given().baseUri("http://localhost").basePath("user/details").headers(headers).when().get();
	}
	
	public static Response get(String basePath, LinkedHashMap<String, ?> params) {
		return given().params(params).basePath(basePath).when().get().then()
                .extract().response();
	}

	public static Response get() {
		return given().params(ScenarioObject.getQueryStringParams()).when().get().then()
                .extract().response();
	}
		
	public static Response put(String jsonName) throws Exception {
		File payload = FileManager.getRecursiveFiles(FIXTURES_PATH, jsonName);
		JsonObject jsonObject = JsonManager.getJsonObject(payload);
		return given().with().body(jsonObject.toString()).when().put().then()
                .extract().response();
	}
	
	public static Response put(HashMap<String, Object> headers, String payload) {
		return given().contentType("aplication/json").body(payload).baseUri("http://localhost").basePath("user/details")
				.headers(headers).when().put();
	}

	public static Response delete(HashMap<String, Object> headers, String payload) {
		return given().contentType("aplication/json").body(payload).baseUri("http://localhost").basePath("user/details")
				.headers(headers).when().delete();
	}

	public static Response patch(HashMap<String, Object> headers, String payload) {
		return given().contentType("aplication/json").body(payload).baseUri("http://localhost").basePath("user/details")
				.headers(headers).when().patch();
	}

	public static String getToken() throws Exception {
		String token = null;
		HashMap<?, ?> authenticationMap = EnvObject.getAuthetication();
		boolean containsTokenField = authenticationMap.containsKey(TOKEN_FIELD);
		if (containsTokenField && (authenticationMap.get(TOKEN_FIELD) != null)) {
			if (!(authenticationMap.get(TOKEN_FIELD).toString().isEmpty())) {
				token = EnvObject.getAuthetication().get(TOKEN_FIELD).toString();
			}
		} else {
			if (authenticationMap.containsKey(TOKEN_FIELD)) {
				authenticationMap.remove(TOKEN_FIELD);
			}
			String url = EnvObject.getBase_url() + EnvObject.getAuthenticate_url();
			Gson gson = new Gson();
			String jsonStr = gson.toJson(LinkedHashMap.class.cast(authenticationMap.get("Body")));
			JsonObject jsonPayloadObject = new JsonParser().parse(jsonStr).getAsJsonObject();
			Response response = post(url, jsonPayloadObject);
			String responseBody = response.getBody().asString();
			if (response.getStatusCode() != 404) {
				token = StringManager.getListMatcherByRegex(responseBody, "token.\\:[^,]*").get(0).split("\\s*:\\s*")[1]
						.replaceFirst("\\\"", "").replaceAll("\\\"$", "");
			}else {
				throw new Exception("Não foi possível obter o token. A url de autenticação retornou status code [ "+response.getStatusCode()+ " ].");
			}
		}
		return token;
	}

}
