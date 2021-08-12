package runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(glue = { "steps", "core" }, monochrome = true, plugin = { "pretty",
		"html:target/cucumber-html-report.html",
		"json:target/cucumber-json-report.json", }, 
		 tags ={"@run"}, features = "src/test/resources/features/")
public class RunTests {

}
