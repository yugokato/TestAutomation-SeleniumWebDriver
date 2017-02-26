package test.SeleniumTestAutomation;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import framework.pages.RegisterPage;
import test.SeleniumTestAutomation.base.BaseTest;


public class RegisterMachinesPageTest extends BaseTest{
	private RegisterPage registerPage;
    private List<String> errorsList;
    private final String VALID_TEST_IP = "1.1.1.1";
    private final String VALID_TEST_USERNAME = "test_user";
    private final String VALID_TEST_PASSWORD = "test_password";
    
    @BeforeMethod
    public void beforeMethod() {
    	restAPI.deleteAllUnknownMachines();
    }

    @Test(description="Verify register a new machine page - success")
	public void verifyRegisterMachinesSuccess() {
    	String topPageFlashMessage;
    	
    	registerPage = topPage.clickRegisterMachinesButton();
    	registerPage.enterIpAddress(VALID_TEST_IP);
    	registerPage.enterUsername(VALID_TEST_USERNAME);
    	registerPage.enterPassword(VALID_TEST_PASSWORD);
    	
    	errorsList = registerPage.clickRegisterButtonAndGetErrors();
    	topPageFlashMessage = topPage.getFlashMessageField().getText(); 
    	
    	Assert.assertTrue(errorsList.get(0).isEmpty());
    	Assert.assertTrue(errorsList.get(1).isEmpty());
    	Assert.assertTrue(errorsList.get(2).isEmpty());
    	Assert.assertTrue(errorsList.get(3).isEmpty());
    	Assert.assertTrue(topPageFlashMessage.contains(VALID_TEST_IP) && topPageFlashMessage.contains("Added"));
    	
    }

    @Test(description="Verify register a new machine page - No password")
	public void verifyRegisterMachinesNoPassword() throws Exception {
    	registerPage = topPage.clickRegisterMachinesButton();
    	
    	// test-1
    	registerPage.enterIpAddress(VALID_TEST_IP);
    	registerPage.enterUsername(VALID_TEST_USERNAME);
    	
    	errorsList = registerPage.clickRegisterButtonAndGetErrors(); 
    	
    	Assert.assertTrue(errorsList.get(0).isEmpty());
    	Assert.assertTrue(errorsList.get(1).isEmpty());
    	Assert.assertTrue(errorsList.get(2).isEmpty());
    	Assert.assertTrue(errorsList.get(3).isEmpty());

    }
    
    @Test(description="Verify register a new machine page - IP duplication")
	public void verifyRegisterMachinesIPDuplicated() {
    	restAPI.addMachine(VALID_TEST_IP, VALID_TEST_PASSWORD);
    	
    	registerPage = topPage.clickRegisterMachinesButton();
    	registerPage.enterIpAddress(VALID_TEST_IP);
    	registerPage.enterUsername(VALID_TEST_USERNAME);
    	registerPage.enterPassword(VALID_TEST_PASSWORD);
    	
    	errorsList = registerPage.clickRegisterButtonAndGetErrors(); 
    	
    	Assert.assertTrue(errorsList.get(0).contains("already exists"));
    	Assert.assertTrue(errorsList.get(1).isEmpty());
    	Assert.assertTrue(errorsList.get(2).isEmpty());
    	Assert.assertTrue(errorsList.get(3).isEmpty());
    	
    }

