package framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import framework.driver.DriverInit;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(){
        this.driver = DriverInit.getDriver();
        PageFactory.initElements(this.driver, this);
        this.wait = new WebDriverWait(this.driver, 10);
    }
    
    @FindBy(id="home")
    private WebElement homeIcon;
    
    @FindBy(id="logout")
    private WebElement logoutIcon;
    
    @FindBy(partialLinkText="Logout from")
    private WebElement loginUsernameField;
    
    @FindBy(id="page-heading")
    private WebElement pageHeadingField;
    
    @FindBy(id="username-error")
    private WebElement usernameErrorField;
    
    @FindBy(id="password-error")
    private WebElement passwordErrorField;
    
    @FindBy(id="confirm-error")
    private WebElement confirmPasswordErrorField;
    
    @FindBy(id="ipaddress-error")
    private WebElement ipaddressErrorField;
    
    public WebElement getHomeIcon(){
        return homeIcon;
    }
    
    public WebElement getLogoutIcon(){
        return logoutIcon;
    }
    
    public WebElement getLoginUsernameField(){
        return loginUsernameField;
    }
    
    public WebElement getPageHeadingField(){
        return pageHeadingField;
    }
    
    public WebElement getUsernameErrorField(){
        return usernameErrorField;
    }
    
    public WebElement getPasswordErrorField(){
        return passwordErrorField;
    }
    
    public WebElement getConfirmPasswordErrorField(){
        return confirmPasswordErrorField;
    }
    
    public WebElement getIpaddressErrorField(){
        return ipaddressErrorField;
    }
    
    public String getLoginUsername(){
        return getLoginUsernameField().getText().replaceAll("Logout from", "").trim();
    }
    
    public void doLogout(){
        getLogoutIcon().click();
    }
    
    public void goToHome(){
        getHomeIcon().click();
    }
    


}