package test.SeleniumTestAutomation.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import framework.api.RestAPI;
import framework.driver.DriverInit;
import framework.pages.RegisterPage;
import framework.pages.TopPage;
import framework.pages.modal.Modal;

public class BaseTest {
    protected WebDriver driver;
    protected JavascriptExecutor jse;
    protected RestAPI restAPI; 
    protected TopPage topPage;
    protected Modal currentModal;
    protected RegisterPage registerPage;
    
    @BeforeClass(alwaysRun=true)
    public void beforeClassBase() {
    	System.setProperty("webdriver.firefox.marionette","lib");
    	driver = new FirefoxDriver();
    	DriverInit.setDriver(driver);
        topPage = new TopPage();
        restAPI = new RestAPI();
        jse = (JavascriptExecutor)driver;
    }
    
    @AfterClass(alwaysRun=true)
    public void afterClassBase() {
        driver.quit();
    }
    
    @BeforeMethod(alwaysRun=true)
    public void beforeMethodBase() {
    	driver.get("http://localhost:5000");
    	driver.manage().window().maximize();
    }

}
