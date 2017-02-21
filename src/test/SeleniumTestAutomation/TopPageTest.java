package test.SeleniumTestAutomation;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import test.SeleniumTestAutomation.base.BaseTest;

import org.testng.Assert;

public class TopPageTest extends BaseTest {

    @Test(description="")
    public void verifyTopPage() {
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
 
    	
        List<WebElement> machineList = topPage.getMachineList();
        List<WebElement> hostNameList = topPage.getHostNameList();
        List<WebElement> ipAddressList = topPage.getIpAddressList();
        List<WebElement> osDistributionImgNameList = topPage.getOSDistributionImgNameList();

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