    @Test(description="Verify register a new machine page - Invalid IP address")
	public void verifyRegisterMachinesInvalidIP() throws Exception {
    	registerPage = topPage.clickRegisterMachinesButton();
    	// test-1
    	registerPage.enterIpAddress("1.1.1.256");
    	registerPage.enterUsername(VALID_TEST_USERNAME);
    	registerPage.enterPassword(VALID_TEST_PASSWORD);
    	
    	errorsList = registerPage.clickRegisterButtonAndGetErrors(); 
    	
    	Assert.assertTrue(errorsList.get(0).isEmpty());
    	Assert.assertTrue(errorsList.get(1).equals("Please enter a valid IP address"));
    	Assert.assertTrue(errorsList.get(2).isEmpty());
    	Assert.assertTrue(errorsList.get(3).isEmpty());
    	
    	// test-2
    	registerPage.enterIpAddress("1.1.1");
    	registerPage.enterUsername(VALID_TEST_USERNAME);
    	registerPage.enterPassword(VALID_TEST_PASSWORD);
    	
    	errorsList = registerPage.clickRegisterButtonAndGetErrors(); 
    	
    	Assert.assertTrue(errorsList.get(0).isEmpty());
    	Assert.assertTrue(errorsList.get(1).equals("Please enter a valid IP address"));
    	Assert.assertTrue(errorsList.get(2).isEmpty());
    	Assert.assertTrue(errorsList.get(3).isEmpty());
    	
    	// test-3
    	registerPage.enterIpAddress("aaaaa");
    	registerPage.enterUsername(VALID_TEST_USERNAME);
    	registerPage.enterPassword(VALID_TEST_PASSWORD);
    	
    	errorsList = registerPage.clickRegisterButtonAndGetErrors(); 
    	
    	Assert.assertTrue(errorsList.get(0).isEmpty());
    	Assert.assertTrue(errorsList.get(1).equals("Please enter a valid IP address"));
    	Assert.assertTrue(errorsList.get(2).isEmpty());
    	Assert.assertTrue(errorsList.get(3).isEmpty());

    	// test-4
    	registerPage.enterIpAddress("");
    	registerPage.enterUsername(VALID_TEST_USERNAME);
    	registerPage.enterPassword(VALID_TEST_PASSWORD);
    	
    	errorsList = registerPage.clickRegisterButtonAndGetErrors(); 
    	
    	Assert.assertTrue(errorsList.get(0).isEmpty());
    	Assert.assertTrue(errorsList.get(1).equals("Please enter a valid IP address"));
    	Assert.assertTrue(errorsList.get(2).isEmpty());
    	Assert.assertTrue(errorsList.get(3).isEmpty());
    }
    
    @Test(description="Verify register a new machine page - Invalid username")
	public void verifyRegisterMachinesInvalidUsername() {
    	registerPage = topPage.clickRegisterMachinesButton();
    	
    	// test-1
    	registerPage.enterIpAddress(VALID_TEST_IP);
    	registerPage.enterUsername("!@#$%");
    	registerPage.enterPassword(VALID_TEST_PASSWORD);
    	
    	errorsList = registerPage.clickRegisterButtonAndGetErrors(); 
    	
    	Assert.assertTrue(errorsList.get(0).isEmpty());
    	Assert.assertTrue(errorsList.get(1).isEmpty());
    	Assert.assertTrue(errorsList.get(2).equals("Please enter a valid username"));
    	Assert.assertTrue(errorsList.get(3).isEmpty());
    	
    	// test-2
    	registerPage.enterIpAddress(VALID_TEST_IP);
    	registerPage.enterUsername("");
    	registerPage.enterPassword(VALID_TEST_PASSWORD);
    	
    	errorsList = registerPage.clickRegisterButtonAndGetErrors(); 
    	
    	Assert.assertTrue(errorsList.get(0).isEmpty());
    	Assert.assertTrue(errorsList.get(1).isEmpty());
    	Assert.assertTrue(errorsList.get(2).equals("Please enter a valid username"));
    	Assert.assertTrue(errorsList.get(3).isEmpty());
    }
    
    @Test(description="Verify register a new machine page - Invalid password")
	public void verifyRegisterMachinesInvalidPassword() {
    	registerPage = topPage.clickRegisterMachinesButton();
    	
    	// test-1
    	registerPage.enterIpAddress(VALID_TEST_IP);
    	registerPage.enterUsername(VALID_TEST_USERNAME);
    	registerPage.enterPassword("asdf.asdf");
    	
    	errorsList = registerPage.clickRegisterButtonAndGetErrors(); 
    	
    	Assert.assertTrue(errorsList.get(0).isEmpty());
    	Assert.assertTrue(errorsList.get(1).isEmpty());
    	Assert.assertTrue(errorsList.get(2).isEmpty());
    	Assert.assertTrue(errorsList.get(3).equals("Please enter a valid passowrd"));

    }
    
    @Test(description="Verify register a new machine page - Multiple invalid fields")
	public void verifyRegisterMachinesMultiInvalidFields() {
    	registerPage = topPage.clickRegisterMachinesButton();
    	
    	// test-1
    	registerPage.enterIpAddress("1.1.1");
    	registerPage.enterUsername("!@#$%");
    	registerPage.enterPassword("asdf.asdf");
    	
    	errorsList = registerPage.clickRegisterButtonAndGetErrors(); 
    	Assert.assertTrue(errorsList.get(0).isEmpty());
    	Assert.assertTrue(errorsList.get(1).equals("Please enter a valid IP address"));
    	Assert.assertTrue(errorsList.get(2).equals("Please enter a valid username"));
    	Assert.assertTrue(errorsList.get(3).equals("Please enter a valid passowrd"));

    }
}
