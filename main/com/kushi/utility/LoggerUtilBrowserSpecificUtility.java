package com.kushi.utility;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kushi.config.DriverConfig;

import java.util.HashMap;
import java.util.Map;

public class LoggerUtilBrowserSpecificUtility {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerUtilBrowserSpecificUtility.class);
    private static WebDriver driver;
    private static Map<String, String> performanceMetrics;

    public LoggerUtilBrowserSpecificUtility(WebDriver driver) {
        LoggerUtilBrowserSpecificUtility.driver = driver;
        LoggerUtilBrowserSpecificUtility.performanceMetrics = new HashMap<>();
    }

  
    public static void logPageLoadPerformance() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        Map<String, String> performanceMetrics = new HashMap<>();
        // Capture Navigation Timing metrics
        Object navigationTiming = js.executeScript("return window.performance.timing");
        performanceMetrics.put("Navigation Timing", navigationTiming.toString());

        // Capture Resource Timing metrics
        Object resourceTiming = js.executeScript("return window.performance.getEntriesByType('resource')");
        performanceMetrics.put("Resource Timing", resourceTiming.toString());

        // Capture Page Load Time
        Number loadTime = (Number) js.executeScript(
            "return window.performance.timing.loadEventEnd - window.performance.timing.navigationStart");
        performanceMetrics.put("Page Load Time", loadTime.toString() + " ms");

        // Capture DOM Content Loaded Time
        Number domContentLoadedTime = (Number) js.executeScript(
            "return window.performance.timing.domContentLoadedEventEnd - window.performance.timing.navigationStart");
        performanceMetrics.put("DOM Content Loaded Time", domContentLoadedTime.toString() + " ms");

        // Capture First Paint Time
        Number firstPaintTime = (Number) js.executeScript(
            "return window.performance.getEntriesByType('paint')[0].startTime");
        performanceMetrics.put("First Paint Time", firstPaintTime.toString() + " ms");

        // Log performance metrics to SLF4J Logger
        LOG.info("Performance Metrics: {}", performanceMetrics.toString());

        // Add performance metrics to Extent Report
      //  TestResultsUtils.logPerformanceMetrics(performanceMetrics);
    }
    /**
     * Returns performance metrics as a formatted string.
     */
    public String getMetrics() {
        // Format performance metrics into a readable string
        StringBuilder metricsBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : performanceMetrics.entrySet()) {
            metricsBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return metricsBuilder.toString();
    }
}
