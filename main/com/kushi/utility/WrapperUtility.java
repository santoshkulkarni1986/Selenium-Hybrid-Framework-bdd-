package com.kushi.utility;

import com.aventstack.extentreports.ExtentTest;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.kushi.config.DriverConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author Santosh Kulkarni
 */
public class WrapperUtility {

    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final Logger logger = Logger.getLogger(WrapperUtility.class);

    public static void setExtentTest(ExtentTest test) {
        extentTest.set(test);
    }

    private static ExtentTest getExtentTest() {
        return extentTest.get();
    }

    // Null-safe method to check ExtentTest instance
    private static void logExtentTestInfo(String message) {
        ExtentTest test = getExtentTest();
        if (test != null) {
            test.info(message);
        }
    }

    private static void logExtentTestFail(String message) {
        ExtentTest test = getExtentTest();
        if (test != null) {
            test.fail(message);
        }
    }

    private static void logExtentTestError(String message) {
        ExtentTest test = getExtentTest();
        if (test != null) {
            test.fail(message);
        }
    }

    /***
     * 
     * @param locator
     * @return
     */
    public static boolean isElementVisible(By locator) {
        try {
            WebElement element = DriverConfig.getDriver().findElement(locator);
            boolean visible = element.isDisplayed() && element.isEnabled();
            logger.info("Element visibility: " + visible + " for locator: " + locator);
            logExtentTestInfo("Checked visibility of element: " + locator + ", visible: " + visible);
            return visible;
        } catch (Exception e) {
            logger.error("Element not found: " + locator + " - " + e.getMessage());
            logExtentTestFail("Failed to find element: " + locator + " - " + e.getMessage());
            return false;
        }
    }

    /***
     * 
     * @param locator
     * @param text
     */
    public static void enterText(By locator, String text) {
        try {
            WebElement element = DriverConfig.getDriver().findElement(locator);
            element.clear();
            element.sendKeys(text);
            String locatorString = locator.toString(); // Get the locator string
            logger.info("Entered text: '" + text + "' into element: " + locatorString);
            logExtentTestInfo("Entered text: '" + text + "' into element: " + locatorString);
        } catch (Exception e) {
            logger.error("Failed to enter text: '" + text + "' into element: " + locator + " - " + e.getMessage());
            logExtentTestFail("Failed to enter text: '" + text + "' into element: " + locator + " - " + e.getMessage());
            throw new RuntimeException("Test failed due to the error: " + e.getMessage());
        }
    }

