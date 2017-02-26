package framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import framework.driver.DriverInit;

public class BasePage {
	protected WebDriver driver;
	protected WebDriverWait wait;

	public BasePage(){
		this.driver = DriverInit.getDriver();
		PageFactory.initElements(this.driver, this);
		this.wait = new WebDriverWait(this.driver, 10);
	}

}