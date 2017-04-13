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
    List<WebElement> machineList;
    List<WebElement> hostNameList;
    List<WebElement> ipAddressList;
    List<WebElement> osDistributionImgNameList;
    
    @BeforeMethod
    public void beforeMethod(Method method) {
        System.out.println(method.getName());
        restAPI.deleteAllUnknownMachines();
        restAPI.registerMachine(TEST_IP, TEST_USERNAME);
    }
    
    @Test(description="Verify modal contents in the top page are valid based on machine's status")
    public void verifyModalContents() throws Exception {
        machineList = topPage.getMachineList();
        hostNameList = topPage.getHostNameList();
        ipAddressList = topPage.getIpAddressList();
        osDistributionImgNameList = topPage.getOSDistributionImgNameList();
        String lastUpdated, hostName, ipAddress, status, statusImgName, osDistribution; 
        String release, macAddress, uptime, cpuLoadAvg, memoryUsage, diskUsage;
        Map<String, String> modalContents;
        
        Pattern p = Pattern.compile("^(.+)\\s\\(([0-9]+)");
        
        for (int i=0; i<machineList.size(); i++){
            topPage.openModal(machineList.get(i));
            currentModal = topPage.getCurrentModalInstance();

            driver.switchTo().activeElement();
            modalContents = currentModal.getModalContents();
            
            // Close modal
            currentModal.clickCloseButton();
            topPage.waitForModalToBeClosed();
            
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

            Matcher m = p.matcher(lastUpdated);
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
                Assert.assertEquals(osDistribution, "N.A");
                Assert.assertTrue(osDistributionImgNameList.get(i).getAttribute("src").contains("other"));
                Assert.assertEquals(release, "N.A");
                Assert.assertEquals(macAddress, "N.A");
                Assert.assertEquals(uptime, "N.A");
                Assert.assertEquals(cpuLoadAvg, "N.A");
                Assert.assertEquals(memoryUsage, "N.A");
                Assert.assertEquals(diskUsage, "N.A");
                Assert.assertTrue(Integer.parseInt(lastUpdated) >= 0);
            }
            
            else {
                Assert.assertTrue(Integer.parseInt(lastUpdated) < 90);
                Assert.assertEquals(hostNameList.get(i).getText(), hostName);
                Assert.assertEquals(ipAddressList.get(i).getText(), ipAddress);
                Assert.assertTrue(status.contains("OK"));
                Assert.assertEquals(statusImgName, "status_ok.png");
                Assert.assertTrue(osDistributionImgNameList.get(i).getAttribute("src").contains(osDistribution.toLowerCase()));
                Assert.assertNotEquals(release, "N.A");
                Assert.assertNotEquals(macAddress, "N.A");
                Assert.assertNotEquals(uptime, "N.A");
                Assert.assertNotEquals(cpuLoadAvg, "N.A");
                Assert.assertNotEquals(memoryUsage, "N.A");
                Assert.assertNotEquals(diskUsage, "N.A");
            }
        }
        
    }
    
    @Test(description="Verify export JSON file feature")
    public void verifyExportJsonFile() throws Exception {
        machineList = topPage.getMachineList();
        hostNameList = topPage.getHostNameList();
        String hostName, jsonFileName;
        
        for (int i=0; i<machineList.size(); i++){
            hostName = hostNameList.get(i).getText();
            jsonFileName = hostName + "_test.json";
            topPage.openModal(machineList.get(i));
            driver.switchTo().activeElement();            
            currentModal = topPage.getCurrentModalInstance();
        
            if (! hostName.equals("#Unknown")){    
                Assert.assertTrue(currentModal.isExportJsonFileButtonExists());
                currentModal.clickExportJsonFileButton();
                topPage.waitForModalToBeClosed();
                driver.switchTo().activeElement();
                currentModal = topPage.getCurrentModalInstance();
                currentModal.enterJsonFileName(jsonFileName);
                currentModal.clickExportButton();
                topPage.waitForModalToBeClosed();
                
                String flashMessage = topPage.getFlashMessageField().getText();
                Assert.assertTrue(flashMessage.contains(jsonFileName));
                Assert.assertTrue(flashMessage.contains("successfully saved"));
            }
            else{
                Assert.assertTrue(! currentModal.isExportJsonFileButtonExists());
                currentModal.clickCloseButton();
                topPage.waitForModalToBeClosed();
            }
        }
    }
    
    @Test(description="Verify SSH access via Butterfly module")
    public void verifyOpenSSHButterfly() throws Exception {
        machineList = topPage.getMachineList();
        hostNameList = topPage.getHostNameList();
        String hostName;
        
        for (int i=0; i<machineList.size(); i++){
            hostName = hostNameList.get(i).getText();
            topPage.openModal(machineList.get(i));
            driver.switchTo().activeElement();            
            currentModal = topPage.getCurrentModalInstance();
        
            if (! hostName.equals("#Unknown")){    
                Assert.assertTrue(currentModal.isOpenSSHButtonExists());
                currentModal.clickOpenSSHButton();
                Assert.assertTrue(currentModal.isButterflyRunning());
            }
            else{
                Assert.assertTrue(! currentModal.isOpenSSHButtonExists());
                currentModal.clickCloseButton();
                topPage.waitForModalToBeClosed();
            }
        }
    }

}