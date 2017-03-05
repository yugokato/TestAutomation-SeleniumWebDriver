package framework.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import framework.driver.DriverInit;
import framework.pages.TopPage;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class RestAPI {
	private WebDriver driver;
	private static final Logger logger = Logger.getLogger(RestAPI.class);
	
	
	public RestAPI(){
		this.driver = DriverInit.getDriver();
		logger.setLevel(Level.ALL);
	}
	
	public String addMachine(String ipaddr, String username){
		String url = "curl -H 'Content-Type: application/json' -X 'POST' http://localhost:5000/api/machines/add/" + ipaddr + ":" + username;
		String result = requestJson(url);
	    logger.info(String.format("Added a machine(%s) via RESUful API - result: " + result, ipaddr));
	    
	    return result;
   
	}
	
	public String addMachine(String ipaddr, String username, String password){
		String url = "curl -H 'Content-Type: application/json' -X 'POST' http://localhost:5000/api/machines/add/" + ipaddr + ":" + username + ":" + password;
		String result = requestJson(url);
	    logger.info(String.format("Added a machine(%s) via RESUful API - result: " + result, ipaddr));
   
	    return result;
	}
	
	public String addMachines(List<HashMap<String, String>> credentialsMapList){
		List<JSONObject> jsonObjList = new ArrayList<JSONObject>();
		ArrayList<String> addIPList = new ArrayList<>();
		
		for(HashMap<String, String> machine : credentialsMapList) {
			addIPList.add(machine.get("IP Address"));
		    JSONObject obj = new JSONObject(machine);
		    jsonObjList.add(obj);
		}

		JSONArray jsonData = new JSONArray(jsonObjList);
		
		String url = "curl -H 'Content-Type: application/json' -X 'POST' http://localhost:5000/api/machines/add -d " + "'" + jsonData.toString() + "'";
		String result = requestJson(url);
	    logger.info(String.format("Added machines(%s) via RESUful API - result: " + result, addIPList.toString()));

	    return result;
	}
	
	public String deleteMachine(String ipaddr){
		String url = "curl -H 'Content-Type: application/json' -X 'DELETE' http://localhost:5000/api/machines/delete/" + ipaddr;
		String result = requestJson(url);
	    logger.info(String.format("Deleted machines(%s) via RESUful API - result: " + result, ipaddr));
   
	    return result;
	}
	
	public String deleteMachines(List<String> deleteIPList){
		Map<String, List<String>> data = new HashMap<>();
	    data.put("IP Address", deleteIPList);
	    
	    JSONObject jsonData = new JSONObject(data);
		
		String url = "curl -H 'Content-Type: application/json' -X 'DELETE' http://localhost:5000/api/machines/delete -d " + "'" + jsonData.toString() + "'";
		String result = requestJson(url);
	    logger.info(String.format("Deleted a machine(%s) via RESUful API - result: " + result, deleteIPList.toString()));

	    return result;
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
	
	private String requestJson (String url){
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
	    
	    driver.navigate().refresh();
	    return output.toString();
	}
}