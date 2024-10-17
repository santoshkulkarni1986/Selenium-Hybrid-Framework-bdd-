package com.kushi.runner;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import com.kushi.config.*;
import com.kushi.steps.TC_01_LoginStepDef;
import com.kushi.utility.Constants;
import com.kushi.utility.PropertyUtils;
import com.kushi.utility.TestResultsUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;


public class BaseTestConfiguration {
    public static DriverConfig driverConfig = new DriverConfig();
    final ExecutionHelper helper = new ExecutionHelper();
	final TestResultsUtils results = new TestResultsUtils();
	public static String baseProjectPath = System.getProperty(Constants.USER_DIR);
	public static PropertyUtils logFilePath = new PropertyUtils(baseProjectPath.concat(Constants.LOG4J_FILE_PATH));
    private static final Logger LOG = LoggerFactory.getLogger(BaseTestConfiguration.class);

	@Parameters({ "browser", "url" ,"featureName"})
    @BeforeTest
    public void setUp(String browser, String url,String featureName ) throws Exception, Throwable {
        LOG.info("Setting up driver for browser: " + browser);

        driverConfig.setUpDriverDetails(browser, url);
		helper.loadResources(featureName,browser);
        LOG.info("Setup completed for feature: " + featureName);


    }

    @AfterTest
    public void tearDown() throws Throwable {
        LOG.info("Teardown started.");
        /** EXTENT REPORT */
        TestResultsUtils.extentReportFlush(); // Flush the extent report
        TestResultsUtils.endTest(); // End the current test
        /** CLOSE DRIVER */
        driverConfig.closeConnection();
        LOG.info("Teardown completed.");

        }
}
