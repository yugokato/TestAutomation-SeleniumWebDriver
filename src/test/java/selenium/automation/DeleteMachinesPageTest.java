package selenium.automation;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import framework.pages.DeletePage;
import selenium.automation.base.BaseTest;


public class DeleteMachinesPageTest extends BaseTest {
    private DeletePage deletePage;
    private final String TEST_IP1 = "1.1.1.1";
    private final String TEST_IP2 = "2.2.2.2";
    private final String TEST_USERNAME = "test_user";
    private final String TEST_PASSWORD = "test_password";
    private List<WebElement> ipAddressList;
    
    @BeforeMethod
    public void beforeMethod() {
        restAPI.deleteAllUnknownMachines();
    }
    
    @Test(description="Verify delete machines page - success")
    public void verifyDeleteMachines() throws Exception {
        restAPI.registerMachine(TEST_IP1, TEST_USERNAME, TEST_PASSWORD);
        restAPI.registerMachine(TEST_IP2, TEST_USERNAME, TEST_PASSWORD);
        deletePage = topPage.clickDeleteMachinesButton();
        
        List<WebElement> checkBoxes = deletePage.getCurrentMachineCheckBoxes();
        for (WebElement checkBox: checkBoxes){
            if (checkBox.getAttribute("value").contains(TEST_IP1) || checkBox.getAttribute("value").contains(TEST_IP2)){
                checkBox.click();
            }
        }
        
        deletePage.getDeleteButton().submit();
        String topPageFlashMessage = topPage.getFlashMessageField().getText();
        Assert.assertTrue(topPageFlashMessage.contains("Deleted") && topPageFlashMessage.contains(TEST_IP1) && topPageFlashMessage.contains(TEST_IP2), topPageFlashMessage);
        
        ipAddressList = topPage.getIpAddressList();
        for (WebElement ip: ipAddressList){
            Assert.assertNotEquals(ip.getText(), TEST_IP1);
            Assert.assertNotEquals(ip.getText(), TEST_IP2);
        }
    }
        
    @Test(description="Verify delete machines page - No machine selected")
    public void verifyDeleteMachinesNoSelection() throws Exception {
        deletePage = topPage.clickDeleteMachinesButton();
        deletePage.getDeleteButton().submit();
        String flashMessage = deletePage.getFlashMessageField().getText();
        Assert.assertTrue(flashMessage.contains("Select machines to delete"), flashMessage);
        
    }
}