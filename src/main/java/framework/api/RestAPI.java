package framework.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import framework.driver.DriverInit;
import framework.pages.TopPage;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class RestAPI {
	private WebDriver driver;
	private static final Logger logger = Logger.getLogger(RestAPI.class);
	
	
	public RestAPI(){
		this.driver = DriverInit.getDriver();
		logger.setLevel(Level.ALL);
	}
	
	public String addMachine(String ipaddr, String username){
		String url = "curl -H 'Content-Type: application/json' -X 'POST' http://localhost:5000/api/machines/add/" + ipaddr + ":" + username;
		StringBuffer output = new StringBuffer();
		
	    String[] cmd = { "/bin/sh", "-c", url };
	    String line;
	    try{
		    Process proc = Runtime.getRuntime().exec(cmd);
		    BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		    while ((line = is.readLine()) != null) {
		    	output.append(line.trim());
		    }

	    }
	    catch(IOException e){
	    	System.out.println(e);
	    }
	    
	    logger.info(String.format("Added a machine(%s) via RESUful API: " + output.toString(), ipaddr));

	    driver.navigate().refresh();
	    return output.toString();
   
	}
	
	public String addMachine(String ipaddr, String username, String password){
		String url = "curl -H 'Content-Type: application/json' -X 'POST' http://localhost:5000/api/machines/add/" + ipaddr + ":" + username + ":" + password;
		StringBuffer output = new StringBuffer();
		
	    String[] cmd = { "/bin/sh", "-c", url };
	    String line;
	    try{
		    Process proc = Runtime.getRuntime().exec(cmd);
		    BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		    while ((line = is.readLine()) != null) {
		    	output.append(line.trim());
		    }

	    }
	    catch(IOException e){
	    	System.out.println(e);
	    }
	    
	    logger.info(String.format("Added a machine(%s) via RESUful API: " + output.toString(), ipaddr));
	    
	    driver.navigate().refresh();
	    return output.toString();
   
	}
	
	public String deleteMachine(String ipaddr){
		String url = "curl -H 'Content-Type: application/json' -X 'DELETE' http://localhost:5000/api/machines/delete/" + ipaddr;
		StringBuffer output = new StringBuffer();
		
	    String[] cmd = { "/bin/sh", "-c", url };
	    String line;
	    try{
		    Process proc = Runtime.getRuntime().exec(cmd);
		    BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		    while ((line = is.readLine()) != null) {
		    	output.append(line.trim());
		    }

	    }
	    catch(IOException e){
	    	System.out.println(e);
	    }
	    
	    logger.info(String.format("Deleted machines(%s) via RESUful API: " + output.toString(), ipaddr));

	    driver.navigate().refresh();
	    return output.toString();
   
	}
	
	public void deleteAllUnknownMachines(){
		// Delete all machines whose hostname is #Unknown
		TopPage topPage = new TopPage();
        String deleteIPs = "";
        List<WebElement> hostNameList = topPage.getHostNameList();
        List<WebElement> ipAddrList = topPage.getIpAddressList();
        
        boolean hasUnknown = false;
        for (int i=0; i<hostNameList.size(); i++){
        	if (hostNameList.get(i).getText().contains("#Unknown")){
        		hasUnknown = true;
        		deleteIPs += ipAddrList.get(i).getText() + ",";
        	}
        }
        
        if (hasUnknown){
        	deleteMachine(deleteIPs.substring(0, deleteIPs.length()-1));
        }
        driver.navigate().refresh();
	}
}