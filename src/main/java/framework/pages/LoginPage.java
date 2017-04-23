package framework.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    @FindBy(css="input[name='username']")
    private WebElement usernameField;
    
    @FindBy(css="input[name='password']")
    private WebElement passwordField;
    
    @FindBy(id="remember_me")
    private WebElement rememberMeCheckbox;
    
    @FindBy(css="form > button[type='submit']")
    private WebElement loginButton;
    
    @FindBy(linkText="or create a new account?")
    private WebElement signupButton;
    
    @FindBy(css=".col-md-12-flash")
    private WebElement flashMessageField;
    
    public WebElement getUsernameField(){
        return usernameField;
    }
    
    public WebElement getPasswordField(){
        return passwordField;
    }
    
    public WebElement getRememberMeCheckbox(){
        return rememberMeCheckbox;
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
    
    public void doLogin(String username, String password, boolean rememberMe){
        getUsernameField().clear();
        getUsernameField().sendKeys(username);
        getPasswordField().clear();
        getPasswordField().sendKeys(password);
        if (rememberMe){
            getRememberMeCheckbox().click();
        }
        getLoginButton().click();
    }
}
