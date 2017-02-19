package framework.pages;

import org.openqa.selenium.WebDriver;

public class DeletePage extends BasePage {
	private WebDriver driver;
	
	public DeletePage(WebDriver driver){
		super(driver);
		this.driver = driver;
	}
}
