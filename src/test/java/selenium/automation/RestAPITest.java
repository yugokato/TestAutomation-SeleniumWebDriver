package selenium.automation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import selenium.automation.base.BaseTest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static io.restassured.RestAssured.given;

public class RestAPITest extends BaseTest {
        private final String VALID_IP_1 = "1.1.1.1";
        private final String VALID_IP_2 = "2.2.2.2";
        private final String VALID_IP_3 = "3.3.3.3";
        private final String VALID_USERNAME = "ubuntu";
        private final String VALID_PASSWORD = "ubuntu";
        private final String INVALID_IP = "1.1.1";
        private final String INVALID_USERNAME = "%@*!";
        private final String INVALID_PASSWORD = "asdf.asdf";
        private final String TEST_HOSTNAME = "vm05";

        @BeforeClass
        public void beforeClass() {
            RestAssured.baseURI = "http://localhost:5000";
            RestAssured.basePath = "/api/machines";
        }
        
        @BeforeMethod
        public void beforeMethod(Method method) {
            System.out.println(method.getName());
            restAPI.deleteAllUnknownMachines();
        }

        @Test(description = "Verify getting machine data via RestfulAPI")
        public void verifyGetMachineDataViaAPI() throws Exception{
            Response response;
            
            // test-1 (a host)
            response = restAPI.getMachineData(TEST_HOSTNAME);
            response.then().assertThat()
                .statusCode(200)
                .body("data.Hostname", equalTo(TEST_HOSTNAME));
            
            // test-2 (All machines except Unknown)
            restAPI.registerMachine(VALID_IP_1, VALID_USERNAME);
            response = restAPI.getMachineData("all");
            response.then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("data[0].Hostname", equalTo("vm01"))
                .body("data[1].Hostname", equalTo("vm02"))
                .body("data[2].Hostname", equalTo("vm03"))
                .body("data[3].Hostname", equalTo("vm04"))
                .body("data[4].Hostname", equalTo("vm05"))
                .body("data[5].Hostname", equalTo("vm06"))
                .body("data[6].Hostname", equalTo("vm07"))
                .body("data[7].Hostname", equalTo("vm08"))
                .body("data[8].Hostname", equalTo("vm09"))
                .body("data[9].Hostname", equalTo("vm10"));
        }
        
        @Test(description = "Verify register machines feature via RestfulAPI - succeed")
        public void verifyRegisterMachinesViaAPIBasic() throws Exception{
            Response response;
            
            // test-1
            response = restAPI.registerMachine(VALID_IP_1, VALID_USERNAME, VALID_PASSWORD);
            response.then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(true));
            
