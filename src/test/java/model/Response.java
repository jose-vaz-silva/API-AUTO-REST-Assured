package model;

public class Response {
	private static io.restassured.response.Response response;

	public static io.restassured.response.Response getResponse() {
		return response;
	}

	public static void setResponse(io.restassured.response.Response response) {
		Response.response = response;
	}
}