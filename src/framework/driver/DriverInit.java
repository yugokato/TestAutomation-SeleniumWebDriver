package framework.driver;

import org.openqa.selenium.WebDriver;

public class DriverInit {
	private static WebDriver driver;
	
	public static void setDriver(WebDriver tempdriver){
		driver = tempdriver;
	}
	
	public static WebDriver getDriver(){
		return driver;
	}
	
	
}