    /***
     * 
     * @param locator
     * @param text
     */
    public static void enterTextByJS(By locator, String text) {
        try {
            WebElement element = DriverConfig.getDriver().findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) DriverConfig.getDriver();
            js.executeScript("arguments[0].value='" + text + "';", element);
            logger.info("Entered text using JS: '" + text + "' in element with locator: " + locator);
            logExtentTestInfo("Entered text using JS: '" + text + "' in element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to enter text using JS: '" + text + "' in element with locator: " + locator + " - " + e.getMessage());
            logExtentTestFail("Failed to enter text using JS: '" + text + "' in element: " + locator + " - " + e.getMessage());
        }
    }

    /***
     * 
     * @param locator
     */
    public static void click(By locator) {
        try {
            WebElement element = DriverConfig.getDriver().findElement(locator);
            element.click();
            logger.info("Clicked on element with locator: " + locator);
            logExtentTestInfo("Clicked on element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to click on element with locator: " + locator + " - " + e.getMessage());
            logExtentTestFail("Failed to click on element: " + locator + " - " + e.getMessage());
        }
    }

    /***
     * 
     * @param locator
     */
    public static void clickByJS(By locator) {
        try {
            WebElement element = DriverConfig.getDriver().findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) DriverConfig.getDriver();
            js.executeScript("arguments[0].click();", element);
            logger.info("Clicked on element using JS with locator: " + locator);
            logExtentTestInfo("Clicked on element using JS: " + locator);
        } catch (Exception e) {
            logger.error("Failed to click on element using JS with locator: " + locator + " - " + e.getMessage());
            logExtentTestFail("Failed to click on element using JS: " + locator + " - " + e.getMessage());
        }
    }

    /***
     * 
     * @param locator
     * @param text
     */
    public static void selectByVisibleText(By locator, String text) {
        try {
            WebElement dropdown = DriverConfig.getDriver().findElement(locator);
            dropdown.sendKeys(text);
            logger.info("Selected from dropdown by visible text: '" + text + "' in element with locator: " + locator);
            logExtentTestInfo("Selected from dropdown by visible text: '" + text + "' in element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to select from dropdown by visible text: '" + text + "' in element with locator: " + locator + " - " + e.getMessage());
            logExtentTestFail("Failed to select from dropdown by visible text: '" + text + "' in element: " + locator + " - " + e.getMessage());
        }
    }

    /***
     * 
     * @param locator
     * @return
     */
    public static List<String> getSortedDropdownValues(By locator) {
        List<String> values = new ArrayList<>();
        try {
            List<WebElement> options = DriverConfig.getDriver().findElements(locator);
            for (WebElement option : options) {
                values.add(option.getText());
            }
            Collections.sort(values);
            logger.info("Sorted dropdown values: " + values + " for locator: " + locator);
            logExtentTestInfo("Sorted dropdown values: " + values + " for locator: " + locator);
            return values;
        } catch (Exception e) {
            logger.error("Failed to retrieve or sort dropdown values for locator: " + locator + " - " + e.getMessage());
            logExtentTestFail("Failed to retrieve or sort dropdown values for locator: " + locator + " - " + e.getMessage());
            return values;
        }
    }

    // Get dropdown count
    public static int getDropdownCount(By locator) {
        try {
            List<WebElement> options = DriverConfig.getDriver().findElements(locator);
            int count = options.size();
            logger.info("Dropdown count: " + count + " for locator: " + locator);
            logExtentTestInfo("Dropdown count: " + count + " for locator: " + locator);
            return count;
        } catch (Exception e) {
            logger.error("Failed to get dropdown count for locator: " + locator + " - " + e.getMessage());
            logExtentTestFail("Failed to get dropdown count for locator: " + locator + " - " + e.getMessage());
            return 0;
        }
    }

    // Scrolling operations
    public static void scrollToElement(By locator) {
        try {
            WebElement element = DriverConfig.getDriver().findElement(locator);
            ((JavascriptExecutor) DriverConfig.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element with locator: " + locator);
            logExtentTestInfo("Scrolled to element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to scroll to element with locator: " + locator + " - " + e.getMessage());
            logExtentTestFail("Failed to scroll to element: " + locator + " - " + e.getMessage());
        }
    }

    public static void scrollDown() {
        try {
            ((JavascriptExecutor) DriverConfig.getDriver()).executeScript("window.scrollBy(0,500)");
            logger.info("Scrolled down the page");
            logExtentTestInfo("Scrolled down the page");
        } catch (Exception e) {
            logger.error("Failed to scroll down the page - " + e.getMessage());
            logExtentTestFail("Failed to scroll down the page - " + e.getMessage());
        }
    }

    public static void scrollUp() {
        try {
            ((JavascriptExecutor) DriverConfig.getDriver()).executeScript("window.scrollBy(0,-500)");
            logger.info("Scrolled up the page");
            logExtentTestInfo("Scrolled up the page");
        } catch (Exception e) {
            logger.error("Failed to scroll up the page - " + e.getMessage());
            logExtentTestFail("Failed to scroll up the page - " + e.getMessage());
        }
    }

    // Alert handling
    public static void handleAlert(String action) {
        try {
            Alert alert = DriverConfig.getDriver().switchTo().alert();
            if ("accept".equalsIgnoreCase(action)) {
                alert.accept();
            } else if ("dismiss".equalsIgnoreCase(action)) {
                alert.dismiss();
            }
            logger.info("Alert " + action + "ed");
            logExtentTestInfo("Alert " + action + "ed");
        } catch (Exception e) {
            logger.error("Failed to handle alert - " + e.getMessage());
            logExtentTestFail("Failed to handle alert - " + e.getMessage());
        }
    }

    // Screenshot capture
    public static void captureScreenshot(String screenshotName) {
        try {
            File screenshot = ((TakesScreenshot) DriverConfig.getDriver()).getScreenshotAs(OutputType.FILE);
            File destination = new File("screenshots/" + screenshotName + ".png");
            Files.copy(screenshot.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("Screenshot taken and saved as: " + destination.getAbsolutePath());
            logExtentTestInfo("Screenshot taken and saved as: " + destination.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to capture screenshot - " + e.getMessage());
            logExtentTestFail("Failed to capture screenshot - " + e.getMessage());
        }
    }

    // Waits
    public static void waitForElement(By locator, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverConfig.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Waited for element with locator: " + locator + " to be visible");
            logExtentTestInfo("Waited for element: " + locator + " to be visible");
        } catch (TimeoutException e) {
            logger.error("Timed out waiting for element with locator: " + locator + " - " + e.getMessage());
            logExtentTestFail("Timed out waiting for element: " + locator + " - " + e.getMessage());
        }
    }

    // Switching tabs
    public static void switchToTab(int tabIndex) {
        try {
            ArrayList<String> tabs = new ArrayList<>(DriverConfig.getDriver().getWindowHandles());
            if (tabIndex < tabs.size()) {
                DriverConfig.getDriver().switchTo().window(tabs.get(tabIndex));
                logger.info("Switched to tab index: " + tabIndex);
                logExtentTestInfo("Switched to tab index: " + tabIndex);
            } else {
                logger.warn("Tab index out of bounds: " + tabIndex);
            }
        } catch (Exception e) {
            logger.error("Failed to switch to tab index: " + tabIndex + " - " + e.getMessage());
            logExtentTestFail("Failed to switch to tab index: " + tabIndex + " - " + e.getMessage());
        }
    }
}
