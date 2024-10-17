package com.kushi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kushi.utility.CucumberUtilityManager;
import com.kushi.utility.LoggerUtility;
import com.kushi.utility.TestResultsUtils;

/**
 * Helper class for managing test execution resources and reporting.
 * 
 * This class is responsible for initializing and cleaning up resources such as
 * loggers, output folders, and Extent Reports.
 * 
 * Author: Santosh Kulkarni
 */
public class ExecutionHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionHelper.class);

    static {
        try {
            // Configure the logger using the configuration file in test/properties
            LoggerUtility.configureLogger("test/properties/log4j.properties");
            LOG.info("Logger configuration loaded successfully from 'test/properties/log4j.properties'");
        } catch (Exception e) {
            LOG.error("Failed to load logger configuration. Exception: {}", e.getMessage(), e);
        }
    }

    /**
     * Load resources and initialize the report for a specific feature and browser.
     *
     * @param featureName the name of the feature file.
     * @param browserName the name of the browser being used.
     */
    public void loadResources(String featureName, String browserName) {
        try {
            LOG.info("Starting resource initialization for feature: '{}' on browser: '{}'", featureName, browserName);

            // Initialize the output folder and log file
            TestResultsUtils.outputFolder(browserName);
            LOG.info("Output folder created for browser: '{}'", browserName);

            // Initialize the Extent Report for the current feature
            TestResultsUtils.extentReportInitialize(featureName);
            LOG.info("Extent report initialized for feature: '{}'", featureName);

            LOG.info("Resources initialized successfully for feature: '{}' on browser: '{}'", featureName, browserName);
        } catch (Exception e) {
            LOG.error("Error initializing resources for feature: '{}' on browser: '{}'. Exception: {}", featureName, browserName, e.getMessage(), e);
        }
    }

    /**
     * Close resources and flush reports.
     */
    public void closeConnections() {
        try {
            LOG.info("Starting resource cleanup and closing connections");

            // Log the current scenario status
            String scenarioStatus = CucumberUtilityManager.getScenarioStatus();
            LOG.info("Current scenario status: '{}'", scenarioStatus);

            // Flush and close the Extent Report
            TestResultsUtils.extentReportFlush();
            LOG.info("Extent report flushed successfully");

            LOG.info("Resources and connections closed successfully");
        } catch (Exception e) {
            LOG.error("Error while closing connections. Exception: {}", e.getMessage(), e);
        } finally {
            try {
                // Ensure the Extent Report is closed
                TestResultsUtils.extentClose();
                LOG.info("Extent report closed successfully");
            } catch (Exception finalException) {
                LOG.error("Error finalizing the Extent Report. Exception: {}", finalException.getMessage(), finalException);
            }
        }
    }
}
