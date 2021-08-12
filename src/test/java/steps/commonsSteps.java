package steps;

import java.io.IOException;

import org.junit.Assert;

import cucumber.api.java.pt.Entao;
import model.Response;

public class commonsSteps {
	@Entao("^o status code deve ser \"(.*?)\"$")
	public void vaidateStatusCode(String expectedStatusCode) throws IOException {
		Assert.assertEquals(expectedStatusCode, String.valueOf(Response.getResponse().getStatusCode()));
	}

}
