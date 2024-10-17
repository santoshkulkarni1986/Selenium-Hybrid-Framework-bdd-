package com.kushi.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.kushi.config.DriverConfig;

/**
 * Santosh Kulkarni
 */
public class TestResultsUtils {

	private static final Logger LOG = LoggerFactory.getLogger(TestResultsUtils.class);
	public static String logDirectory;
	public static String logFilePath;
	public static WebDriver driver;
	public static String baseProjectPath = System.getProperty("user.dir");
	private static final ThreadLocal<String> extentReportDirectory = new ThreadLocal<>();
	private static final ThreadLocal<ExtentReports> extent = new ThreadLocal<>();
	private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	public static void outputFolder(String browserName) throws Exception {
		try {
			// Define the ExtentReports folder in the root directory
			String extentReportsRoot = baseProjectPath.concat(File.separator).concat("ExtentReports");

			// Delete the folder if it already exists
			File extentReportsFolder = new File(extentReportsRoot);
			if (extentReportsFolder.exists()) {
				FileUtils.deleteDirectory(extentReportsFolder);
				LOG.info("Existing ExtentReports folder deleted: {}", extentReportsRoot);
			}

			// Recreate the folder structure
			DirFileUtils.createDirTree(extentReportsRoot);

			Date curDate = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateToStr = format.format(curDate);
			format = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
			dateToStr = format.format(curDate);

			String executionStartDate = dateToStr.substring(0, 10);
			String executionStartTime = dateToStr.substring(11, 19).replace(' ', '-');

			String browserDirectory = extentReportsRoot.concat(File.separator).concat(browserName.toLowerCase());
			DirFileUtils.createDirTree(browserDirectory);

			String dateDirectory = browserDirectory.concat(File.separator).concat(executionStartDate);
			DirFileUtils.createDirTree(dateDirectory);

			String timestampDirectory = dateDirectory.concat(File.separator).concat(executionStartTime);
			DirFileUtils.createDirTree(timestampDirectory);

			String reportDirectory = timestampDirectory.concat(File.separator).concat("ExtentReport");
			DirFileUtils.createDirTree(reportDirectory);
			extentReportDirectory.set(reportDirectory);

		} catch (Exception exception) {
			LOG.error("Error occurred while creating files and directories", exception);
			throw new Exception(exception);
		}
	}

	public static synchronized void extentReportInitialize(String featureName) throws Exception {
		try {
			String extentReportLocation = extentReportDirectory.get().concat(File.separator).concat(featureName)
					.concat(" - ExtentReport.html");
			CucumberUtilityManager.setExtentReportPath(extentReportLocation);
			LOG.info("Extent Report Location: {}", extentReportLocation);

			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(extentReportLocation);
			sparkReporter.config().setTheme(Theme.DARK);
			sparkReporter.config().setDocumentTitle("Automation Report");
			sparkReporter.config()
					.setReportName("Test Report Using the BrowserName is - " + DriverConfig.getBrowserName());

			// Set up ExtentReports instance
			ExtentReports reports = new ExtentReports();
			reports.setSystemInfo("Operating System", System.getProperty("os.name"));
			reports.setSystemInfo("Java Version", System.getProperty("java.version"));
			reports.setSystemInfo("Browser", DriverConfig.getBrowserName());
			reports.setSystemInfo("Author Name", "Santosh Kulkarni");
			reports.setSystemInfo("Project Name", "MyProject");
			reports.setSystemInfo("Project Version", "1.0.0");
			reports.setSystemInfo("Build Number", "1234");
			reports.setSystemInfo("Environment", "Staging");
			reports.setSystemInfo("Team", "QA Team");
			reports.attachReporter(sparkReporter);

			extent.set(reports); // Store ExtentReports instance for this thread

		} catch (Exception exception) {
			LOG.error("Error initializing extent report", exception);
			throw new Exception(exception);
		}
	}

	public static synchronized void extentReportFlush() throws Exception {
		try {
			ExtentReports reports = extent.get();
			if (reports != null) {
				reports.flush();
			}
		} catch (Exception exception) {
			LOG.error("Error flushing extent report", exception);
			throw new Exception(exception);
		}
	}

	public static synchronized void logStepStart(String stepDescription) {
		ExtentTest extentTest = test.get();
		if (extentTest != null) {
			extentTest.log(Status.INFO, "Step started: " + stepDescription);
		}
	}

	public static synchronized void startTest(String testScenario, String tags) throws Throwable {
	    try {
	        // Create a new test instance in ExtentReports
	        ExtentTest extentTest = extent.get().createTest(testScenario);
	        test.set(extentTest);

	        LOG.info("Starting test: {} in {}", testScenario, DriverConfig.getBrowserName());

	        // Use the tags parameter to assign categories
	        if (tags != null && !tags.isEmpty()) {
	            extentTest.assignCategory(tags);
	        } else {
	            extentTest.assignCategory("Uncategorized");
	        }

	        extentTest.assignAuthor("Santosh Kulkarni");
	        

	        // Set ExtentTest in the WrapperSeleniumClass and DriverConfig
	        WrapperUtility.setExtentTest(extentTest);
	        DriverConfig.setLogger(extentTest);

	        // Log additional information such as browser, OS, and execution start time
	        extentTest.log(Status.INFO, "Test Execution Started on Browser: " + DriverConfig.getBrowserName());
	        extentTest.log(Status.INFO, "Operating System: " + System.getProperty("os.name"));
	        extentTest.log(Status.INFO, "Java Version: " + System.getProperty("java.version"));

	        // Timestamp for when the test starts
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	        Date date = new Date();
	        String executionStartTime = formatter.format(date);
	        extentTest.log(Status.INFO, "Execution Start Time: " + executionStartTime);

	    } catch (Exception exception) {
	        LOG.error("Error starting test", exception);
	        throw new Exception(exception);
	    }
	}


