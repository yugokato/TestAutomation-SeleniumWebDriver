package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage extends BasePage {
	private WebDriver driver;
	
	public RegisterPage(WebDriver driver){
		super(driver);
		this.driver = driver;
	}
}
