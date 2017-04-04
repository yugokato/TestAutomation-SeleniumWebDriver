package framework.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import framework.driver.DriverInit;
import framework.pages.TopPage;
import io.restassured.response.Response;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class RestAPI extends RestClient {
    private WebDriver driver;
    private static final Logger logger = Logger.getLogger(RestAPI.class);
    private static final String BASE_URL = "http://localhost:5000/api/machines";
    
    public RestAPI(){
        this.driver = DriverInit.getDriver();
        logger.setLevel(Level.ALL);
    }
    
    public Response getMachineData(String hostname){
        String url = BASE_URL + "/" + hostname;
        Response response = requestGET(url);
        
        return response;
    }
    
    public Response registerMachine(String ipaddr, String username){
        String url = BASE_URL + "/add/" + ipaddr + ":" + username;
        Response response = requestPOST(url, null);
        logger.info(String.format("Added a machine(%s) via RESUful API", ipaddr));
        driver.navigate().refresh();

        return response;
    }
    
    public Response registerMachine(String ipaddr, String username, String password){
        String url = BASE_URL + "/add/" + ipaddr + ":" + username + ":" + password;
        Response response = requestPOST(url, null);
        logger.info(String.format("Added a machine(%s) via RESUful API", ipaddr));
        driver.navigate().refresh();

        return response;
    }
    
    public Response registerMachines(List<HashMap<String, String>> credentialsMapList){
        List<JSONObject> jsonObjList = new ArrayList<JSONObject>();
        ArrayList<String> addIPList = new ArrayList<>();
        
        for(HashMap<String, String> machine : credentialsMapList) {
            JSONObject obj = new JSONObject(machine);
            jsonObjList.add(obj);
            addIPList.add(machine.get("IP Address"));
        }

        JSONArray jsonData = new JSONArray(jsonObjList);
        String jsonDataStr = jsonData.toString();
        
        String url = BASE_URL + "/add";
        Response response = requestPOST(url, jsonDataStr);
        logger.info(String.format("Added machines(%s) via RESUful API", addIPList.toString()));
        driver.navigate().refresh();
        
        return response;
    }
    
    public Response deleteMachine(String ipaddr){
        String url = BASE_URL + "/delete/" + ipaddr;
        Response response = requestDELETE(url, null);
        logger.info(String.format("Deleted machines(%s) via RESUful API", ipaddr));
        driver.navigate().refresh();
   
        return response;
    }
    
    public Response deleteMachines(List<String> deleteIPList){
        Map<String, List<String>> data = new HashMap<>();
        data.put("IP Address", deleteIPList);
        
        JSONObject jsonData = new JSONObject(data);
        String jsonDataStr = jsonData.toString();
        
        String url = BASE_URL + "/delete";
        Response response = requestDELETE(url, jsonDataStr);
        logger.info(String.format("Deleted a machine(%s) via RESUful API", deleteIPList.toString()));
        driver.navigate().refresh();

        return response;
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
    }
    

}