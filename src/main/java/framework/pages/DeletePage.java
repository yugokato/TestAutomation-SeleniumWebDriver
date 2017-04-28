package framework.pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class DeletePage extends BasePage {
    
    @FindBy(css=".col-md-12 > h2")
    private WebElement pageHeading;
    
    @FindBy(css=".form-group label h5")
    private List<WebElement> currentMachineCheckBoxes;
    
    @FindBy(css="form button[type='submit']")
    private WebElement deleteButton;
    
    @FindBy(css=".alert")
    private WebElement flashMessageField;
    
    public WebElement getPageHeading(){
        return pageHeading;
    }
    
    public List<WebElement> getCurrentMachineCheckBoxes(){
        return currentMachineCheckBoxes;
    }
    
    public WebElement getDeleteButton(){
        return deleteButton;
    }
    
    public WebElement getFlashMessageField(){
        return flashMessageField;
    }
}