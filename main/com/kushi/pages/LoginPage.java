package com.kushi.pages;

import org.openqa.selenium.By;

import com.kushi.utility.Constants;
import com.kushi.utility.PropertyUtils;
/**
 * Santosh Kulkarni
 */
public class LoginPage {

	/** The base project path. */
	public static String baseProjectPath = System.getProperty(Constants.USER_DIR);

	/** The repository. */
	public static PropertyUtils repository = new PropertyUtils(baseProjectPath.concat(Constants.LOGIN_HOMEPAGE_OBJECTREPOSITORY_PROPERTY));
	
	public static By username = By.xpath(repository.getProperty("Login_Username"));
	public static By passWord = By.xpath(repository.getProperty("Login_Password"));
	public static By login = By.xpath(repository.getProperty("Login_LoginBtn"));
	public static By logutButton=By.xpath(repository.getProperty("Login_Button_XPTHA"));
	public static By userNameHRM=By.xpath(repository.getProperty("Login_UserName_HRM"));
	public static By passWordHRM=By.xpath(repository.getProperty("Login_Password_HRM"));
	public static By loginButtonHRM=By.xpath(repository.getProperty("Login_LoginButton_HRM"));

}