	public static ExtentTest getExtentTest() {
		return test.get();
	}

	public static synchronized void logInfo(String message, long elapsedTime) {
		try {
			ExtentTest extentTest = test.get();
			if (extentTest != null) {
				extentTest.log(Status.INFO, message + " (Time taken: " + elapsedTime + " ms)");
			}
			LOG.info(message + " (Time taken: " + elapsedTime + " ms)");
		} catch (Exception exception) {
			LOG.error("Error logging info", exception);
		}
	}

	public static synchronized void logInfo(String message) {
		try {
			ExtentTest extentTest = test.get();
			if (extentTest != null) {
				extentTest.log(Status.INFO, message);
			}
			LOG.info(message);
		} catch (Exception exception) {
			LOG.error("Error logging info", exception);
		}
	}

	public static synchronized void logPass(String message) {
		try {
			ExtentTest extentTest = test.get();
			if (extentTest != null) {
				extentTest.log(Status.PASS, message);
			}
			LOG.info(message);
		} catch (Exception exception) {
			LOG.error("Error logging pass", exception);
		}
	}

	public static synchronized void logFail(String message) {
		try {
			ExtentTest extentTest = test.get();
			if (extentTest != null) {
				String screenshotPath = captureScreenshot(CucumberUtilityManager.getScenarioName());
				extentTest.log(Status.FAIL, message).addScreenCaptureFromPath(screenshotPath);
			}
			LOG.error(message);
		} catch (Exception exception) {
			LOG.error("Error logging fail", exception);
		}
	}

	public static synchronized void logSkip(String message) {
		try {
			ExtentTest extentTest = test.get();
			if (extentTest != null) {
				extentTest.log(Status.SKIP, message);
			}
			LOG.warn(message);
		} catch (Exception exception) {
			LOG.error("Error logging skip", exception);
		}
	}

	public static synchronized void endTest() throws Throwable {
		try {
			ExtentTest extentTest = test.get();
			if (extentTest != null) {
				extent.get().removeTest(extentTest);
				test.remove();
			}
		} catch (Exception exception) {
			LOG.error("Error ending test", exception);
		}
	}

	public static synchronized void extentClose() throws Exception {
		try {
			ExtentReports reports = extent.get();
			if (reports != null) {
				reports.flush();
				extent.remove();
			}
		} catch (Exception exception) {
			LOG.error("Error closing extent report", exception);
			throw new Exception(exception);
		}
	}

	public static String captureScreenshot(String scenarioName) {
		try {
			driver = DriverConfig.getDriver();
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String screenshotPath = extentReportDirectory.get() + File.separator + scenarioName + ".png";
			FileUtils.copyFile(scrFile, new File(screenshotPath));
			return screenshotPath;
		} catch (Exception e) {
			LOG.error("Error capturing screenshot", e);
			return null;
		}
	}

	/*
	 * public static synchronized void logBrowserDetails(String browserName, String
	 * baseURL, String scenarioName) { try { ExtentTest extentTest = test.get(); if
	 * (extentTest != null) { // Create a table with HTML to display browser details
	 * StringBuilder browserDetailsTable = new StringBuilder();
	 * browserDetailsTable.append("<h2>Log Browser Metrics</h2>");
	 * browserDetailsTable.
	 * append("<table border='1' style='width:100%; border-collapse:collapse;'>");
	 * browserDetailsTable.append("<tr><th>Detail</th><th>Value</th></tr>");
	 * browserDetailsTable.append("<tr><td>Browser Name</td><td>").append(
	 * browserName).append("</td></tr>");
	 * browserDetailsTable.append("<tr><td>Base URL</td><td>").append(baseURL).
	 * append("</td></tr>");
	 * browserDetailsTable.append("<tr><td>Scenario Name</td><td>").append(
	 * scenarioName).append("</td></tr>"); browserDetailsTable.append("</table>");
	 * 
	 * extentTest.info("Browser Details: " + browserDetailsTable.toString()); } }
	 * catch (Exception e) { LOG.error("Error logging browser details", e); } }
	 *
	 * 
	 * 
	 * Logs performance metrics in a tabular format.
	 * 
	 * @param performanceMetrics A map containing performance metrics.
	 * 
	 * public static synchronized void logPerformanceMetrics(Map<String, String>
	 * performanceMetrics) { try { ExtentTest extentTest = test.get(); if
	 * (extentTest != null) { StringBuilder metricsTable = new StringBuilder();
	 * metricsTable.append("<h2>Performance Metrics</h2>");
	 * metricsTable.append("<table border='1' style='width:100%'>");
	 * metricsTable.append("<tr><th>Metric</th><th>Value</th></tr>"); for
	 * (Map.Entry<String, String> entry : performanceMetrics.entrySet()) {
	 * metricsTable.append("<tr>");
	 * metricsTable.append("<td>").append(entry.getKey()).append("</td>");
	 * metricsTable.append("<td>").append(entry.getValue()).append("</td>");
	 * metricsTable.append("</tr>"); } metricsTable.append("</table>");
	 * extentTest.info("Performance Metrics: " + metricsTable.toString()); } } catch
	 * (Exception e) { LOG.error("Error logging performance metrics", e); } }
	 */
	public static void captureScreenshotOnFailure(String scenarioName) {
		try {
			ExtentTest extentTest = test.get();
			if (extentTest != null) {
				String screenshotPath = captureScreenshot(scenarioName);
				if (screenshotPath != null) {
					extentTest.addScreenCaptureFromPath(screenshotPath);
				}
			}
		} catch (Exception exception) {
			LOG.error("Error capturing screenshot on failure", exception);
		}
	}
}
