package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

public class JsonManager {
	public static JsonObject getJsonObject(File jsonFile) throws IOException {
		String jsonStr = new String(Files.readAllBytes(jsonFile.toPath()));
		JsonParser jsonParser = new JsonParser();
		return jsonParser.parse(jsonStr).getAsJsonObject();
	}
	
	public static JsonArray getJsonArray(File jsonFile) throws IOException {
		String jsonStr = new String(Files.readAllBytes(jsonFile.toPath()));
		JsonParser jsonParser = new JsonParser();
		return jsonParser.parse(jsonStr).getAsJsonArray();
	}

	public static JsonObject getJsonObject(String jsonStr) throws IOException {
		JsonParser jsonParser = new JsonParser();
		return jsonParser.parse(jsonStr).getAsJsonObject();
	}

	public static String getJsonAsString(File jsonFile) throws IOException {
		String jsonStr = new String(Files.readAllBytes(jsonFile.toPath()));
		JsonParser jsonParser = new JsonParser();
		return jsonParser.parse(jsonStr).getAsJsonObject().toString();
	}

	public static String getPrettyJson(String json) {
		Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
		JsonElement je = parse(new StringReader(json));
		return gson.toJson(je);
	}

	private static JsonElement parse(Reader json) throws JsonIOException, JsonSyntaxException {
		try {
			JsonReader jsonReader = new JsonReader(json);
			jsonReader.setLenient(true);
			JsonElement element = parse(jsonReader);
			if (!element.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
				throw new JsonSyntaxException("Did not consume the entire document.");
			}
			return element;
		} catch (MalformedJsonException e) {
			throw new JsonSyntaxException(e);
		} catch (IOException e) {
			throw new JsonIOException(e);
		} catch (NumberFormatException e) {
			throw new JsonSyntaxException(e);
		}
	}

	private static JsonElement parse(JsonReader json) throws JsonIOException, JsonSyntaxException {
		boolean lenient = json.isLenient();
		json.setLenient(true);
		try {
			return Streams.parse(json);
		} catch (StackOverflowError e) {
			throw new JsonParseException("Failed parsing JSON source: " + json + " to Json", e);
		} catch (OutOfMemoryError e) {
			throw new JsonParseException("Failed parsing JSON source: " + json + " to Json", e);
		} finally {
			json.setLenient(lenient);
		}
	}

	public static void saveJsonObjectToFile(JsonObject jsonObject, File file) throws IOException {
		OutputStream os = new FileOutputStream(file);
		os.write(getPrettyJson(jsonObject.toString()).getBytes());
		os.flush();
		os.close();
	}

	public static boolean isJSONValid(String jsonInString) {
		try {
			getJsonObject(jsonInString);
			return true;
		} catch (com.google.gson.JsonSyntaxException | IllegalStateException | IOException ex) {
			return false;
		}
	}
}
