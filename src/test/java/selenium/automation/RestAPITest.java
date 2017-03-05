package selenium.automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import selenium.automation.base.BaseTest;

public class RestAPITest extends BaseTest {
		private final String VALID_IP_1 = "1.1.1.1";
		private final String VALID_IP_2 = "2.2.2.2";
		private final String VALID_IP_3 = "3.3.3.3";
		private final String VALID_USERNAME = "ubuntu";
		private final String VALID_PASSWORD = "ubuntu";
		private final String INVALID_IP = "1.1.1";
		private final String INVALID_USERNAME = "%@*!";
		private final String INVALID_PASSWORD = "asdf.asdf";
		
		
		@BeforeMethod
	    public void beforeMethod() {
	    	restAPI.deleteAllUnknownMachines();
	    }

	    @Test(description = "Verify register machines feature via RestfulAPI - succeed")
	    public void verifyRegisterMachinesViaAPIBasic() throws Exception{
	    	String result;
	    	
	    	// test-1
	    	result = restAPI.addMachine(VALID_IP_1, VALID_USERNAME, VALID_PASSWORD);
	    	Assert.assertTrue(result.contains("true"));	
	    	
	    	// test-2(no password)
	    	result = restAPI.addMachine(VALID_IP_2, VALID_USERNAME);
	    	Assert.assertTrue(result.contains("\"success\": true"));	
	    }
	    
	    @Test(description = "Verify bulk register machines feature via RestfulAPI - succeed")
	    public void verifyBulkRegisterMachinesViaAPIBasic() throws Exception{
			HashMap<String, String> machine1 = new HashMap<>();
			HashMap<String, String> machine2 = new HashMap<>();;
			HashMap<String, String> machine3 = new HashMap<>();;
			
			machine1.put("IP Address", VALID_IP_1);
			machine1.put("Username", VALID_USERNAME);
			machine1.put("Password", VALID_PASSWORD);
			machine2.put("IP Address", VALID_IP_2);
			machine2.put("Username", VALID_USERNAME);
			machine2.put("Password", VALID_PASSWORD);
			machine3.put("IP Address", VALID_IP_3);
			machine3.put("Username", VALID_USERNAME);
			// no password for machine 3
			
			ArrayList<HashMap<String, String>> credentialsMapList = new ArrayList<>();
			credentialsMapList.add(machine1);
			credentialsMapList.add(machine2);
			credentialsMapList.add(machine3);
			
	    	String result = restAPI.addMachines(credentialsMapList);
	    	Assert.assertTrue(result.contains("\"success\": true"));
	    	
	    }
	    
	    @Test(description = "Verify register machines feature via RestfulAPI - Invalid parameters")
	    public void verifyRegisterMachinesViaAPIInvalid() throws Exception{
	    	String result;
	    	
	    	// test-1 (Invalid IP)
	    	result = restAPI.addMachine(INVALID_IP, VALID_USERNAME, VALID_PASSWORD);
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains("invalid ip address"));
	    	
