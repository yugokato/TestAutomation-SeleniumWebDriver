package test.SeleniumTestAutomation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.Version;

import framework.pages.TopPage;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.Assert;

public class TopPageTest {
    private WebDriver driver;
    private JavascriptExecutor jse;
    private TopPage topPage;

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
    
    @Test(description="")
    public void verifyTopPage() throws Exception {
    	Map<String, String[]> testData = new HashMap();
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

    
    @Test(description="")
    public void verifyTopPageWhenUnreahable() throws Exception {
    	//final DockerClient docker = DefaultDockerClient.fromEnv().build();
    	//final DockerClient docker = new DefaultDockerClient("tcp://192.168.99.100:2376");
    	//docker.killContainer("vm02");
    	
    	//docker.close();
    	
    	// docker stop vm01
    	//String[] cmd = { "/bin/sh", "-c", "docker --host=tcp://192.168.99.100:2376 stop vm01" };
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
 
}
