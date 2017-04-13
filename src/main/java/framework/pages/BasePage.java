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