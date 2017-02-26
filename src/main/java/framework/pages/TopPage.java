package framework.pages;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import framework.pages.modal.Modal;


public class TopPage extends BasePage {

    @FindBy(css=".col-xs-4.col-sm-3.col-md-2")
    private List<WebElement> machineList;

    @FindBy(css=".col-xs-4.col-sm-3.col-md-2 > h4 > a")
    private List<WebElement> hostNameList;
    
    @FindBy(css=".col-xs-4.col-sm-3.col-md-2 > h5 > a")
    private List<WebElement> ipAddressList;
    
    @FindBy(css=".col-xs-4.col-sm-3.col-md-2 > img")
    private List<WebElement> osDistributionImgNameList;
    
    @FindBy(css=".col-md-12-flash p.flashes")
    private WebElement flashMessageField;
    
	@FindBy(css=".col-md-12 > a[class='btn btn-info']")
	private WebElement registerMachinesButton;
    
	@FindBy(css=".col-md-12 > a[class='btn btn-danger']")
	private WebElement deleteMachinesButton;
	
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
    
	public WebElement getRegisterMachinesButton(){
		return registerMachinesButton;
	}
	
	public WebElement getDeleteMachinesButton(){
		return deleteMachinesButton;
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
    
    public RegisterPage clickRegisterMachinesButton(){
    	getRegisterMachinesButton().click();
    	RegisterPage registerPage = new RegisterPage();
    	wait.until(ExpectedConditions.textToBePresentInElement(registerPage.getPageHeading(), "Register A New Machine"));
    	return registerPage;
    	
    }
    
    public DeletePage clickDeleteMachinesButton(){
    	getDeleteMachinesButton().click();
    	DeletePage deletePage = new DeletePage();
    	wait.until(ExpectedConditions.textToBePresentInElement(deletePage.getPageHeading(), "Delete Machines"));
    	return deletePage;
    }

}