package selenium.automation;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import framework.pages.modal.Modal;
import selenium.automation.base.BaseTest;

import org.testng.Assert;

public class ModalTest extends BaseTest{
    private Modal currentModal;
    private final String TEST_IP = "1.1.1.1";
    private final String TEST_USERNAME = "test_user";
    private final Pattern PATTERN_LAST_UPDATED = Pattern.compile("^(.+)\\s\\(([0-9]+)");
    List<WebElement> machineList;
    List<WebElement> hostNameList;
    List<WebElement> ipAddressList;
    List<WebElement> osDistributionImgNameList;
    
    @BeforeMethod
    public void beforeMethod(Method method) {
        System.out.println(method.getName());
        restAPI.deleteAllUnknownMachines();
        restAPI.registerMachine(TEST_IP, TEST_USERNAME);
        homePage.waitForAjaxToLoad();
    }
    
    @Test(description="Verify modal contents in the home page are valid based on machine's status")
    public void verifyModalContents() throws Exception {
        machineList = homePage.getMachineList();
        hostNameList = homePage.getHostNameList();
        ipAddressList = homePage.getIpAddressList();
        osDistributionImgNameList = homePage.getOSDistributionImgNameList();
        String lastUpdated, hostName, ipAddress, status, statusImgName, osDistribution; 
        String release, macAddress, uptime, cpuLoadAvg, memoryUsage, diskUsage;
        Map<String, String> modalContents;
        
        for (int i=0; i<machineList.size(); i++){
            homePage.openModal(machineList.get(i));
            currentModal = homePage.getCurrentModalInstance();

            driver.switchTo().activeElement();
            modalContents = currentModal.getModalContents();
            
            // Close modal
            currentModal.clickCloseButton();
            homePage.waitForModalToBeClosed();
            
            lastUpdated = modalContents.get("LAST_UPDATED");
            hostName = modalContents.get("HOST_NAME");
            ipAddress = modalContents.get("IP_ADDRESS");
            status = modalContents.get("STATUS");
            statusImgName = modalContents.get("STATUS_IMG_NAME");
            osDistribution = modalContents.get("OS_DISTRIBUTION");
            release = modalContents.get("RELEASE");
            macAddress = modalContents.get("MAC_ADDRESS");
            uptime = modalContents.get("UPTIME");
            cpuLoadAvg = modalContents.get("CPU_LOAD_AVG");
            memoryUsage = modalContents.get("MEMORY_USAGE");
            diskUsage = modalContents.get("DISK_USAGE");

            Matcher m = PATTERN_LAST_UPDATED.matcher(lastUpdated);
            if(m.find()){
                lastUpdated = m.group(2);
            }
            
            if (hostName.equals("#Unknown")){
                Assert.assertEquals(hostNameList.get(i).getText(), hostName);
                Assert.assertEquals(ipAddressList.get(i).getText(), ipAddress);
                if (status.contains("Unreachable")){
                    Assert.assertEquals(statusImgName, "status_unreachable.png");
                }
                else if (modalContents.get("STATUS").contains("Unknown")){
                    Assert.assertEquals(statusImgName, "status_unknown.png");
                }
                Assert.assertEquals(osDistribution, "None");
                Assert.assertTrue(osDistributionImgNameList.get(i).getAttribute("src").contains("other"));
                Assert.assertEquals(release, "None");
                Assert.assertEquals(macAddress, "None");
                Assert.assertEquals(uptime, "None");
                Assert.assertEquals(cpuLoadAvg, "None");
                Assert.assertEquals(memoryUsage, "None");
                Assert.assertEquals(diskUsage, "None");
                Assert.assertTrue(Integer.parseInt(lastUpdated) > 0, lastUpdated);
            }
            
            else {
                Assert.assertTrue(Integer.parseInt(lastUpdated) < 90, lastUpdated);
                Assert.assertEquals(hostNameList.get(i).getText(), hostName);
                Assert.assertEquals(ipAddressList.get(i).getText(), ipAddress);
                Assert.assertTrue(status.contains("OK"));
                Assert.assertEquals(statusImgName, "status_ok.png");
                Assert.assertTrue(osDistributionImgNameList.get(i).getAttribute("src").contains(osDistribution.toLowerCase()));
                Assert.assertNotEquals(release, "None");
                Assert.assertNotEquals(macAddress, "None");
                Assert.assertNotEquals(uptime, "None");
                Assert.assertNotEquals(cpuLoadAvg, "None");
                Assert.assertNotEquals(memoryUsage, "None");
                Assert.assertNotEquals(diskUsage, "None");
            }
        }
        
    }
    
