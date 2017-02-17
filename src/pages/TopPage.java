package pages;
import pages.modal.Modal;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    private WebElement getCurrentModal(){
    	String selector = "div[class='modal fade in']";
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));
    	WebElement currentModal = driver.findElement(By.cssSelector(selector));
    	return currentModal;
    }
    
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
    
    public Modal openModal(WebElement currentMachine){
    	currentMachine.click();
    	WebElement currentModal = getCurrentModal();
    	wait.until(ExpectedConditions.visibilityOf(currentModal));
    	return new Modal(currentModal);
    }
    


}
