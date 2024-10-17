package com.kushi.steps;

import com.kushi.config.DriverConfig;
import com.kushi.pages.LoginPage;
import com.kushi.utility.TestResultsUtils;
import com.kushi.utility.WrapperUtility;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Collection;
import java.util.Set;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import io.cucumber.java.Scenario;

public class TC_01_LoginStepDef {

    private static final Logger LOG = LoggerFactory.getLogger(TC_01_LoginStepDef.class);
    
    
   

    @When("I enter username {string}")
    public void i_enter_username(String userName) {
    	 long startTime = System.currentTimeMillis(); // Start timer
        try {
            TestResultsUtils.logStepStart("Entering username: " + userName);
            WrapperUtility.enterText(LoginPage.username, userName);
            LOG.info("User is able to enter username ::PASS");
            TestResultsUtils.logPass("User is able to enter username");
            long elapsedTime = System.currentTimeMillis() - startTime; // End timer
            LOG.info("Time taken to enter username: in ms", elapsedTime); // Log the time
            TestResultsUtils.logInfo("Time taken to enter username: in ms", elapsedTime); // Log the time

        } catch (final Exception exception) {
            LOG.error("User is unable to enter username::FAIL::{}", exception.getMessage());
            TestResultsUtils.logFail("User is unable to enter username: " + exception.getMessage());
            TestResultsUtils.captureScreenshotOnFailure("enter_username");
            Assert.fail("User is unable to enter username");
        }
    }

    @When("I enter password {string}")
    public void i_enter_password(String passWord) {
        long startTime = System.currentTimeMillis(); // Start timer

        try {
            WrapperUtility.enterText(LoginPage.passWord, passWord);
            LOG.info("User is able to enter password ::PASS");
            TestResultsUtils.logPass("User is able to enter password");
            long elapsedTime = System.currentTimeMillis() - startTime; // End timer
            LOG.info("Time taken to enter password: in ms", elapsedTime); // Log the time
            TestResultsUtils.logInfo("Time taken to enter password: in ms", elapsedTime); // Log the time
        } catch (final Exception exception) {
            LOG.error("User is unable to enter password::FAIL::{}", exception.getMessage());
            TestResultsUtils.logFail("User is unable to enter password: " + exception.getMessage());
            TestResultsUtils.captureScreenshotOnFailure("enter_password");
            Assert.fail("User is unable to enter password");
        }
    }

    @When("I click on the submit button")
    public void i_click_on_the_submit_button() {
        long startTime = System.currentTimeMillis(); // Start timer

        try {
            WrapperUtility.click(LoginPage.login);
            LOG.info("User is able to click the submit button ::PASS");
            TestResultsUtils.logPass("User is able to click the submit button");
            long elapsedTime = System.currentTimeMillis() - startTime; // End timer
            LOG.info("Time taken to click the submit button: in ms", elapsedTime); // Log the time
            TestResultsUtils.logInfo("Time taken to click the submit button: in ms", elapsedTime);

        } catch (final Exception exception) {
            LOG.error("User is unable to click the submit button::FAIL::{}", exception.getMessage());
            TestResultsUtils.logFail("User is unable to click the submit button: " + exception.getMessage());
            TestResultsUtils.captureScreenshotOnFailure("click_submit_button");
            Assert.fail("User is unable to click the submit button");
        }
    }

    @Then("I should see the welcome message")
    public void i_should_see_the_welcome_message() {
        long startTime = System.currentTimeMillis(); // Start timer

        try {
            DriverConfig.getDriver().findElement(By.xpath("//a[contains(text(),'Log out')]")).click();
            LOG.info("User is able to click the logout button ::PASS");
            TestResultsUtils.logPass("User is able to click the Logout button");
            long elapsedTime = System.currentTimeMillis() - startTime; // End timer
            LOG.info("Time taken to see the welcome message in ms : ", elapsedTime); // Log the time
            TestResultsUtils.logInfo("Time taken to see the welcome message in ms ", elapsedTime);
        } catch (final Exception exception) {
            LOG.error("Welcome message is not displayed::FAIL::{}", exception.getMessage());
            TestResultsUtils.logFail("Welcome message is not displayed: " + exception.getMessage());
            TestResultsUtils.captureScreenshotOnFailure("welcome_message");
            Assert.fail("Welcome message is not displayed");
        }
    }
    
   

}