	    	// test-2 (Invalid username)
	    	result = restAPI.addMachine(VALID_IP_1, INVALID_USERNAME, VALID_PASSWORD);
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains("invalid username"));
	    	
	    	// test-3 (Invalid password)
	    	result = restAPI.addMachine(VALID_IP_1, VALID_USERNAME, INVALID_PASSWORD);
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains("invalid password"));
	    	
	    	// test-4 (Invalid all parameters)
	    	result = restAPI.addMachine(INVALID_IP, INVALID_USERNAME, INVALID_PASSWORD);
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains("\"invalid ip address\",\"invalid username\",\"invalid password\""));
	    	
	    	// test-5 (Duplicate IP)
	    	restAPI.addMachine(VALID_IP_1, VALID_USERNAME, VALID_PASSWORD);
	    	result = restAPI.addMachine(VALID_IP_1, VALID_USERNAME, VALID_PASSWORD);
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains("ip duplicate"));
	    }

	    @Test(description = "Verify bulk register machines feature via RestfulAPI - Invalid parameters")
	    public void verifyBulkRegisterMachinesViaAPIInvalid() throws Exception{
			HashMap<String, String> machine1 = new HashMap<>();
			HashMap<String, String> machine2 = new HashMap<>();;
			HashMap<String, String> machine3 = new HashMap<>();;
			ArrayList<HashMap<String, String>> credentialsMapList = new ArrayList<>();
			String result;
			
			//test-1(Invalid IP)
			machine1.put("IP Address", INVALID_IP);
			machine1.put("Username", VALID_USERNAME);
			machine1.put("Password", VALID_PASSWORD);
			machine2.put("IP Address", VALID_IP_2);
			machine2.put("Username", VALID_USERNAME);
			machine3.put("IP Address", VALID_IP_3);
			machine3.put("Username", VALID_USERNAME);
			
			credentialsMapList.add(machine1);
			credentialsMapList.add(machine2);
			credentialsMapList.add(machine3);
			
	    	result = restAPI.addMachines(credentialsMapList);
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains(INVALID_IP) && result.contains("invalid ip address"));
	    	
	    	// test-2 (Invalid username)
	    	clearTestData(credentialsMapList);
	    	machine1.put("IP Address", VALID_IP_1);
			machine1.put("Username", INVALID_USERNAME);
			machine1.put("Password", VALID_PASSWORD);
			machine2.put("IP Address", VALID_IP_2);
			machine2.put("Username", VALID_USERNAME);
			machine3.put("IP Address", VALID_IP_3);
			machine3.put("Username", VALID_USERNAME);
			
			credentialsMapList.add(machine1);
			credentialsMapList.add(machine2);
			credentialsMapList.add(machine3);
			
	    	result = restAPI.addMachines(credentialsMapList);
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains(VALID_IP_1) && result.contains("invalid username"));
	    	
	    	// test-3 (Invalid password)
	    	clearTestData(credentialsMapList);
	    	machine1.put("IP Address", VALID_IP_1);
			machine1.put("Username", VALID_USERNAME);
			machine1.put("Password", INVALID_PASSWORD);
			machine2.put("IP Address", VALID_IP_2);
			machine2.put("Username", VALID_USERNAME);
			machine3.put("IP Address", VALID_IP_3);
			machine3.put("Username", VALID_USERNAME);
			
			credentialsMapList.add(machine1);
			credentialsMapList.add(machine2);
			credentialsMapList.add(machine3);
			
	    	result = restAPI.addMachines(credentialsMapList);
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains(VALID_IP_1) && result.contains("invalid password"));
	    	
	    	// test-4 (IP duplicate)
	    	result = restAPI.addMachines(credentialsMapList);
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains(String.format("\"%s\": [\"ip duplicate\"]", VALID_IP_2)));
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains(String.format("\"%s\": [\"ip duplicate\"]", VALID_IP_3)));
	    	
	    	// test-5 (Invalid all parameters)
	    	clearTestData(credentialsMapList);
	    	machine1.put("IP Address", INVALID_IP);
			machine1.put("Username", INVALID_USERNAME);
			machine1.put("Password", INVALID_PASSWORD);
			machine2.put("IP Address", VALID_IP_1);
			machine2.put("Username", INVALID_USERNAME);
			machine3.put("IP Address", VALID_IP_2);
			machine3.put("Username", INVALID_USERNAME);
			
			credentialsMapList.add(machine1);
			credentialsMapList.add(machine2);
			credentialsMapList.add(machine3);
			
	    	result = restAPI.addMachines(credentialsMapList);
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains(String.format("\"%s\": [\"invalid ip address\",\"invalid username\",\"invalid password\"]", INVALID_IP)));
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains(String.format("\"%s\": [\"invalid username\"]", VALID_IP_1)));
	    	Assert.assertTrue(result.contains("\"success\": false") && result.contains(String.format("\"%s\": [\"invalid username\"]", VALID_IP_2)));

	    }
	    
	    @Test(description = "Verify delete machines feature via RestfulAPI - succeed")
	    public void verifyDeleteMachinesViaAPIBasic() throws Exception{
	    	String result;
	    	restAPI.addMachine(VALID_IP_1, VALID_USERNAME);
	    	
	    	result = restAPI.deleteMachine(VALID_IP_1);
	    	Assert.assertTrue(result.contains("\"deleted machines\": 1,\"success\": true"));			
	    }
	    
	    @Test(description = "Verify bulk delete machines feature via RestfulAPI - succeed")
	    public void verifyBulkDeleteMachinesViaAPIBasic() throws Exception{
	    	List<String> deleteIPList = new ArrayList<>();
	    	
	    	restAPI.addMachine(VALID_IP_1, VALID_USERNAME);
	    	restAPI.addMachine(VALID_IP_2, VALID_USERNAME);
	    	restAPI.addMachine(VALID_IP_3, VALID_USERNAME);
	    	
	    	deleteIPList.add(VALID_IP_1);
	    	deleteIPList.add(VALID_IP_2);
	    	deleteIPList.add(VALID_IP_3);
			
	    	String result = restAPI.deleteMachines(deleteIPList);
	    	Assert.assertTrue(result.contains("true"));
	    }
	    
	    @Test(description = "Verify delete machines feature via RestfulAPI - Non existing machine")
	    public void verifyDeleteMachinesViaAPINonExist() throws Exception{
	    	String result = restAPI.deleteMachine(VALID_IP_1);
	    	Assert.assertTrue(result.contains("\"deleted machines\": 0,\"success\": false"));			
	    }

	    @Test(description = "Verify bulk delete machines feature via RestfulAPI - Non existing machines")
	    public void verifyBulkDeleteMachinesViaAPINonExist() throws Exception{
	    	String result;
	    	List<String> deleteIPList = new ArrayList<>();
	    	deleteIPList.add(VALID_IP_1);
	    	deleteIPList.add(VALID_IP_2);
	    	deleteIPList.add(VALID_IP_3);
	    	
	    	// test-1 (non exist)
	    	result = restAPI.deleteMachines(deleteIPList);
	    	Assert.assertTrue(result.contains("\"deleted machines\": 0,\"success\": false"));
	    	
	    	// test-2 (some machines exist)
	    	restAPI.addMachine(VALID_IP_1, VALID_USERNAME);
	    	restAPI.addMachine(VALID_IP_2, VALID_USERNAME);
	    	result = restAPI.deleteMachines(deleteIPList);
	    	Assert.assertTrue(result.contains("\"deleted machines\": 2,\"success\": true"));	
	    }
	    
	    public void clearTestData(ArrayList<HashMap<String, String>> credentialsMapList){
	    	restAPI.deleteAllUnknownMachines();
	    	for (HashMap<String, String> machine: credentialsMapList){
	    		machine.clear();
	    	}
	    	credentialsMapList.clear();
	    }
}
