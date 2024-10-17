package com.kushi.steps;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GoogleSearchPlaywright {
    public static void main(String[] args) {
        // Set up WebDriverManager for Chrome
        
        // Initialize ChromeDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Navigate to Google
            driver.get("https://www.google.com");

            // Locate the search bar element
            WebElement searchBar = driver.findElement(By.name("q"));
            WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfAllElements(searchBar));
            
            wait.until(ExpectedConditions.numberOfWindowsToBe(0));
            
            // Input the search term "Playwright"
            searchBar.sendKeys("Playwright");
            
            // Submit the search
            searchBar.submit();

            // Wait for the results to load
            Thread.sleep(2000);
            
            // Optionally, print the title of the search results page
            System.out.println("Page title is: " + driver.getTitle());
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
