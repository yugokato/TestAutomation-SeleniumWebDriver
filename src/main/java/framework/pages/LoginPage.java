package framework.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    @FindBy(css="input[name='username']")
    private WebElement usernameField;
    
    @FindBy(css="input[name='password']")
    private WebElement passwordField;
    
    @FindBy(css="form > button[type='submit']")
    private WebElement loginButton;
    
    @FindBy(linkText="or create a new account?")
    private WebElement signupButton;
    
    @FindBy(css=".col-md-12-flash p.flashes")
    private WebElement flashMessageField;
    
    public WebElement getUsernameField(){
        return usernameField;
    }
    
    public WebElement getPasswordField(){
        return passwordField;
    }
    
    public WebElement getLoginButton(){
        return loginButton;
    }
    
    public WebElement getSignupButton(){
        return signupButton;
    }
    
    public WebElement getFlashMessageField(){
        return flashMessageField;
    }
    
    public SignupPage clickSignupButton(){
        getSignupButton().click();
        return new SignupPage();
    }
    
    public String getFlashMessages(){
        String flashMessages = "";
        try{
            flashMessages = getFlashMessageField().getText();
            return flashMessages;
        }catch(NoSuchElementException e){
            return flashMessages;
        }
    }
    
    public void doLogin(String username, String password){
        getUsernameField().sendKeys(username);
        getPasswordField().sendKeys(password);
        getLoginButton().click();
    }
}
