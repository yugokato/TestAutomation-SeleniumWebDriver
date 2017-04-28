package framework.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignupPage extends BasePage {
    @FindBy(css="input[name='username']")
    private WebElement usernameField;
    
    @FindBy(css="input[name='password']")
    private WebElement passwordField;
    
    @FindBy(css="input[name='confirm']")
    private WebElement confirmPasswordField;
    
    @FindBy(css="form > button[type='submit']")
    private WebElement createButton;
    
    @FindBy(css=".alert")
    private WebElement flashMessageField;

    private WebElement getUsernameField(){
        return usernameField;
    }
    
    private WebElement getPasswordField(){
        return passwordField;
    }
    
    private WebElement getConfirmPasswordField(){
        return confirmPasswordField;
    }
    
    private WebElement getCreateButton(){
        return createButton;
    }
    
    private WebElement getFlashMessageField(){
        return flashMessageField;
    }
    
    public void createNewAccount(String username, String password, String confirmPassword){
        getUsernameField().clear();
        getUsernameField().sendKeys(username);
        getPasswordField().clear();
        getPasswordField().sendKeys(password);
        getConfirmPasswordField().clear();
        getConfirmPasswordField().sendKeys(confirmPassword);
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
