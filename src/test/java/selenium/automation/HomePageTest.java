package selenium.automation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import framework.docker.DockerManager;
import framework.pages.modal.Modal;

import org.testng.Assert;

import selenium.automation.base.BaseTest;


public class HomePageTest extends BaseTest {
    private Modal currentModal;
    private final String TEST_IP = "172.30.0.11";
    private final String TEST_IP2 = "172.30.0.21";
    private final String TEST_IP3 = "172.30.0.111";
    private final String TEST_USERNAME = "ubuntu";
    private final String TEST_PASSWORD = "ubuntu";
    private final String TEST_OS_DIST = "ubuntu";
    private final String TEST_HOSTNAME = "vm99_test";
    private final String TEST_CONTAINER_NAME = "vm99_test";
    private final String TEST_CONTAINER_TO_DELETE = "vm01";
    private final String TEST_DOCKER_NETWORK = "mgmtappforlinuxmachines_mynetwork";
    private List<WebElement> machineList;
    private List<WebElement> hostNameList;
    private List<WebElement> ipAddressList;
    private List<WebElement> osDistributionImgNameList;
    
    @BeforeMethod
    public void beforeMethod(Method method) {
        System.out.println(method.getName());
        restAPI.deleteAllUnknownMachines();
        homePage.waitForAjaxToLoad();
    }
    
    @AfterMethod
    public void afterMethod() {
        restAPI.deleteMachine(TEST_IP);
        restAPI.deleteMachine(TEST_IP2);
        restAPI.deleteMachine(TEST_IP3);
    }
    
    @Test(description="Verify initial state of the home page", priority=0)
    public void verifyHomePageBasic() throws Exception {
        // register a new machine whose status is #Unknown
        restAPI.registerMachine(TEST_IP, TEST_USERNAME);
        homePage.waitForAjaxToLoad();
        
        // hard-coded test data
        Map<String, String[]> testData = new HashMap<String, String[]>();
        testData.put("vm01", new String[] {"172.30.0.1", "ubuntu"});
        testData.put("vm02", new String[] {"172.30.0.2", "ubuntu"});
        testData.put("vm03", new String[] {"172.30.0.3", "centos"});
        testData.put("vm04", new String[] {"172.30.0.4", "debian"});
        testData.put("vm05", new String[] {"172.30.0.5", "centos"});
        testData.put("vm06", new String[] {"172.30.0.6", "ubuntu"});
        testData.put("vm07", new String[] {"172.30.0.7", "debian"});
        testData.put("vm08", new String[] {"172.30.0.8", "ubuntu"});
        testData.put("vm09", new String[] {"172.30.0.9", "ubuntu"});
        testData.put("vm10", new String[] {"172.30.0.10", "centos"});
        testData.put("#Unknown", new String[] {TEST_IP, "other"});
 
        machineList = homePage.getMachineList();
        hostNameList = homePage.getHostNameList();
        ipAddressList = homePage.getIpAddressList();
        osDistributionImgNameList = homePage.getOSDistributionImgNameList();

        Assert.assertEquals(machineList.size(), testData.size());
        
        String hostName, ipAddr, osDistImgName;
        for (int i=0; i<machineList.size(); i++){
            hostName = hostNameList.get(i).getText();
            ipAddr = ipAddressList.get(i).getText();
            osDistImgName = osDistributionImgNameList.get(i).getAttribute("src");
            Assert.assertEquals(ipAddr, testData.get(hostName)[0]);
            Assert.assertTrue(osDistImgName.contains(testData.get(hostName)[1]));
        }
    }   
    
    @Test(description="Verify home page icon changes after new machine becomes reachable")
    public void verifyHomePageUnknownBecomesReachable() throws Exception {
        String osDistImgName, testContainerID, testContainerIP;
        
        // Create a new container for test
        Map<String, String> testConfigMap = new HashMap<>();
        testConfigMap.put("IMAGE", "yugokato/ubuntu_template");
        testConfigMap.put("HOSTNAME", TEST_HOSTNAME);
        testConfigMap.put("CONTAINER_NAME", TEST_CONTAINER_NAME);
        testConfigMap.put("NETWORK_NAME", TEST_DOCKER_NETWORK);
        
        // Get created container's ID/IP Address
        Map<String, String> testContainerInfo = DockerManager.createAndStartContainer(testConfigMap);
        testContainerID = testContainerInfo.get("ID");
        testContainerIP = testContainerInfo.get("IP_ADDRESS");
    
        // Register the new container for test
        restAPI.registerMachine(testContainerIP, TEST_USERNAME, TEST_PASSWORD);
        homePage.waitForAjaxToLoad();
        
        // Check the initial #Unknown status after registration
        osDistImgName = homePage.getOsDistImgFieldByHostname("#Unknown").getAttribute("src");
        Assert.assertTrue(homePage.getMachineElementByHostname("#Unknown").isDisplayed());
        Assert.assertTrue(homePage.getIpAddressField(testContainerIP).isDisplayed());
        Assert.assertTrue(osDistImgName.contains("other"));
        
        // Wait for up to 60 seconds until SSH access starts and status changes reachable
        for (int i=0; i<60; i+=10){
            //driver.navigate().refresh();
            try{
                homePage.getMachineElementByHostname("#Unknown");
                Thread.sleep(10000);
            }catch(NoSuchElementException e){
                break;
            }                
        }
        
        // Check the new status after reachable 
        osDistImgName = homePage.getOsDistImgFieldByHostname(TEST_HOSTNAME).getAttribute("src");
        Assert.assertTrue(homePage.getMachineElementByHostname(TEST_HOSTNAME).isDisplayed());
        Assert.assertTrue(osDistImgName.contains(TEST_OS_DIST));
        
        // Clean the test container
        docker.killContainer(testContainerID);
        docker.removeContainer(testContainerID);       
    }

