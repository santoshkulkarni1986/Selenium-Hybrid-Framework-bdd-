package com.kushi.steps;

import com.kushi.utility.EnvironmentUtil;
import com.kushi.utility.TestResultsUtils;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) throws Throwable {
        // Get the scenario name
        String scenarioName = scenario.getName();

        // Get the tags from the scenario
        String scenarioTags = String.join(",", scenario.getSourceTagNames());

        // Get the tags filter from EnvironmentUtil
        String tagsFilter = EnvironmentUtil.getTags();

        // Check if any of the scenario tags match the tags filter
        boolean shouldRun = scenarioTags.contains(tagsFilter);

        if (shouldRun) {
            // Start the test with tags
            TestResultsUtils.startTest(scenarioName, scenarioTags);
        } else {
            // Optionally, you can skip starting the test if it doesn't match the tags filter
            System.out.println("Skipping test: " + scenarioName + " as it does not match the tag filter: " + tagsFilter);
        }
    }
}
