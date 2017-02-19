package pages;
import pages.modal.Modal;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class TopPage extends BasePage {
	
	public TopPage(WebDriver driver){
		super(driver);
	}
	
    @FindBy(css=".col-xs-4.col-sm-3.col-md-2")
    private List<WebElement> machineList;

    @FindBy(css=".col-xs-4.col-sm-3.col-md-2 > h4 > a")
    private List<WebElement> hostNameList;
    
    @FindBy(css=".col-xs-4.col-sm-3.col-md-2 > h5 > a")
    private List<WebElement> ipAddressList;
    
    @FindBy(css=".col-xs-4.col-sm-3.col-md-2 > img")
    private List<WebElement> osDistributionImgNameList;
    
    @FindBy(css=".flashes")
    private WebElement flashMessageField;
    
    public List<WebElement> getMachineList(){
        return machineList;
    }
    
    public List<WebElement> getHostNameList(){
    	return hostNameList;
    }
    
    public List<WebElement> getIpAddressList(){
    	return ipAddressList;
    }
    
    public List<WebElement> getOSDistributionImgNameList(){
    	return osDistributionImgNameList;
    }
    
    public WebElement getFlashMessageField(){
    	return flashMessageField;
    }
    
    private WebElement getCurrentModalElement(){
    	String selector = "div[class='modal fade in']";
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));
    	WebElement currentModal = driver.findElement(By.cssSelector(selector));
    	return currentModal;
    }
    
    public Modal getCurrentModalInstance(){
    	return new Modal(getCurrentModalElement());
    }
    
    public void openModal(WebElement element){
    	element.click();
    	WebElement currentModal = getCurrentModalElement();
    	wait.until(ExpectedConditions.visibilityOf(currentModal));
    }
    


}
