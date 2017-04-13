package selenium.automation;

import java.lang.reflect.Method;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import framework.pages.DeletePage;
import framework.pages.RegisterPage;
import selenium.automation.base.BaseTest;

public class LoginPageTest extends BaseTest {
    private final String TEST_USERNAME = "testuser123";
    private final String TEST_PASSWORD = "testpassword123";
    private final String TEST_INVALID_USERNAME = "testinvaliduser123";
    private final String TEST_INVALID_PASSWORD = "testinvalidpassword123";
    
    @BeforeClass
    public void beforeClass() {
        restAPI.addUser(TEST_USERNAME, TEST_PASSWORD);
    }
    
    @BeforeMethod
    public void beforeMethod(Method method) {
        System.out.println(method.getName());
        driver.get("http://localhost:5000/logout");
    }
    
    @AfterMethod
    public void afterMethod() {
        driver.get("http://localhost:5000/logout");
    }
    
    @AfterClass
    public void afterClass() {
        restAPI.deleteUser(TEST_USERNAME);
    }
    
    @Test(description="Verify login/logout - succeed", priority=0)
    public void verifyLoginLogout() throws Exception {
        String flashMessages;
        
        loginPage.doLogin(TEST_USERNAME, TEST_PASSWORD);
        flashMessages = topPage.getFlashMessages();
        String loginUsername = topPage.getLoginUsername();
        Assert.assertEquals(flashMessages, String.format("Logged in successfully as a user \"%s\"", TEST_USERNAME));
        Assert.assertEquals(loginUsername, TEST_USERNAME);
        
        topPage.doLogout();
        flashMessages = loginPage.getFlashMessages();
        Assert.assertEquals(flashMessages, String.format("Logged out from a user \"%s\"", TEST_USERNAME));
    }
    
    @Test(description="Verify login - invalid username/password", priority=1)
    public void verifyLoginWithInvalidCredentials() throws Exception {
        String flashMessages;
        
        loginPage.doLogin(TEST_INVALID_USERNAME, TEST_PASSWORD);
        flashMessages = topPage.getFlashMessages();
        Assert.assertEquals(flashMessages, "Username or password is not correct");
        
        loginPage.doLogin(TEST_USERNAME, TEST_INVALID_PASSWORD);
        flashMessages = topPage.getFlashMessages();
        Assert.assertEquals(flashMessages, "Username or password is not correct");
        
    }
    
    @Test(description="Verify accessing pages before logged in is not allowed", priority=2)
    public void verifyAccessUnquthorizedPages() throws Exception {
        String flashMessages;
        
        // test-1 (top page)
        driver.navigate().refresh();
        driver.get("http://localhost:5000/top");
        flashMessages = loginPage.getFlashMessages();
        Assert.assertEquals(flashMessages, "Please log in to access this page.");
        
        // test-2 (register page)
        driver.navigate().refresh();
        driver.get("http://localhost:5000/register");
        flashMessages = loginPage.getFlashMessages();
        Assert.assertEquals(flashMessages, "Please log in to access this page.");
        
        // test-3 (delete page)
        driver.navigate().refresh();
        driver.get("http://localhost:5000/delete");
        flashMessages = loginPage.getFlashMessages();
        Assert.assertEquals(flashMessages, "Please log in to access this page.");
    }
    
    @Test(description="Verify accessing pages after logged in is allowed", priority=3)
    public void verifyAccessAuthorizedPagesAfterLogin() throws Exception {
        loginPage.doLogin(TEST_USERNAME, TEST_PASSWORD);
        
        // test-1 (top page)
        driver.get("http://localhost:5000/top");
        Assert.assertEquals(topPage.getPageHeadingField().getText(), "Linux Machines");
        
        // test-2 (register page)
        driver.get("http://localhost:5000/register");
        RegisterPage registerPage = new RegisterPage();
        Assert.assertEquals(registerPage.getPageHeadingField().getText(), "Register A New Machine");
        
        // test-3 (delete page)
        driver.get("http://localhost:5000/delete");
        DeletePage deletePage = new DeletePage();
        Assert.assertEquals(deletePage.getPageHeadingField().getText(), "Delete Machines");

    }
}
