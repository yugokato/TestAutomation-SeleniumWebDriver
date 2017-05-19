package framework.pages;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
    
    @FindBy(css=".alert")
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
        element.findElement(By.className("img-responsive")).click();
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
    
    public void waitForModalToBeClosed(){
        String selector = ".modal-open";
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(selector)));
    }
    
    public void waitForEC2StateChangeToStart(String ipAddress){
        String xpathExpression = "//*[@id=\"machine-grid-" + ipAddress + "\"]/span";
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathExpression)));
    }
    
    public void waitForEC2StateChangeToStop(String ipAddress){
        String xpathExpression = "//*[@id=\"machine-grid-" + ipAddress + "\"]/span";
        for (int i=0; i<120; i+=5){
            try{
                driver.findElement(By.xpath(xpathExpression));
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException ee){}
            }
            catch(NoSuchElementException e){
                break;
            }
        }
       
    }
    
    public void waitForMachineStatusToBeOK(String ipAddress){
        String osDistImgName;
        for (int i=0; i<60; i+=5){
            osDistImgName = getOsDistImgFieldByIpAddress(ipAddress).getAttribute("src");
            if (osDistImgName.contains("unreachable") || osDistImgName.contains("other") ){
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException e){}
            }
            else
                break;
        }
    }
    
    public void waitForMachineStatusToBeUnreachable(String ipAddress){
        String osDistImgName;
        for (int i=0; i<90; i+=5){
            osDistImgName = getOsDistImgFieldByIpAddress(ipAddress).getAttribute("src");
            if (! osDistImgName.contains("unreachable")){
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException e){}
            }
            else
                break;
        }
    }

    public WebElement getMachineElementByHostname(String hostname){
        WebElement machineElement = getHostnameField(hostname).findElement(By.xpath("../.."));
        return machineElement;
    }
    
    public WebElement getMachineElementByIpAddress(String ipAddress){
        WebElement machineElement = getIpAddressField(ipAddress).findElement(By.xpath("../.."));
        return machineElement;
    }

    public WebElement getHostnameField(String hostname){
        WebElement hostnameField = driver.findElement(By.linkText(hostname));
        return hostnameField;
    }

    public WebElement getIpAddressField(String ipAddress){
        WebElement ipAddressField = driver.findElement(By.linkText(ipAddress));
        return ipAddressField;
    }

    public WebElement getOsDistImgFieldByHostname(String hostname){
        WebElement parentElement = getMachineElementByHostname(hostname);
        WebElement distImgField = parentElement.findElement(By.cssSelector("img"));
        return distImgField;
    }
    
    public WebElement getOsDistImgFieldByIpAddress(String ipAddress){
        WebElement parentElement = getMachineElementByIpAddress(ipAddress);
        WebElement distImgField = parentElement.findElement(By.cssSelector("img"));
        return distImgField;
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
    
    public ArrayList<String> getIpAddressListStr(){
        ArrayList<String> ipAddressListStr = new ArrayList<>();
        for (WebElement ipAddress: getIpAddressList()){
            ipAddressListStr.add(ipAddress.getText());
        }
        return ipAddressListStr;
    }
}