    @Test(description="Verify export JSON file feature")
    public void verifyExportJsonFile() throws Exception {
        machineList = homePage.getMachineList();
        hostNameList = homePage.getHostNameList();
        String hostName, jsonFileName;
        
        for (int i=0; i<machineList.size(); i++){
            hostName = hostNameList.get(i).getText();
            jsonFileName = hostName + "_test.json";
            homePage.openModal(machineList.get(i));
            driver.switchTo().activeElement();            
            currentModal = homePage.getCurrentModalInstance();
        
            if (! hostName.equals("#Unknown")){    
                Assert.assertTrue(currentModal.isExportJsonFileButtonExists());
                currentModal.clickExportJsonFileButton();
                homePage.waitForModalToBeClosed();
                driver.switchTo().activeElement();
                currentModal = homePage.getCurrentModalInstance();
                currentModal.enterJsonFileName(jsonFileName);
                currentModal.clickExportButton();
                homePage.waitForModalToBeClosed();
                
                String flashMessage = homePage.getFlashMessageField().getText();
                Assert.assertTrue(flashMessage.contains(jsonFileName));
                Assert.assertTrue(flashMessage.contains("successfully saved"));
            }
            else{
                Assert.assertTrue(! currentModal.isExportJsonFileButtonExists());
                currentModal.clickCloseButton();
                homePage.waitForModalToBeClosed();
            }
        }
    }
    
    @Test(description="Verify SSH access via Butterfly module")
    public void verifyOpenSSHButterfly() throws Exception {
        machineList = homePage.getMachineList();
        hostNameList = homePage.getHostNameList();
        String hostName;
        
        for (int i=0; i<machineList.size(); i++){
            hostName = hostNameList.get(i).getText();
            homePage.openModal(machineList.get(i));
            driver.switchTo().activeElement();            
            currentModal = homePage.getCurrentModalInstance();
        
            if (! hostName.equals("#Unknown")){    
                Assert.assertTrue(currentModal.isOpenSSHButtonExists());
                currentModal.clickOpenSSHButton();
                Assert.assertTrue(currentModal.isButterflyRunning());
            }
            else{
                Assert.assertTrue(! currentModal.isOpenSSHButtonExists());
                currentModal.clickCloseButton();
                homePage.waitForModalToBeClosed();
            }
        }
    }
    
    @Test(description="Verify Ajax automatically refreshes modal contents")
    public void verifyAjaxForModalContents() throws Exception{
        String lastUpdatedBeforeAjax, lastUpdatedAfterAjax;
        int secsAgoBeforeAjax = 0;
        int secsAgoAfterAjax = 0;
        Matcher m;
        
        restAPI.deleteMachine(TEST_IP);
        homePage.waitForAjaxToLoad();
        
        // Open modal(vm05)
        homePage.openModal(homePage.getMachineElementByHostname("vm05"));
        driver.switchTo().activeElement();            
        currentModal = homePage.getCurrentModalInstance();
        lastUpdatedBeforeAjax = currentModal.getLastUpdated();
        m = PATTERN_LAST_UPDATED.matcher(lastUpdatedBeforeAjax);
        if(m.find()){
            secsAgoBeforeAjax = Integer.parseInt(m.group(2));
        }
        
        // Close modal
        currentModal.clickCloseButton();
        homePage.waitForModalToBeClosed();
        
        // Open modal(vm05)
        homePage.openModal(homePage.getMachineElementByHostname("vm05"));
        driver.switchTo().activeElement();            
        currentModal = homePage.getCurrentModalInstance();
        lastUpdatedAfterAjax = currentModal.getLastUpdated();
        m = PATTERN_LAST_UPDATED.matcher(lastUpdatedAfterAjax);
        if(m.find()){
            secsAgoAfterAjax = Integer.parseInt(m.group(2));
        }
        
        // Close modal
        currentModal.clickCloseButton();
        homePage.waitForModalToBeClosed();
        
        int deltaSeconds = (secsAgoAfterAjax - secsAgoBeforeAjax) > 0 ? secsAgoBeforeAjax - secsAgoAfterAjax : secsAgoAfterAjax;
        Assert.assertTrue(deltaSeconds < 3, "difference between before/after was " + deltaSeconds + " seconds");
        
    }

}