package framework.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignupPage extends BasePage {
    @FindBy(css="input[name='username']")
    private WebElement usernameField;
    
    @FindBy(css="input[name='password']")
    private WebElement passwordField;
    
    @FindBy(css="form > button[type='submit']")
    private WebElement createButton;
    
    @FindBy(css=".col-md-12-flash2 p.flashes")
    private WebElement flashMessageField;

    private WebElement getUsernameField(){
        return usernameField;
    }
    
    private WebElement getPasswordField(){
        return passwordField;
    }
    
    private WebElement getCreateButton(){
        return createButton;
    }
    
    private WebElement getFlashMessageField(){
        return flashMessageField;
    }
    
    public void createNewAccount(String username, String password){
        getUsernameField().sendKeys(username);
        getPasswordField().sendKeys(password);
        getCreateButton().click();
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

}
