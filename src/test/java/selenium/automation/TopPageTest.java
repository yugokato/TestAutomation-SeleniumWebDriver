package selenium.automation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import framework.docker.DockerManager;
import framework.pages.modal.Modal;

import org.testng.Assert;

import selenium.automation.base.BaseTest;


public class TopPageTest extends BaseTest {
	private Modal currentModal;
    private final String TEST_IP = "172.30.0.11";
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
    public void beforeMethod() {
    	restAPI.deleteAllUnknownMachines();
    	restAPI.deleteMachine(TEST_IP);
    }
    
    @AfterMethod
    public void afterMethod() {
    	restAPI.deleteMachine(TEST_IP);
    }
    
    @Test(description="Verify initial state of the top page")
    public void verifyTopPageBasic() throws Exception {
    	// register a new machine whose status is #Unknown
    	restAPI.registerMachine(TEST_IP, TEST_USERNAME);
    	
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
    	testData.put("#Unknown", new String[] {"", "other"});
 
    	
        machineList = topPage.getMachineList();
        hostNameList = topPage.getHostNameList();
        ipAddressList = topPage.getIpAddressList();
        osDistributionImgNameList = topPage.getOSDistributionImgNameList();

        String hostName, ipAddr, osDistImgName;
        for (int i=0; i<machineList.size(); i++){
        	hostName = hostNameList.get(i).getText();
        	ipAddr = ipAddressList.get(i).getText();
        	osDistImgName = osDistributionImgNameList.get(i).getAttribute("src");
        	if (! hostName.equals("#Unknown")){
	        	Assert.assertTrue(ipAddr.equals(testData.get(hostName)[0]));
        	}
        	Assert.assertTrue(osDistImgName.contains(testData.get(hostName)[1]));
        }
    }
    
    @Test(description="Verify state changes after new machine becomes reachable")
    public void verifyTopPageUnknownBecomesReachable() throws Exception {
    	int numOfMachines;
    	String addedHostName, addedIpAddress, osDistributionImgName;
    	String testContainerID, testContainerIP;
    	
    	// Create a new container for test
    	Map<String, String> testConfigMap = new HashMap<>();
    	testConfigMap.put("IMAGE", "yugokato/ubuntu_template");
    	testConfigMap.put("HOSTNAME", TEST_HOSTNAME);
    	testConfigMap.put("CONTAINER_NAME", TEST_CONTAINER_NAME);
    	testConfigMap.put("NETWORK_NAME", TEST_DOCKER_NETWORK);
    	
    	Map<String, String> testContainerInfo = DockerManager.createAndStartContainer(testConfigMap);
    	testContainerID = testContainerInfo.get("ID");
    	testContainerIP = testContainerInfo.get("IP_ADDRESS");
    
    	// Register the new container for test
    	restAPI.registerMachine(testContainerIP, TEST_USERNAME, TEST_PASSWORD);
    	
    	// Check the initial #Unknown status after registration
    	driver.navigate().refresh();
    	numOfMachines = topPage.getMachineList().size();
    	addedHostName = topPage.getHostNameList().get(0).getText();
    	addedIpAddress = topPage.getIpAddressList().get(0).getText();
    	osDistributionImgName = topPage.getOSDistributionImgNameList().get(0).getAttribute("src");
    	
    	Assert.assertTrue(addedHostName.equals("#Unknown"), addedHostName);
    	Assert.assertTrue(addedIpAddress.equals(testContainerIP), addedIpAddress);
    	Assert.assertTrue(osDistributionImgName.contains("other"), osDistributionImgName);
    
    	// Wait for 40 seconds until SSH access starts and status changes reachable
    	try{
    		Thread.sleep(40000);
    	}catch(InterruptedException e){}
    	
    	
    	// Check the new status after reachable 
    	driver.navigate().refresh();
    	addedHostName = topPage.getHostNameList().get(numOfMachines-1).getText();
    	addedIpAddress = topPage.getIpAddressList().get(numOfMachines-1).getText();
    	osDistributionImgName = topPage.getOSDistributionImgNameList().get(numOfMachines-1).getAttribute("src");
    	Assert.assertTrue(addedHostName.equals(TEST_HOSTNAME), addedHostName);
    	Assert.assertTrue(addedIpAddress.equals(testContainerIP), addedIpAddress);
    	Assert.assertTrue(osDistributionImgName.contains(TEST_OS_DIST));
    	
    	// Clean the test container
    	docker.killContainer(testContainerID);
    	docker.removeContainer(testContainerID);   	
    }
    

    @Test(description="Verify top page status changes when the existing machine becomes unreachable")
    public void verifyTopPageOkBecomesUnreahable() throws Exception{
    	
    	// stop one of the existing containers 
    	docker.killContainer(TEST_CONTAINER_TO_DELETE);  // vm01
    	
    	// Wait for 90 seconds
    	Thread.sleep(90000);
        driver.navigate().refresh();
        
        // check status icon
        machineList = topPage.getMachineList();
        hostNameList = topPage.getHostNameList();
        osDistributionImgNameList = topPage.getOSDistributionImgNameList();

        String hostName, osDistImgName;
        Map<String, String> modalContents;
        for (int i=0; i<machineList.size(); i++){
        	hostName = hostNameList.get(i).getText();
        	if (hostName.equals(TEST_CONTAINER_TO_DELETE)){
        		osDistImgName = osDistributionImgNameList.get(i).getAttribute("src");
        		Assert.assertTrue(osDistImgName.contains("unreachable"));
        		
        		// check modal contents
        		topPage.openModal(machineList.get(i));
        		currentModal = topPage.getCurrentModalInstance();
        		modalContents = currentModal.getModalContents();
        		Assert.assertTrue(modalContents.get("STATUS").contains("Unreachable"));
        		Assert.assertTrue(modalContents.get("STATUS_IMG_NAME").equals("status_unreachable.png"));
        		currentModal.clickCloseButton();
        		break;
        	}
        }
        
        // re-start the container
        docker.startContainer(TEST_CONTAINER_TO_DELETE);
        
    	// Wait for 40 seconds
    	Thread.sleep(40000);
        driver.navigate().refresh();
        
        // Check all machines' status = ok
        osDistributionImgNameList = topPage.getOSDistributionImgNameList();
        for (int i=0; i<osDistributionImgNameList.size(); i++){
        	Assert.assertTrue(! osDistributionImgNameList.get(i).getAttribute("src").contains("unreachable"));
        }
	    
    }

}