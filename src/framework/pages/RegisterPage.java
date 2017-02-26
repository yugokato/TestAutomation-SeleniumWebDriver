package framework.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class RegisterPage extends BasePage {
	
	@FindBy(css=".col-md-12 > h2")
	private WebElement pageHeading;
	
	@FindBy(css="input[name='InputIPAddress']")
	private WebElement ipAddressField;
	
	@FindBy(css="input[name='InputUsername']")
	private WebElement usernameField;
	
	@FindBy(css="input[name='InputPassword']")
	private WebElement passwordField;
	
	@FindBy(css="form > button[type='submit']")
	private WebElement registerButton;
	
	@FindBy(css=".col-md-12-flash2 p.flashes")
	private WebElement flashMessageField;
	
	@FindBy(css=".has-error")
	private List<WebElement> errorFieldList;
	
	@FindBy(css=".col-md-6 a img")
	private WebElement homeIcon;
	
	public WebElement getPageHeading(){
		return pageHeading;
	}
	
	public WebElement getIpAddressField(){
		return ipAddressField;
	}
	
	public WebElement getUsernameField(){
		return usernameField;
	}
	
	public WebElement getPasswordField(){
		return passwordField;
	}
	
	public WebElement getRegisterButton(){
		return registerButton;
	}
	
	public WebElement getFlashMessageField(){
		return flashMessageField; 
	}
	
	public WebElement getHomeIcon(){
		return homeIcon;
	}
	
	public List<WebElement> getErrorFieldList(){
		return errorFieldList;
	}
	
	public void enterIpAddress(String ipaddr){
		getIpAddressField().clear();
		getIpAddressField().sendKeys(ipaddr);
	}
	
	public void enterUsername(String username){
		getUsernameField().clear();
		getUsernameField().sendKeys(username);
	}
	
	public void enterPassword(String password){
		getPasswordField().sendKeys(password);
	}
	
	public ArrayList<String> clickRegisterButtonAndGetErrors(){
		getRegisterButton().click();
		
		String flashMessage = "";
		try{
			flashMessage = getFlashMessageField().getText();
		}catch(NoSuchElementException e){}
		
		List<WebElement> errorFields = getErrorFieldList();
		ArrayList<String> errorsList= new ArrayList();
		
		errorsList.add(flashMessage);
		for (WebElement element: errorFields){
			errorsList.add(element.getText());
		}
		
		return errorsList;

	}
}
