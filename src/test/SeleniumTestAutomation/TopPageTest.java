package test.SeleniumTestAutomation;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import test.SeleniumTestAutomation.base.BaseTest;

import org.testng.Assert;

public class TopPageTest extends BaseTest {
    private final String TEST_IP = "172.30.0.11";
    private final String TEST_USERNAME = "ubuntu";
    private final String TEST_PASSWORD = "ubuntu";
    private final String TEST_HOSTNAME = "vm11";
    private final String TEST_OS_DIST = "ubuntu";
    List<WebElement> machineList;
    List<WebElement> hostNameList;
    List<WebElement> ipAddressList;
    List<WebElement> osDistributionImgNameList;
    
    @BeforeMethod
    public void beforeMethod() {
    	restAPI.deleteAllUnknownMachines();
    	restAPI.deleteMachine(TEST_IP);
    }
    
    @Test(description="Verify initial state of the top page")
    public void verifyTopPageBasic() {
    	restAPI.addMachine(TEST_IP, TEST_USERNAME);
    	
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
    
    @Test(description="Verify state changes after reachable")
    public void verifyTopPageAfterReachable() {
    	int numOfMachines;
    	String addedHostName, addedIpAddress, osDistributionImgName;
    	
    	restAPI.addMachine(TEST_IP, TEST_USERNAME, TEST_PASSWORD);
    	
    	// Check the initial #Unknown status
    	driver.navigate().refresh();
    	numOfMachines = topPage.getMachineList().size();
    	addedHostName = topPage.getHostNameList().get(0).getText();
    	addedIpAddress = topPage.getIpAddressList().get(0).getText();
    	osDistributionImgName = topPage.getOSDistributionImgNameList().get(0).getAttribute("src");
    	
    	Assert.assertTrue(addedHostName.equals("#Unknown"), addedHostName);
    	Assert.assertTrue(addedIpAddress.equals(TEST_IP), addedIpAddress);
    	Assert.assertTrue(osDistributionImgName.contains("other"), osDistributionImgName);
    
    	// Wait for 40 seconds until SSH access starts
    	try{
    		Thread.sleep(40000);
    	}catch(InterruptedException e){}
    	
    	
    	// Check the new status after reachable 
    	driver.navigate().refresh();
    	addedHostName = topPage.getHostNameList().get(numOfMachines-1).getText();
    	addedIpAddress = topPage.getIpAddressList().get(numOfMachines-1).getText();
    	osDistributionImgName = topPage.getOSDistributionImgNameList().get(numOfMachines-1).getAttribute("src");
    	Assert.assertTrue(addedHostName.equals(TEST_HOSTNAME), addedHostName);
    	Assert.assertTrue(addedIpAddress.equals(TEST_IP), addedIpAddress);
    	Assert.assertTrue(osDistributionImgName.contains(TEST_OS_DIST));
    }
    
    

    /*
    @Test(description="")
    public void verifyTopPageWhenUnreahable() throws Exception {
    	String cmd = "docker --host=tcp://192.168.99.100:2376 stop vm01";
	    String line;
	    try{
		    Process proc = Runtime.getRuntime().exec(cmd);
		    		    BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		    while ((line = is.readLine()) != null) {
		    	System.out.println(line);
		    }
	    }
	    catch(IOException e){
	    	System.out.println(e);
	    	
	    }
    }
    */
 
}
