package framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
	protected WebDriver driver;
	protected WebDriverWait wait;

	public BasePage(){}
	
	public BasePage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
		this.wait = new WebDriverWait(this.driver, 10);
	}

}
