package SeleniumTestAutomation;

import pages.TopPage;
import pages.modal.Modal;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.Assert;

public class ModalsTest {
    private WebDriver driver;
    private JavascriptExecutor jse;
    private TopPage topPage;
    private Modal currentModal;


    @BeforeSuite(alwaysRun=true)
    public void beforeSuite() throws Exception {
    	System.setProperty("webdriver.firefox.marionette","lib");
    	driver = new FirefoxDriver();
        topPage = new TopPage(driver);
        jse = (JavascriptExecutor)driver;
    }
    
    @BeforeMethod
    public void beforeMethod() throws Exception {
        driver.get("http://localhost:5000");
        driver.manage().window().maximize();
        jse.executeScript("scroll(0, 250);");
    }

    @AfterSuite(alwaysRun=true)
    public void afterSuite() {
        driver.quit();
    }
    
    @Test(description="Verify modal contents in the top page are valid based on machine's status")
    public void verifyModalContents() throws Exception {
        List<WebElement> machineList = topPage.getMachineList();
        List<WebElement> hostNameList = topPage.getHostNameList();
        List<WebElement> ipAddressList = topPage.getIpAddressList();
        List<WebElement> osDistributionImgNameList = topPage.getOSDistributionImgNameList();
    	String lastUpdated, hostName, ipAddress, status, statusImgName, osDistribution; 
    	String release, macAddress, uptime, cpuLoadAvg, memoryUsage, diskUsage;
    	Map<String, String> modalContents;
    	
    	Pattern p = Pattern.compile("^(.+)\\s\\(([0-9]+)");
        
        for (int i=0; i<machineList.size(); i++){
			topPage.openModal(machineList.get(i));
			currentModal = topPage.getCurrentModalInstance();
			
            driver.switchTo().activeElement();
            modalContents = currentModal.getModalContents();
            
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
            	Assert.assertTrue(hostNameList.get(i).getText().equals(hostName));
            	Assert.assertTrue(ipAddressList.get(i).getText().equals(ipAddress));
            	if (status.contains("Unreachable")){
            		Assert.assertTrue(statusImgName.equals("status_unreachable.png"));
            	}
            	else if (modalContents.get("STATUS").contains("Unknown")){
            		Assert.assertTrue(statusImgName.equals("status_unknown.png"));
            	}
            	Assert.assertTrue(osDistribution.equals("N.A"));
            	Assert.assertTrue(osDistributionImgNameList.get(i).getAttribute("src").contains("other"));
            	Assert.assertTrue(release.equals("N.A"));
            	Assert.assertTrue(macAddress.equals("N.A"));
            	Assert.assertTrue(uptime.equals("N.A"));
            	Assert.assertTrue(cpuLoadAvg.equals("N.A"));
            	Assert.assertTrue(memoryUsage.equals("N.A"));
            	Assert.assertTrue(diskUsage.equals("N.A"));
            	Assert.assertTrue(Integer.parseInt(lastUpdated) > 0);
            }
            
            else {
            	Assert.assertTrue(Integer.parseInt(lastUpdated) < 90);
            	Assert.assertTrue(hostNameList.get(i).getText().equals(hostName));
            	Assert.assertTrue(ipAddressList.get(i).getText().equals(ipAddress));
            	Assert.assertTrue(status.contains("OK"));
            	Assert.assertTrue(statusImgName.equals("status_ok.png"));
            	Assert.assertTrue(osDistributionImgNameList.get(i).getAttribute("src").contains(osDistribution.toLowerCase()));
            	Assert.assertTrue(! release.equals("N.A"));
            	Assert.assertTrue(! macAddress.equals("N.A"));
            	Assert.assertTrue(! uptime.equals("N.A"));
            	Assert.assertTrue(! cpuLoadAvg.equals("N.A"));
            	Assert.assertTrue(! memoryUsage.equals("N.A"));
            	Assert.assertTrue(! diskUsage.equals("N.A"));
            }
            
            // Close modal
            currentModal.clickCloseButton();
        }
        
    }
    
    @Test(description="Verify export JSON file feature")
    public void verifyExportJsonFile() throws Exception {
    	List<WebElement> machineList = topPage.getMachineList();
    	List<WebElement> hostNameList = topPage.getHostNameList();
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
	    		driver.switchTo().activeElement();
	    		currentModal = topPage.getCurrentModalInstance();
	    		currentModal.enterJsonFileName(jsonFileName);
	    		currentModal.clickExportButton();
	    		
	    		String flashMessage = topPage.getFlashMessageField().getText();
	    		Assert.assertTrue(flashMessage.contains(jsonFileName));
	    		Assert.assertTrue(flashMessage.contains("successfully saved"));
    		}
			else{
				Assert.assertTrue(! currentModal.isExportJsonFileButtonExists());
				currentModal.clickCloseButton();
			}
    	}
    }
    
    @Test(description="Verify SSH access via Butterfly module")
    public void verifyOpenSSHButterfly() throws Exception {
    	List<WebElement> machineList = topPage.getMachineList();
    	List<WebElement> hostNameList = topPage.getHostNameList();
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
			}
		}
    }
    


}
