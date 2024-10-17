package com.kushi.runner;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

public class LoginRunner extends BaseTestConfiguration {

	@CucumberOptions(features = "test/features/", plugin = { "html:target/cucumber-html-report",
			"json:target/cucumber-json-report.json" }, glue = "com.kushi.steps", tags = "@Regression" )

	public class runner extends AbstractTestNGCucumberTests {

	}

}
