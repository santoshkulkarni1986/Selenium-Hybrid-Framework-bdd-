package com.kushi.config;

import java.io.File;
import java.time.Duration;
import java.util.logging.Level;

import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentTest;

/**
 * Santosh Kulkarni
 */
public class DriverConfig {

    public static WebDriver driver;
    private static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    private static final ThreadLocal<String> browserName = new ThreadLocal<>();
    private static final ThreadLocal<String> baseURL = new ThreadLocal<>();
    private static final ThreadLocal<String> scenarioName = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> logger = new ThreadLocal<>();

    private static final Logger LOG = LoggerFactory.getLogger(DriverConfig.class);

    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(30);
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(60);

    public void setUpDriverDetails(String browser, String url) {
        setBrowserName(browser);
        setBaseURL(url);
        LOG.info("Setting up driver for browser: {}", browser);
        LOG.info("Navigating to URL: {}", url);
        try {
            switch (browser.toLowerCase()) {
            case "chrome":
                setUpChromeDriver();
                break;
            case "firefox":
                setUpFirefoxDriver();
                break;
            case "safari":
                setUpSafariDriver();
                break;
            case "edge":
                setUpEdgeDriver();
                break;
            case "ie":
                setUpInternetExplorerDriver();
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
            }

            WebDriver driver = getDriver();
            if (driver == null) {
                throw new RuntimeException("WebDriver initialization failed for browser: " + browser);
            }
            driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
            driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);
            driver.manage().window().maximize();
            driver.get(url);
        } catch (Exception e) {
            LOG.error("Exception during WebDriver setup for browser: {}", browser, e);
            throw new RuntimeException("WebDriver initialization failed for browser: " + browser, e);
        }
    }

    private void setUpChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        configureCommonOptions(options, "chrome");
        options.addArguments("--start-maximized"); // Ensure the browser starts maximized
        driver = new ChromeDriver(options);
        setWebDriver(driver);
        LOG.info("ChromeDriver setup complete.");
    }

    private void setUpFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        configureCommonOptions(options, "firefox");
        options.addArguments("--start-maximized"); // Ensure the browser starts maximized
        driver = new FirefoxDriver(options);
        setWebDriver(driver);
        LOG.info("FirefoxDriver setup complete.");
    }

    private void setUpSafariDriver() {
        SafariOptions options = new SafariOptions();
        configureCommonOptions(options, "safari");
        driver = new SafariDriver(options);
        setWebDriver(driver);
        LOG.info("SafariDriver setup complete.");
    }

    private void setUpEdgeDriver() {
        EdgeOptions options = new EdgeOptions();
        configureCommonOptions(options, "edge");
        driver = new EdgeDriver(options);
        setWebDriver(driver);
        LOG.info("EdgeDriver setup complete.");
    }

    private void setUpInternetExplorerDriver() {
        InternetExplorerOptions options = new InternetExplorerOptions();
        configureCommonOptions(options, "ie");
        driver = new InternetExplorerDriver(options);
        setWebDriver(driver);
        LOG.info("InternetExplorerDriver setup complete.");
    }

    private void configureCommonOptions(Object options, String browser) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        logPrefs.enable(LogType.DRIVER, Level.ALL);
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

        File logDir = new File("BrowserLogs");
        if (!logDir.exists()) {
            logDir.mkdirs(); // Create directory if it does not exist
        }

        String logFilePath = new File(logDir, browser + "-browser.log").getAbsolutePath();

        if (options instanceof ChromeOptions) {
            ChromeOptions chromeOptions = (ChromeOptions) options;
            chromeOptions.setCapability("goog:loggingPrefs", logPrefs);
            System.setProperty("webdriver.chrome.logfile", logFilePath);
            System.setProperty("webdriver.chrome.verboseLogging", "true");
        } else if (options instanceof FirefoxOptions) {
            FirefoxOptions firefoxOptions = (FirefoxOptions) options;
            firefoxOptions.setCapability("moz:firefoxOptions", logPrefs);
            System.setProperty("webdriver.firefox.logfile", logFilePath);
        } else if (options instanceof SafariOptions) {
            // SafariOptions does not support logging preferences in the same way
        } else if (options instanceof EdgeOptions) {
            EdgeOptions edgeOptions = (EdgeOptions) options;
            edgeOptions.setCapability("ms:loggingPrefs", logPrefs);
        } else if (options instanceof InternetExplorerOptions) {
            InternetExplorerOptions ieOptions = (InternetExplorerOptions) options;
            ieOptions.setCapability("se:ieOptions", logPrefs);
        }
    }

    public static WebDriver getDriver() {
        return webDriver.get();
    }

    private static void setWebDriver(WebDriver driver) {
        webDriver.set(driver);
    }

    private static void setBrowserName(String browser) {
        browserName.set(browser);
    }

    private static void setBaseURL(String url) {
        baseURL.set(url);
    }

    public static String getBrowserName() {
        return browserName.get();
    }

    public static String getBaseURL() {
        return baseURL.get();
    }

    public static String getScenarioName() {
        return scenarioName.get();
    }

    public static void setScenarioName(String scenario) {
        scenarioName.set(scenario);
    }

    public static ExtentTest getLogger() {
        return logger.get();
    }

    public static void setLogger(ExtentTest test) {
        logger.set(test);
    }

    public void closeConnection() {
        try {
            WebDriver driver = getDriver();
            if (driver != null) {
                driver.quit();
                webDriver.remove(); // Remove the driver from ThreadLocal after quitting
            }
        } catch (NoSuchSessionException e) {
            LOG.warn("Session already closed: {}", e.getMessage());
        } catch (Exception e) {
            LOG.error("Error during teardown: {}", e.getMessage());
        }
    }
}