            // test-2(no password)
            response = restAPI.registerMachine(VALID_IP_2, VALID_USERNAME);
            response.then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(true));    
        }
        
        @Test(description = "Verify bulk register machines feature via RestfulAPI - succeed")
        public void verifyBulkRegisterMachinesViaAPIBasic() throws Exception{
            Response response;
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
            
            response = restAPI.registerMachines(credentialsMapList);
            response.then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(true));    
        }
        
        @Test(description = "Verify register machines feature via RestfulAPI - Invalid parameters")
        public void verifyRegisterMachinesViaAPIInvalid() throws Exception{
            Response response;
            
            // test-1 (Invalid IP)
            response = restAPI.registerMachine(INVALID_IP, VALID_USERNAME, VALID_PASSWORD);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(false))
                .body("result.reason[0]", equalTo("invalid ip address"));
            
            // test-2 (Invalid username)
            response = restAPI.registerMachine(VALID_IP_1, INVALID_USERNAME, VALID_PASSWORD);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(false))
                .body("result.reason[0]", equalTo("invalid username"));
            
            // test-3 (Invalid password)
            response = restAPI.registerMachine(VALID_IP_1, VALID_USERNAME, INVALID_PASSWORD);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(false))
                .body("result.reason[0]", equalTo("invalid password"));
            
            // test-4 (Invalid all parameters)
            response = restAPI.registerMachine(INVALID_IP, INVALID_USERNAME, INVALID_PASSWORD);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(false))
                .body("result.reason", hasItems("invalid ip address", "invalid username", "invalid password"));

            // test-5 (Duplicate IP)
            restAPI.registerMachine(VALID_IP_1, VALID_USERNAME, VALID_PASSWORD);
            response = restAPI.registerMachine(VALID_IP_1, VALID_USERNAME, VALID_PASSWORD);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.reason[0]", equalTo("ip duplicate"));
        }

        @Test(description = "Verify bulk register machines feature via RestfulAPI - Invalid parameters")
        public void verifyBulkRegisterMachinesViaAPIInvalid() throws Exception{
            Response response;
            HashMap<String, String> machine1 = new HashMap<>();
            HashMap<String, String> machine2 = new HashMap<>();;
            HashMap<String, String> machine3 = new HashMap<>();;
            ArrayList<HashMap<String, String>> credentialsMapList = new ArrayList<>();
            
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
            
            response = restAPI.registerMachines(credentialsMapList);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(false))
                .body("result.reason.'" + INVALID_IP + "'[0]", equalTo("invalid ip address"));
            
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
            
            response = restAPI.registerMachines(credentialsMapList);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(false))
                .body("result.reason.'" + VALID_IP_1 + "'[0]", equalTo("invalid username"));
            
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
            
            response = restAPI.registerMachines(credentialsMapList);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(false))
                .body("result.reason.'" + VALID_IP_1 + "'[0]", equalTo("invalid password"));
            
            // test-4 (IP duplicate)
            response = restAPI.registerMachines(credentialsMapList);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(false))
                .body("result.reason.'" + VALID_IP_2 + "'[0]", equalTo("ip duplicate"))
                .body("result.reason.'" + VALID_IP_3 + "'[0]", equalTo("ip duplicate"));
            
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
            
            response = restAPI.registerMachines(credentialsMapList);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(false))
                .body("result.reason.'" + INVALID_IP + "'", hasItems("invalid ip address", "invalid username", "invalid password"))
                .body("result.reason.'" + VALID_IP_1 + "'[0]", equalTo("invalid username"))
                .body("result.reason.'" + VALID_IP_2 + "'[0]", equalTo("invalid username"));
        }
        
        @Test(description = "Verify delete machines feature via RestfulAPI - succeed")
        public void verifyDeleteMachinesViaAPIBasic() throws Exception{
            restAPI.registerMachine(VALID_IP_1, VALID_USERNAME);
            
            Response response;
            response = restAPI.deleteMachine(VALID_IP_1);
            response.then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(true))
                .body("result.'deleted machines'", equalTo(1));            
        }
        
        @Test(description = "Verify bulk delete machines feature via RestfulAPI - succeed")
        public void verifyBulkDeleteMachinesViaAPIBasic() throws Exception{           
            restAPI.registerMachine(VALID_IP_1, VALID_USERNAME);
            restAPI.registerMachine(VALID_IP_2, VALID_USERNAME);
            restAPI.registerMachine(VALID_IP_3, VALID_USERNAME);
            
            Response response;
            List<String> deleteIPList = new ArrayList<>();
            
            deleteIPList.add(VALID_IP_1);
            deleteIPList.add(VALID_IP_2);
            deleteIPList.add(VALID_IP_3);
            
            response = restAPI.deleteMachines(deleteIPList);
            response.then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(true));
        }
        
        @Test(description = "Verify delete machines feature via RestfulAPI - Non existing machine")
        public void verifyDeleteMachinesViaAPINonExist() throws Exception{
            Response response;
            response = restAPI.deleteMachine(VALID_IP_1);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(false))
                .body("result.'deleted machines'", equalTo(0));              
        }

        @Test(description = "Verify bulk delete machines feature via RestfulAPI - Non existing machines")
        public void verifyBulkDeleteMachinesViaAPINonExist() throws Exception{
            Response response;
            List<String> deleteIPList = new ArrayList<>();
            deleteIPList.add(VALID_IP_1);
            deleteIPList.add(VALID_IP_2);
            deleteIPList.add(VALID_IP_3);
            
            // test-1 (non exist)
            response = restAPI.deleteMachines(deleteIPList);
            response.then().assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(false))
                .body("result.'deleted machines'", equalTo(0));  
            
            // test-2 (some machines exist)
            restAPI.registerMachine(VALID_IP_1, VALID_USERNAME);
            restAPI.registerMachine(VALID_IP_2, VALID_USERNAME);
            response = restAPI.deleteMachines(deleteIPList);
            response.then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("result.success", equalTo(true))
                .body("result.'deleted machines'", equalTo(2));  
        }
        
        @Test(description = "Verify bulk register machines with no body returns status code 400(Bad Request)")
        public void verifyBulkRegisterMachinesViaAPIWithNoBody() throws Exception{
            Response response;
            response = given().contentType(ContentType.JSON).when().post("/add");
            response.then().assertThat()
                    .statusCode(400)
                    .contentType(ContentType.JSON)
                    .body("result.error", equalTo("The browser (or proxy) sent a request that this server could not understand."));
        }
        
        @Test(description = "Verify bulk delete machines with no body returns status code 400(Bad Request)")
        public void verifyBulkDeleteMachinesViaAPIWithNoBody() throws Exception{
            Response response;
            response = given().contentType(ContentType.JSON).when().delete("/delete");
            response.then().assertThat()
                    .statusCode(400)
                    .contentType(ContentType.JSON)
                    .body("result.error", equalTo("The browser (or proxy) sent a request that this server could not understand."));
        }
        
        @Test(description = "Verify getting nonexist machine returns status code 404(Not Found)")
        public void verifyGetUnknownMachineDataViaAPI() throws Exception{
            Response response;
            response = given().contentType(ContentType.JSON).when().get("/vm99");
            response.then().assertThat()
                    .statusCode(404)
                    .contentType(ContentType.JSON)
                    .body("result.error", equalTo("The requested URL was not found on the server.  If you entered the URL manually please check your spelling and try again."));            
        }
        
        @Test(description = "Verify POST/DELETE to unknown URL returns status code 405(Method Not Allowed)")
        public void verifyRequestUnallowedURLViaAPI() throws Exception{
            Response response;
            
            // test-1 (POST)
            response = given().contentType(ContentType.JSON).when().post("/non_exist");
            response.then().assertThat()
                    .statusCode(405)
                    .contentType(ContentType.JSON)
                    .body("result.error", equalTo("The method is not allowed for the requested URL."));
            
            // test-2 (DELETE)
            response = given().contentType(ContentType.JSON).when().delete("/non_exist");
            response.then().assertThat()
                    .statusCode(405)
                    .contentType(ContentType.JSON)
                    .body("result.error", equalTo("The method is not allowed for the requested URL."));
        }
        
        public void clearTestData(ArrayList<HashMap<String, String>> credentialsMapList){
            restAPI.deleteAllUnknownMachines();
            for (HashMap<String, String> machine: credentialsMapList){
                machine.clear();
            }
            credentialsMapList.clear();
        }
}
