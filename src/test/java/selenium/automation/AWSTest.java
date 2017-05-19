package selenium.automation;

import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import framework.pages.modal.Modal;
import selenium.automation.base.BaseTest;

public class AWSTest extends BaseTest {
    private Modal currentModal;
    private final String TEST_IP_AWS = "52.52.224.126";
    private final String TEST_USERNAME_AWS = "ec2-user";
    private List<WebElement> machineList;
    private List<WebElement> ipAddressList;

    @BeforeMethod
    public void beforeMethod(Method method) {
        System.out.println(method.getName());
        restAPI.deleteAllUnknownMachines();
        restAPI.deleteMachine(TEST_IP_AWS);
        topPage.waitForAjaxToLoad();
        
        // register a new machine whose ip address is AWS
        restAPI.registerMachine(TEST_IP_AWS, TEST_USERNAME_AWS);
        topPage.waitForAjaxToLoad();
    }
    
    @AfterMethod
    public void afterMethod() {
        restAPI.deleteMachine(TEST_IP_AWS);
    }
    
    @Test(description="Verify a tag 'AWS' is added if the ip address belongs to AWS", priority=0)
    public void verifyTopPageAWS() throws Exception {
        ipAddressList = topPage.getIpAddressList();
        Assert.assertTrue(ipAddressList.get(0).getText().contains("AWS"));
    }
    
    @Test(description="Verify modal shows start/stop instance button for AWS", priority=1)
    public void verifyModalAWS() throws Exception {
        restAPI.registerMachine(TEST_IP_AWS, TEST_USERNAME_AWS);
        topPage.waitForAjaxToLoad();
        
        machineList = topPage.getMachineList();
        for (int i=0; i<machineList.size(); i++){
            // Open modal dialog
            topPage.openModal(machineList.get(i));
            driver.switchTo().activeElement();
            currentModal = topPage.getCurrentModalInstance();
            
            // Check if it has "Start instance" button and it is displayed
            if (i == 0){
                Assert.assertTrue(currentModal.isEC2ControlButtonExists());
                Assert.assertTrue(currentModal.getEC2ControlButton().isDisplayed());
                Assert.assertEquals(currentModal.getEC2ControlButton().getText(), "Start Instance");
            }
            else{
                Assert.assertFalse(currentModal.isEC2ControlButtonExists());
            }
            
            // Close modal
            currentModal.clickCloseButton();
            topPage.waitForModalToBeClosed();
        }           
    }
    
    @Test(description="Verify start EC2 instance", priority=2)
    public void verifyStartEC2InstanceAndAjax() throws Exception {
        // Open modal
        topPage.openModal(topPage.getMachineElementByIpAddress(TEST_IP_AWS + " (AWS)"));
        driver.switchTo().activeElement();
        currentModal = topPage.getCurrentModalInstance();
        
        // Start EC2 Instance and wait until Ajax processes EC2 Instance's state change (stopped -> pending)
        currentModal.getEC2ControlButton().click();
        topPage.waitForEC2StateChangeToStart(TEST_IP_AWS);
        
        // Check EC2 button is disabled with updated text by Ajax
        topPage.openModal(topPage.getMachineElementByIpAddress(TEST_IP_AWS + " (AWS)"));
        driver.switchTo().activeElement();
        currentModal = topPage.getCurrentModalInstance();
        Assert.assertTrue(currentModal.getEC2ControlButton().getAttribute("class").contains("disabled"));
        Assert.assertEquals(currentModal.getEC2ControlButton().getText(), "Starting Instance");
        // Close modal
        currentModal.clickCloseButton();
        topPage.waitForModalToBeClosed();
     
        // Wait until Ajax processes EC2 Instance's state change (pending -> running)
        topPage.waitForEC2StateChangeToStop(TEST_IP_AWS);
        
        topPage.openModal(topPage.getMachineElementByIpAddress(TEST_IP_AWS + " (AWS)"));
        driver.switchTo().activeElement();
        currentModal = topPage.getCurrentModalInstance();
        Assert.assertFalse(currentModal.getEC2ControlButton().getAttribute("class").contains("disabled"));
        Assert.assertEquals(currentModal.getEC2ControlButton().getText(), "Stop Instance");
        // Close modal
        currentModal.clickCloseButton();
        topPage.waitForModalToBeClosed();
        
        topPage.waitForMachineStatusToBeOK(TEST_IP_AWS + " (AWS)");
        String osDistImg = topPage.getOsDistImgFieldByIpAddress(TEST_IP_AWS + " (AWS)").getAttribute("src");
        Assert.assertFalse(osDistImg.contains("unreachable"));
        Assert.assertFalse(osDistImg.contains("other"));
    }
    
    @Test(description="Verify stop EC2 instance", priority=3)
    public void verifyStopEC2InstanceAndAjax() throws Exception {
        // Wait until the machine state becomes OK
        topPage.waitForMachineStatusToBeOK(TEST_IP_AWS + " (AWS)");
        
        // Open modal
        topPage.openModal(topPage.getMachineElementByIpAddress(TEST_IP_AWS + " (AWS)"));
        driver.switchTo().activeElement();
        currentModal = topPage.getCurrentModalInstance();
        
        // Stop EC2 Instance and wait until Ajax processes EC2 Instance's state change (running -> stopping)
        currentModal.getEC2ControlButton().click();
        topPage.waitForEC2StateChangeToStart(TEST_IP_AWS);
        
        // Check EC2 button is disabled with updated text by Ajax
        topPage.openModal(topPage.getMachineElementByIpAddress(TEST_IP_AWS + " (AWS)"));
        driver.switchTo().activeElement();
        currentModal = topPage.getCurrentModalInstance();
        Assert.assertTrue(currentModal.getEC2ControlButton().getAttribute("class").contains("disabled"));
        Assert.assertEquals(currentModal.getEC2ControlButton().getText(), "Stopping Instance");
        // Close modal
        currentModal.clickCloseButton();
        topPage.waitForModalToBeClosed();
        
        // Wait until EC2 Instance's state changes stopping -> stopped
        topPage.waitForEC2StateChangeToStop(TEST_IP_AWS);
        
        topPage.openModal(topPage.getMachineElementByIpAddress(TEST_IP_AWS + " (AWS)"));
        driver.switchTo().activeElement();
        currentModal = topPage.getCurrentModalInstance();
        Assert.assertFalse(currentModal.getEC2ControlButton().getAttribute("class").contains("disabled"));
        Assert.assertEquals(currentModal.getEC2ControlButton().getText(), "Start Instance");
        // Close modal
        currentModal.clickCloseButton();
        topPage.waitForModalToBeClosed();
        
        topPage.waitForMachineStatusToBeUnreachable(TEST_IP_AWS + " (AWS)");
        String osDistImg = topPage.getOsDistImgFieldByIpAddress(TEST_IP_AWS + " (AWS)").getAttribute("src");
        Assert.assertTrue(osDistImg.contains("unreachable"));
    }
    
}
