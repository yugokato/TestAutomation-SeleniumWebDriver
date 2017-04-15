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
        signupPage.createNewAccount(TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD);
        String flashMessagesSignup = signupPage.getFlashMessages();
        String flashMessagesLogin = loginPage.getFlashMessages();
        Assert.assertTrue(flashMessagesSignup.isEmpty(), flashMessagesSignup);
        Assert.assertTrue(flashMessagesLogin.contains("Created"));
        Assert.assertTrue(flashMessagesLogin.contains(TEST_USERNAME));
    }
    
    @Test(description="Verify create a new account - duplicate account")
    public void verifyCreateNewAccountDuplicate() throws Exception {
        signupPage = loginPage.clickSignupButton();
        signupPage.createNewAccount(TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD);
        String flashMessagesSignup = signupPage.getFlashMessages();
        Assert.assertTrue(flashMessagesSignup.contains("already exists"), flashMessagesSignup);
    }
    
    @Test(description="Verify create a new account - password does not match")
    public void verifyCreateNewAccountPasswordMismatch() throws Exception {
        signupPage = loginPage.clickSignupButton();
        signupPage.createNewAccount(TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD + "_");
        Assert.assertEquals(signupPage.getPasswordErrorField().getText(), "Password does not match.");
    }
    
    @Test(description="Verify create a new account - missing parameters")
    public void verifyCreateNewAccountWithMissingParameters() throws Exception {
        //test-1 (no username, no password, no confirm)
        signupPage = loginPage.clickSignupButton();
        signupPage.createNewAccount("", "", "");
        Assert.assertEquals(signupPage.getUsernameErrorField().getText(), "This field is required.");
        Assert.assertEquals(signupPage.getPasswordErrorField().getText(), "This field is required.");
        Assert.assertEquals(signupPage.getConfirmPasswordErrorField().getText(), "This field is required.");
        
        //test-2 (no username)
        signupPage.createNewAccount("", TEST_PASSWORD, TEST_PASSWORD);
        Assert.assertEquals(signupPage.getUsernameErrorField().getText(), "This field is required.");

        //test-3 (no username, no password)
        signupPage.createNewAccount("", "", TEST_PASSWORD);
        Assert.assertEquals(signupPage.getUsernameErrorField().getText(), "This field is required.");
        Assert.assertEquals(signupPage.getPasswordErrorField().getText(), "This field is required.");
        
        //test-4 (no username, no confirm)
        signupPage.createNewAccount("", TEST_PASSWORD, "");
        Assert.assertEquals(signupPage.getUsernameErrorField().getText(), "This field is required.");
        Assert.assertEquals(signupPage.getConfirmPasswordErrorField().getText(), "This field is required.");

        //test-5 (no password, no confirm)
        signupPage.createNewAccount(TEST_USERNAME, "", "");
        Assert.assertEquals(signupPage.getPasswordErrorField().getText(), "This field is required.");
        Assert.assertEquals(signupPage.getConfirmPasswordErrorField().getText(), "This field is required.");
        
    }
    
}
