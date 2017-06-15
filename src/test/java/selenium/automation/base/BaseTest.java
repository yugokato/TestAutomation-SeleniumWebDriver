package selenium.automation.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.spotify.docker.client.DockerClient;

import framework.api.RestAPI;
import framework.driver.DriverInit;
import framework.pages.LoginPage;
import framework.pages.HomePage;
import framework.docker.DockerManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class BaseTest {
    protected final static Logger logger = Logger.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected RestAPI restAPI;
    protected LoginPage loginPage;
    protected HomePage homePage;
    protected DockerManager containerManager;
    protected DockerClient docker;
    protected String APP_FRAMEWORK;
    protected String APP_BASE_URL;
    private static final String TEST_ADMIN_USER = "ADMIN_USER";
    private static final String TEST_ADMIN_PASSWORD = "ADMIN_PASSWORD";
    private static final String[] INITIAL_CONTAINERS = 
        {"/vm01", "/vm02", "/vm03", "/vm04", "/vm05", "/vm06", 
                "/vm07", "/vm08", "/vm09", "/vm10", "/mongo"};
    
    @BeforeClass(alwaysRun=true)
    public void beforeClassBase() {
        setTestProperty();
        PropertyConfigurator.configure(System.getProperty("user.dir") + "/" + "resources/log4j.property");
        System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "/resources/chromedriver");
        DesiredCapabilities chromeCapabilities = DriverInit.setChromeCapabilities();
        DriverInit.setDriver(new ChromeDriver(chromeCapabilities));
        driver = DriverInit.getDriver();
        restAPI = new RestAPI();
        loginPage = new LoginPage();
        homePage = new HomePage();
        docker = DockerManager.getDockerClient();
        restAPI.addUser(TEST_ADMIN_USER, TEST_ADMIN_PASSWORD);
    }
    
    @BeforeMethod(alwaysRun=true)
    public void beforeMethodBase() throws Exception{
        DockerManager.startTestContainers(INITIAL_CONTAINERS);
        driver.get(APP_BASE_URL);
        driver.manage().window().maximize();
        loginPage.doLogin(TEST_ADMIN_USER, TEST_ADMIN_PASSWORD, false);
    }
    
    @AfterMethod(alwaysRun=true)
    public void afterMethodBase() throws Exception{
        driver.get(APP_BASE_URL + "/portal/logout");
    }

    @AfterClass(alwaysRun=true)
    public void afterClassBase() {
        restAPI.deleteAllUnknownMachines();
        restAPI.deleteUser(TEST_ADMIN_USER);
        driver.quit();
    }
    
    public void setTestProperty(){
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("resources/config.property");

            // load a properties file
            prop.load(input);

            // set the property value
            System.setProperty("APP_FRAMEWORK", prop.getProperty("APP_FRAMEWORK"));
            System.setProperty("APP_BASE_URL", prop.getProperty("APP_BASE_URL"));
            System.setProperty("API_PATH_MACHINES", prop.getProperty("API_PATH_MACHINES"));
            System.setProperty("API_PATH_USERS", prop.getProperty("API_PATH_USERS"));

            APP_FRAMEWORK = System.getProperty("APP_FRAMEWORK");
            APP_BASE_URL = System.getProperty("APP_BASE_URL");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

      }
}