    @Test(description="Verify home page icon changes when the existing machine becomes unreachable")
    public void verifyHomePageOkBecomesUnreahable() throws Exception{
        String osDistImgName;
        
        // stop one of the existing containers to emulate an incident
        docker.killContainer(TEST_CONTAINER_TO_DELETE);  // vm01
        
        // Wait for up to 90 seconds until the machine icon changes
        homePage.waitForMachineStatusToBeUnreachable("172.30.0.1");
        
        // check the machine status = unreachable and the icon reflects an alarm
        osDistImgName = homePage.getOsDistImgFieldByHostname(TEST_CONTAINER_TO_DELETE).getAttribute("src");
        Assert.assertTrue(osDistImgName.contains("unreachable"));
                
        // check machine status and icon in modal = unreachable
        Map<String, String> modalContents;
        homePage.openModal(homePage.getMachineElementByHostname(TEST_CONTAINER_TO_DELETE));
        currentModal = homePage.getCurrentModalInstance();
        modalContents = currentModal.getModalContents();
        Assert.assertTrue(modalContents.get("STATUS").contains("Unreachable"));
        Assert.assertEquals(modalContents.get("STATUS_IMG_NAME"), "status_unreachable.png");
        Assert.assertFalse(currentModal.isOpenSSHButtonExists());
        currentModal.clickCloseButton();
        
        // re-start the container
        docker.startContainer(TEST_CONTAINER_TO_DELETE);
        
        // Wait until the machine status becomes OK
        homePage.waitForMachineStatusToBeOK("172.30.0.1");
        
        // Check the machine status = ok
        osDistImgName = homePage.getOsDistImgFieldByHostname(TEST_CONTAINER_TO_DELETE).getAttribute("src");
        Assert.assertTrue(osDistImgName.contains("ubuntu.png"));
    }

    @Test(description="Verify home page elements are dinamically updated by Ajax")
    public void verifyHomePageAjax() throws Exception{
        restAPI.registerMachine(TEST_IP, TEST_USERNAME);
        homePage.waitForAjaxToLoad();
        
        restAPI.registerMachine(TEST_IP2, TEST_USERNAME);
        homePage.waitForAjaxToLoad();
        
        ArrayList<String> ipAddressList = homePage.getIpAddressListStr();
        Assert.assertTrue(ipAddressList.contains(TEST_IP));
        Assert.assertTrue(ipAddressList.contains(TEST_IP2));
        
    }
    
    @Test(description="Verify elements are sorted by hostname and ip address correctly")
    public void verifyHomePageSorting() throws Exception{
        restAPI.registerMachine(TEST_IP, TEST_USERNAME);
        homePage.waitForAjaxToLoad();
        
        restAPI.registerMachine(TEST_IP2, TEST_USERNAME);
        homePage.waitForAjaxToLoad();
        
        restAPI.registerMachine(TEST_IP3, TEST_USERNAME);
        homePage.waitForAjaxToLoad();
        
        // Check Ajax side's sorting mechanism
        ArrayList<String> ipAddressList = homePage.getIpAddressListStr();
        Assert.assertEquals(ipAddressList.get(0), TEST_IP);
        Assert.assertEquals(ipAddressList.get(1), TEST_IP2);
        Assert.assertEquals(ipAddressList.get(2), TEST_IP3);
        
        // Check application side's sorting mechanism
        driver.navigate().refresh();
        ipAddressList = homePage.getIpAddressListStr();
        Assert.assertEquals(ipAddressList.get(0), TEST_IP);
        Assert.assertEquals(ipAddressList.get(1), TEST_IP2);
        Assert.assertEquals(ipAddressList.get(2), TEST_IP3);
        
    }
}