package core;

import static constants.TimeOutConstants.MAX_TIMEOUT;

import java.util.Base64;
import java.util.LinkedHashMap;

import org.hamcrest.Matchers;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import constants.AuthenticationType;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import model.EnvObject;
import model.ScenarioObject;

public class Hooks {

	private static String accessTokenField = "access_token";
	RequestSpecBuilder reqBuild = new RequestSpecBuilder();
	ResponseSpecBuilder resBuild = new ResponseSpecBuilder();
	public static String currentStep;

	@Before
	public void before(Scenario scenario) throws Exception {
		System.out.println("====> Scenario: " + scenario.getName());
		ScenarioManager.loadSenarioName(scenario);
		ScenarioManager.loadEnvTags();
		setup();
	}

	@After
	public void tearDown(Scenario scenario) {
		embedScreenshot(scenario);
		DriverFactory.killDriver();
	}

	private void setup() throws Exception {
		EnvManager.loadEnvs();
		createRequestSpecification();
		createResponseSpecifications();
		loadToken();
		setRestAssuredSpecifications();
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	public void createRequestSpecification() {
		String pathUrl = LinkedHashMap.class.cast(EnvObject.getPath_urls().get(ScenarioObject.getPath_url()))
				.get("path_url").toString();
//		String queryStrings = ScenarioObject.getQueryStringParams();
		reqBuild.setContentType(EnvObject.getContent_type());
		reqBuild.setBaseUri(EnvObject.getBase_url());
		reqBuild.setBasePath(pathUrl);
	}

	public void createResponseSpecifications() {
		resBuild.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
	}

	public void setRestAssuredSpecifications() {
		if (EnvObject.getHeaders().containsKey(RequestManager.TOKEN_FIELD) || EnvObject.getHeaders().size() > 0) {
			reqBuild.addHeaders(EnvObject.getHeaders());
		}
		RestAssured.requestSpecification = reqBuild.build();
		RestAssured.responseSpecification = resBuild.build();
	}

	public void loadToken() throws Exception {
		if (!EnvObject.getAuthenticate_url().equals("") && !EnvObject.getAuthenticate_url().isEmpty()) {
			String token = new String();
			String authenticationType = EnvObject.getAuthetication().get("Authentication-Type").toString();
			switch (authenticationType) {
			case AuthenticationType.BASIC_AUTH:
				String basicCredentials = new String(Base64.getEncoder().encode(
						"username:password".getBytes("UTF-8")));
				EnvObject.addHeaders("Authorization", "Basic " + basicCredentials);
				setRestAssuredSpecifications();
				token = RequestManager.getToken();
				EnvObject.removeHeaders("Authorization");
				EnvObject.addHeaders("ACCESS_TOKEN", token);
				break;
			case AuthenticationType.BEARER_TOKEN:
				EnvObject.addHeaders("Authorization", "Bearer " + token);
				token = RequestManager.getToken();
				break;
			}
			System.setProperty(accessTokenField, token);
		}
	}

	public void embedScreenshot(Scenario scenario) {
		try {
			byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
			scenario.embed(screenshot, "image/png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
