package selenium.automation;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import framework.pages.SignupPage;
import selenium.automation.base.BaseTest;

public class SignupPageTest extends BaseTest {
    
    private final String TEST_USERNAME = "testuser123";
    private final String TEST_PASSWORD = "testpassword123";
    private SignupPage signupPage;
    
    @BeforeMethod
    public void beforeMethod(Method method) {
        System.out.println(method.getName());
        driver.get("http://localhost:5000/logout");
    }
    
    @AfterClass
    public void afterClass() {
        restAPI.deleteUser(TEST_USERNAME);
    }
    
    @Test(description="Verify create a new account - succeed", priority=0)
    public void verifyCreateNewAccount() throws Exception {
        signupPage = loginPage.clickSignupButton();
        signupPage.createNewAccount(TEST_USERNAME, TEST_PASSWORD);
        String flashMessagesSignup = signupPage.getFlashMessages();
        String flashMessagesLogin = loginPage.getFlashMessages();
        Assert.assertTrue(flashMessagesSignup.isEmpty(), flashMessagesSignup);
        Assert.assertTrue(flashMessagesLogin.contains("Created"));
        Assert.assertTrue(flashMessagesLogin.contains(TEST_USERNAME));
    }
    
    @Test(description="Verify create a new account - duplicate", priority=1)
    public void verifyCreateNewAccountDuplicate() throws Exception {
        signupPage = loginPage.clickSignupButton();
        signupPage.createNewAccount(TEST_USERNAME, TEST_PASSWORD);
        String flashMessagesSignup = signupPage.getFlashMessages();
        Assert.assertTrue(flashMessagesSignup.contains("already exists"), flashMessagesSignup);
    }
    
}
